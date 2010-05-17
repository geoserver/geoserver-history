package org.geoserver.security.decorators;

import static org.easymock.EasyMock.*;
import junit.framework.TestCase;

import org.acegisecurity.AcegiSecurityException;
import org.geoserver.security.SecureObjectsTest;
import org.geoserver.security.SecureCatalogImpl.WrapperPolicy;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.FeatureStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

public class ReadOnlyFeatureSourceTest extends SecureObjectsTest {

    public void testReadOnlyFeatureSourceDataStore() throws Exception {
        // build up the mock
        DataStore ds = createNiceMock(DataStore.class);
        replay(ds);
        FeatureSource fs = createNiceMock(FeatureSource.class);
        FeatureCollection fc = createNiceMock(FeatureCollection.class);
        expect(fs.getDataStore()).andReturn(ds);
        expect(fs.getFeatures()).andReturn(fc);
        expect(fs.getFeatures(Filter.INCLUDE)).andReturn(fc);
        expect(fs.getFeatures(new DefaultQuery())).andReturn(fc);
        replay(fs);
        
        ReadOnlyFeatureSource ro = new ReadOnlyFeatureSource(fs, WrapperPolicy.HIDE);
        assertTrue(ro.getDataStore() instanceof ReadOnlyDataStore); 
        ReadOnlyFeatureCollection collection = (ReadOnlyFeatureCollection) ro.getFeatures();
        assertEquals(WrapperPolicy.HIDE, ro.policy);
        assertTrue(ro.getFeatures(Filter.INCLUDE) instanceof ReadOnlyFeatureCollection);
        assertTrue(ro.getFeatures(new DefaultQuery()) instanceof ReadOnlyFeatureCollection);
    }
    
    public void testReadOnlyFeatureStore() throws Exception {
        // build up the mock
        SimpleFeatureType schema = createNiceMock(SimpleFeatureType.class);
        expect(schema.getName()).andReturn(new NameImpl("testFT"));
        replay(schema);
        FeatureStore fs = createNiceMock(FeatureStore.class);
        expect(fs.getSchema()).andReturn(schema);
        replay(fs);
        
        ReadOnlyFeatureStore ro = new ReadOnlyFeatureStore(fs, WrapperPolicy.RO_CHALLENGE);
        try {
            ro.addFeatures(createNiceMock(FeatureCollection.class));
            fail("This should have thrown a security exception");
        } catch(AcegiSecurityException e) {
            // ok
        }
    }
    
    
    public void testReadOnlyFeatureSourceDataAccess() throws Exception {
        // build the mock up
        DataAccess da = createNiceMock(DataAccess.class);
        replay(da);
        FeatureSource fs = createNiceMock(FeatureSource.class);
        expect(fs.getDataStore()).andReturn(da);
        replay(fs);
        
        ReadOnlyFeatureSource ro = new ReadOnlyFeatureSource(fs, WrapperPolicy.RO_CHALLENGE);
        assertTrue(ro.getDataStore() instanceof ReadOnlyDataAccess); 
    }
}
