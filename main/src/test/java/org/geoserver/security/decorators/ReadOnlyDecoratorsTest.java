package org.geoserver.security.decorators;

import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.impl.LayerGroupInfoImpl;
import org.geoserver.security.AbstractAuthorizationTest;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Transaction;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

public class ReadOnlyDecoratorsTest extends AbstractAuthorizationTest {
    
    public void testReadOnlyLayerInfoFeatures() {
        ReadOnlyLayerInfo ro = new ReadOnlyLayerInfo(statesLayer);
        
        assertFalse(statesLayer.getResource() instanceof ReadOnlyFeatureTypeInfo);
        assertTrue(ro.getResource() instanceof ReadOnlyFeatureTypeInfo);
    }
    
    public void testReadOnlyLayerInfoCoverages() {
        ReadOnlyLayerInfo ro = new ReadOnlyLayerInfo(arcGridLayer);
        
        assertSame(arcGridLayer.getResource(), ro.getResource());
    }
    
    public void testReadOnlyFeatureTypeInfo() throws Exception {
        ReadOnlyFeatureTypeInfo ro = new ReadOnlyFeatureTypeInfo(states);
        assertTrue(ro.getFeatureSource(null, null) instanceof ReadOnlyFeatureSource);
        assertTrue(ro.getStore() instanceof ReadOnlyDataStoreInfo);
    }
    
    public void testReadOnlyDataStoreInfo() throws Exception {
        ReadOnlyDataStoreInfo ro = new ReadOnlyDataStoreInfo(statesStore);
        assertTrue(ro.getDataStore(null) instanceof ReadOnlyDataStore);
    }
    
    public void testReadOnlyFeatureSourceDataStore() throws Exception {
        DataStore ds = createNiceMock(DataStore.class);
        replay(ds);
        FeatureSource fs = createNiceMock(FeatureSource.class);
        FeatureCollection fc = createNiceMock(FeatureCollection.class);
        expect(fs.getDataStore()).andReturn(ds);
        expect(fs.getFeatures()).andReturn(fc);
        expect(fs.getFeatures(Filter.INCLUDE)).andReturn(fc);
        expect(fs.getFeatures(new DefaultQuery())).andReturn(fc);
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
        FeatureType schema = createNiceMock(FeatureType.class);
        replay(schema);
        DataAccess da = createNiceMock(DataAccess.class);
        Name name = new NameImpl("blah");
        expect(da.getFeatureSource(name)).andReturn(fs);
        replay(da);
        ReadOnlyDataAccess ro = new ReadOnlyDataAccess(da);
        assertTrue(ro.getFeatureSource(name) instanceof ReadOnlyFeatureSource);
        
        // check the easy ones, those that are not implemented in a read only
        // collection
        try {
            ro.createSchema(null);
            fail("Should have failed with an IOException");
        } catch(IOException e) {
            assertEquals(ReadOnlyDataAccess.READ_ONLY, e.getMessage());
        }
        try {
            ro.updateSchema(null, null);
            fail("Should have failed with an IOException");
        } catch(IOException e) {
            assertEquals(ReadOnlyDataAccess.READ_ONLY, e.getMessage());
        }
    }
    
    public void testReadOnlyDataStore() throws Exception {
        FeatureSource fs = createNiceMock(FeatureSource.class);
        replay(fs);
        DataStore ds = createNiceMock(DataStore.class);
        expect(ds.getFeatureSource("blah")).andReturn(fs);
        replay(ds);
        ReadOnlyDataStore ro = new ReadOnlyDataStore(ds);
        assertTrue(ro.getFeatureSource("blah") instanceof ReadOnlyFeatureSource); 
        
        // check the easy ones, those that are not implemented in a read only
        // collection
        try {
            ro.createSchema(null);
            fail("Should have failed with an IOException");
        } catch(IOException e) {
            assertEquals(ReadOnlyDataStore.READ_ONLY, e.getMessage());
        }
        try {
            ro.updateSchema((String) null, null);
            fail("Should have failed with an IOException");
        } catch(IOException e) {
            assertEquals(ReadOnlyDataStore.READ_ONLY, e.getMessage());
        }
        
        try {
            ro.updateSchema((Name) null, null);
            fail("Should have failed with an IOException");
        } catch(IOException e) {
            assertEquals(ReadOnlyDataStore.READ_ONLY, e.getMessage());
        }
        try {
            ro.getFeatureWriter("states", Transaction.AUTO_COMMIT);
            fail("Should have failed with an IOException");
        } catch(IOException e) {
            assertEquals(ReadOnlyDataStore.READ_ONLY, e.getMessage());
        }
        try {
            ro.getFeatureWriter("states", Filter.INCLUDE, Transaction.AUTO_COMMIT);
            fail("Should have failed with an IOException");
        } catch(IOException e) {
            assertEquals(ReadOnlyDataStore.READ_ONLY, e.getMessage());
        }
        try {
            ro.getFeatureWriterAppend("states", Transaction.AUTO_COMMIT);
            fail("Should have failed with an IOException");
        } catch(IOException e) {
            assertEquals(ReadOnlyDataStore.READ_ONLY, e.getMessage());
        }
    }

    public void testReadOnlyFeatureCollection() throws Exception {
        Feature feature = createNiceMock(Feature.class);
        replay(feature);
        Iterator it = createNiceMock(Iterator.class);
        replay(it);
        final SortBy sort = createNiceMock(SortBy.class);
        replay(sort);
        FeatureCollection fc = createNiceMock(FeatureCollection.class);
        expect(fc.iterator()).andReturn(it).anyTimes();
        expect(fc.sort(sort)).andReturn(fc).anyTimes();
        expect(fc.subCollection(Filter.INCLUDE)).andReturn(fc).anyTimes();
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
        
        // check derived collections are still read only
        assertTrue(ro.sort(sort) instanceof ReadOnlyFeatureCollection);
        assertTrue(ro.subCollection(Filter.INCLUDE) instanceof ReadOnlyFeatureCollection);
    }
    
    public void testReadOnlyLayerGroup() {
        LayerGroupInfo group = new LayerGroupInfoImpl();
        group.getLayers().addAll(Arrays.asList(statesLayer, roadsLayer));
        ReadOnlyLayerGroup ro = new ReadOnlyLayerGroup(group);
        
        assertEquals(group.getLayers().size(), ro.getLayers().size());
        for (LayerInfo layer : ro.getLayers()) {
            assertTrue(layer instanceof ReadOnlyLayerInfo);
        }
    }
}
