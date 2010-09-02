package org.geoserver.python;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.simple.SimpleFeatureSource;
import org.h2.tools.DeleteDbFiles;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.python.util.PythonInterpreter;
import org.springframework.context.ApplicationContext;

public class GeoServerPythonTest {

    static Python python;
    PythonInterpreter pi;
    ByteArrayOutputStream out;
    
    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        python = new Python(new GeoServerResourceLoader(new File("target")));
    }

    
    @Before
    public void setUp() throws Exception {
        
        out = new ByteArrayOutputStream();
        pi = python.interpreter();
        pi.setOut(out);
    }
    
    public void testGeoServerPy() throws Exception {
        
        pi.exec("import sys");
        pi.exec("print sys.path");
        print();
        clear();
        
        pi.exec("from geoserver import Catalog");
        pi.exec("cat = Catalog('sf')");
        pi.exec("stores = cat.stores()");
        clear();
        pi.exec("print [str(s) for s in stores]");
        _assert("['sf']");
        clear();
        
        pi.exec("s = cat.get('sf')");
        pi.exec("print len(s.layers())");
        _assert("3");
        clear();
            
        pi.exec("print [str(l) for l in s.layers()]");
        _assert("['AggregateGeoFeature', 'GenericEntity', 'PrimitiveGeoFeature']");
        clear();
    }
    
    @Test
    public void testGeoServerPySave() throws Exception {
        ApplicationContext context = createMock(ApplicationContext.class);
        Catalog catalog = createMock(Catalog.class);
        expect(context.getBean("catalog")).andReturn(catalog);
        
        DataStoreInfo store = createMock(DataStoreInfo.class);
        expect(catalog.getDataStoreByName("sf", "sf")).andReturn(store);
        
        DataStore ds = createMock(DataStore.class);
        SimpleFeatureSource fs = createMock(SimpleFeatureSource.class);
        SimpleFeatureType ft = createMock(SimpleFeatureType.class);
        
        expect(store.getDataStore(null)).andReturn((DataAccess) ds);
        expect(ds.getFeatureSource("PrimitiveGeoFeature")).andReturn(fs);
        expect(fs.getSchema()).andReturn(ft).anyTimes();
        
        FeatureTypeInfo featureType = createMock(FeatureTypeInfo.class);
        expect(featureType.getName()).andReturn("PrimitiveGeoFeature");
        expect(featureType.getFeatureSource(null,null)).andReturn((FeatureSource)fs);
        expect(catalog.getFeatureTypesByStore(store)).andReturn(Arrays.asList(featureType));
        expect(catalog.getFeatureTypeByStore(store, "PrimitiveGeoFeature")).andReturn(featureType);
        
        featureType.setTitle("changed");
        expectLastCall();
        
        catalog.save(featureType);
        expectLastCall();
        
        new GeoServerExtensions().setApplicationContext(context);
        
        replay(context);
        replay(catalog);
        replay(store);
        replay(featureType);
        replay(ds);
        replay(fs);
        replay(ft);

        pi.exec("from geoserver import Catalog");
        pi.exec("cat = Catalog('sf')");
        pi.exec("sf = cat.get('sf')");
        pi.exec("pg = sf.get('PrimitiveGeoFeature')");
        pi.exec("pg.meta.title = 'changed'");
        pi.exec("pg.save()");
    }
    
    @Test
    public void testAddDataStore() throws Exception {
        ApplicationContext context = createMock(ApplicationContext.class);
        
        Catalog catalog = createMock(Catalog.class);
        expect(context.getBean("catalog")).andReturn(catalog);
        
        WorkspaceInfo ws = createMock(WorkspaceInfo.class);
        expect(ws.getName()).andReturn("gs").anyTimes();
        expect(catalog.getDefaultWorkspace()).andReturn(ws).anyTimes();
        expect(catalog.getWorkspaceByName("gs")).andReturn(ws).anyTimes();
        expect(catalog.getWorkspaceByName(null)).andReturn(ws).anyTimes();
        
        CatalogFactory fac = createMock(CatalogFactory.class);
        expect(catalog.getFactory()).andReturn(fac).anyTimes();
        
        DataStoreInfo info = createMock(DataStoreInfo.class);
        expect(fac.createDataStore()).andReturn(info);
        
        info.setName("Foo");
        expectLastCall();
        
        info.setWorkspace(ws);
        expectLastCall();
        
        expect(info.getConnectionParameters()).andReturn(new HashMap());
        
        catalog.add(info);
        expectLastCall();
        
        replay(context);
        replay(catalog);
        replay(fac);
        replay(info);
            
        new GeoServerExtensions().setApplicationContext(context);
        
        DeleteDbFiles.execute("target", "foo", true);
        
        pi.exec("from geoserver import Catalog");
        pi.exec("from geoscript.workspace import H2");
        pi.exec("from geoscript.geom import Point");
        pi.exec("cat = Catalog()");
        pi.exec("h2 = H2('foo', 'target')");
        pi.exec("l = h2.create('bar', [('geom', Point, 'epsg:4326')])");
        pi.exec("l.add([Point(10,10)])");
        pi.exec("l.add([Point(20,20)])");
        pi.exec("cat.add(h2, 'Foo')");

        pi.exec("h2.close()");
    }

    void print() {
        System.out.println(new String(out.toByteArray()));
    }
    
    void clear() {
        out = new ByteArrayOutputStream();
        pi.setOut(out);
    }
    
    void _assert(String result) {
        assertEquals(result, new String(out.toByteArray()).trim());
    }
    
}
