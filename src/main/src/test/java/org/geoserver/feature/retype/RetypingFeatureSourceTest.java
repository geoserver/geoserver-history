package org.geoserver.feature.retype;

import static org.geoserver.data.test.MockData.BRIDGES;
import static org.geoserver.data.test.MockData.BUILDINGS;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.geoserver.data.test.MockData;
import org.geoserver.data.util.IOUtils;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureLocking;
import org.geotools.data.FeatureSource;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

public class RetypingFeatureSourceTest extends TestCase {

    static final String RENAMED = "houses";

    RetypingDataStore rts;

    private File data;

    private PropertyDataStore store;

    @Override
    protected void setUp() throws Exception {
        data = File.createTempFile("retype", "data", new File("./target"));
        data.delete();
        data.mkdir();

        copyTestData(BUILDINGS, data);
        copyTestData(BRIDGES, data);

        store = new PropertyDataStore(data);
    }

    void copyTestData(QName testData, File data) throws IOException {
        String fileName = testData.getLocalPart() + ".properties";
        URL properties = MockData.class.getResource(fileName);
        IOUtils.copy(properties.openStream(), new File(data, fileName));
    }

    public void testSimpleRename() throws IOException {
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = store.getFeatureSource(BUILDINGS.getLocalPart());
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(fs.getSchema());
        tb.setName("houses");
        SimpleFeatureType target = tb.buildFeatureType();

        FeatureSource<SimpleFeatureType, SimpleFeature> retyped = RetypingFeatureSource.getRetypingSource(fs, target);
        assertTrue(retyped instanceof FeatureLocking);
        assertEquals(target, retyped.getSchema());
        assertEquals(target, ((DataStore) retyped.getDataStore()).getSchema("houses"));
        assertEquals(target, retyped.getFeatures().getSchema());

        FeatureIterator<SimpleFeature> it = retyped.getFeatures().features();
        SimpleFeature f = it.next();
        it.close();
        assertEquals(target, f.getType());
    }

    public void testConflictingRename() throws IOException {
        // we rename buildings to a feature type that's already available in the data store
        FeatureSource<SimpleFeatureType, SimpleFeature> fs = store.getFeatureSource(BUILDINGS.getLocalPart());
        assertEquals(2, store.getTypeNames().length);
        assertNotNull(store.getSchema(BRIDGES.getLocalPart()));

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(fs.getSchema());
        tb.setName(BRIDGES.getLocalPart());
        SimpleFeatureType target = tb.buildFeatureType();

        FeatureSource<SimpleFeatureType, SimpleFeature> retyped = RetypingFeatureSource.getRetypingSource(fs, target);
        assertEquals(target, retyped.getSchema());
        DataStore rs = (DataStore) retyped.getDataStore();
        assertEquals(1, rs.getTypeNames().length);
        assertEquals(BRIDGES.getLocalPart(), rs.getTypeNames()[0]);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        String fid = BRIDGES.getLocalPart() + ".1107531701011";
        Filter fidFilter = ff.id(Collections.singleton(ff.featureId(fid)));

        FeatureIterator<SimpleFeature> it = retyped.getFeatures(fidFilter).features();
        assertTrue(it.hasNext());
        SimpleFeature f = it.next();
        assertFalse(it.hasNext());
        it.close();

        // _=the_geom:MultiPolygon,FID:String,ADDRESS:String
        // Buildings.1107531701010=MULTIPOLYGON (((0.0008 0.0005, 0.0008 0.0007, 0.0012 0.0007,
        // 0.0012 0.0005, 0.0008 0.0005)))|113|123 Main Street
        // Buildings.1107531701011=MULTIPOLYGON (((0.002 0.0008, 0.002 0.001, 0.0024 0.001, 0.0024
        // 0.0008, 0.002 0.0008)))|114|215 Main Street
        assertEquals("114", f.getAttribute("FID"));
        assertEquals("215 Main Street", f.getAttribute("ADDRESS"));
    }
}
