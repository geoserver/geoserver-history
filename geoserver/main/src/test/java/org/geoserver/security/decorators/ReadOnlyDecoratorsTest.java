package org.geoserver.security.decorators;

import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Iterator;

import org.geoserver.security.AbstractAuthorizationTest;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

public class ReadOnlyDecoratorsTest extends AbstractAuthorizationTest {
    
    public void testReadOnlyLayerInfo() {
        ReadOnlyLayerInfo ro = new ReadOnlyLayerInfo(statesLayer);
        
        assertFalse(statesLayer.getResource() instanceof ReadOnlyFeatureTypeInfo);
        assertTrue(ro.getResource() instanceof ReadOnlyFeatureTypeInfo);
    }
    
    public void testReadOnlyFeatureTypeInfo() throws Exception {
        ReadOnlyFeatureTypeInfo ro = new ReadOnlyFeatureTypeInfo(states);
        assertTrue(ro.getFeatureSource(null, null) instanceof ReadOnlyFeatureSource);
        assertTrue(ro.getStore() instanceof ReadOnlyDataStoreInfo);
    }
    
    public void testReadOnlyFeatureSourceDataStore() throws Exception {
        DataStore ds = createNiceMock(DataStore.class);
        replay(ds);
        FeatureSource fs = createNiceMock(FeatureSource.class);
        expect(fs.getDataStore()).andReturn(ds);
        replay(fs);
        ReadOnlyFeatureSource ro = new ReadOnlyFeatureSource(fs);
        assertTrue(ro.getDataStore() instanceof ReadOnlyDataStore); 
        assertTrue(ro.getFeatures() instanceof ReadOnlyFeatureCollection);
        assertTrue(ro.getFeatures(Filter.INCLUDE) instanceof ReadOnlyFeatureCollection);
        assertTrue(ro.getFeatures(new DefaultQuery()) instanceof ReadOnlyFeatureCollection);
    }
    
    
    public void testReadOnlyFeatureSourceDataAccess() throws Exception {
        DataAccess da = createNiceMock(DataAccess.class);
        replay(da);
        FeatureSource fs = createNiceMock(FeatureSource.class);
        expect(fs.getDataStore()).andReturn(da);
        replay(fs);
        ReadOnlyFeatureSource ro = new ReadOnlyFeatureSource(fs);
        assertTrue(ro.getDataStore() instanceof ReadOnlyDataAccess); 
    }
    
    public void testReadOnlyDataAccess() throws Exception {
        FeatureSource fs = createNiceMock(FeatureSource.class);
        replay(fs);
        DataAccess da = createNiceMock(DataAccess.class);
        Name name = new NameImpl("blah");
        expect(da.getFeatureSource(name)).andReturn(fs);
        replay(da);
        ReadOnlyDataAccess ro = new ReadOnlyDataAccess(da);
        assertTrue(ro.getFeatureSource(name) instanceof ReadOnlyFeatureSource); 
    }
    
    public void testReadOnlyDataStore() throws Exception {
        FeatureSource fs = createNiceMock(FeatureSource.class);
        replay(fs);
        DataStore ds = createNiceMock(DataStore.class);
        expect(ds.getFeatureSource("blah")).andReturn(fs);
        replay(ds);
        ReadOnlyDataStore ro = new ReadOnlyDataStore(ds);
        assertTrue(ro.getFeatureSource("blah") instanceof ReadOnlyFeatureSource); 
    }

    public void testReadOnlyFeatureCollection() throws Exception {
        Feature feature = createNiceMock(Feature.class);
        replay(feature);
        Iterator it = createNiceMock(Iterator.class);
        replay(it);
        FeatureCollection fc = createNiceMock(FeatureCollection.class);
        expect(fc.iterator()).andReturn(it).anyTimes();
        replay(fc);
        
        ReadOnlyFeatureCollection ro = new ReadOnlyFeatureCollection(fc);
        
        // check the easy ones, those that are not implemented in a read only
        // collection
        try {
            ro.add(feature);
            fail("Should have failed with an UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.addAll(new ArrayList());
            fail("Should have failed with an UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.clear();
            fail("Should have failed with an UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.remove(feature);
            fail("Should have failed with an UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.removeAll(new ArrayList());
            fail("Should have failed with an UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // ok
        }
        try {
            ro.retainAll(new ArrayList());
            fail("Should have failed with an UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // ok
        }
        
        // let's check the iterator, should allow read but not remove
        Iterator roit = ro.iterator();
        roit.hasNext();
        roit.next();
        try {
            roit.remove();
            fail("Should have failed with an UnsupportedOperationException");
        } catch(UnsupportedOperationException e) {
            // ok
        }
    }
}
