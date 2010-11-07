/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.catalog;

import java.io.IOException;

import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;
import org.geotools.data.DataAccess;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Tests for {@link ResourcePool}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class ResourcePoolTest extends GeoServerTestSupport {

    /**
     * Test that the {@link FeatureType} cache returns the same instance every time. This is assumed
     * by some nasty code in other places that tampers with the CRS. If a new {@link FeatureType} is
     * constructed for the same {@link FeatureTypeInfo}, Bad Things Happen (TM).
     */
    public void testFeatureTypeCacheInstance() throws Exception {
        ResourcePool pool = new ResourcePool(getCatalog());
        FeatureTypeInfo info = getCatalog().getFeatureTypeByName(
                MockData.LAKES.getNamespaceURI(), MockData.LAKES.getLocalPart());
        FeatureType ft1 = pool.getFeatureType(info);
        FeatureType ft2 = pool.getFeatureType(info);
        FeatureType ft3 = pool.getFeatureType(info);
        assertSame(ft1, ft2);
        assertSame(ft1, ft3);
    }
    
    boolean cleared = false;
    public void testCacheClearing() throws IOException {
        cleared = false;
        ResourcePool pool = new ResourcePool(getCatalog()) {
            @Override
            public void clear(FeatureTypeInfo info) {
                cleared = true;
                super.clear(info);
            }
        };
        FeatureTypeInfo info = getCatalog().getFeatureTypeByName(
                MockData.LAKES.getNamespaceURI(), MockData.LAKES.getLocalPart());
        
        assertNotNull( pool.getFeatureType( info ) );
        info.setTitle("changed");
        
        assertFalse( cleared );
        getCatalog().save( info );
        assertTrue( cleared );
        
        cleared = false;
        assertNotNull( pool.getFeatureType( info ) );
        
        for ( LayerInfo l : getCatalog().getLayers( info ) ) {
            getCatalog().remove( l );
        }
        getCatalog().remove( info );
        assertTrue( cleared );
    }

    boolean disposeCalled;

    /**
     * Make sure {@link ResourcePool#clear(DataStoreInfo)} and {@link ResourcePool#dispose()} call
     * {@link DataAccess#dispose()}
     */
    public void testDispose() throws IOException {
        disposeCalled = false;
        class ResourcePool2 extends ResourcePool {
            @SuppressWarnings("serial")
            public ResourcePool2(Catalog catalog) {
                super(catalog);
                dataStoreCache = new DataStoreCache() {
                    @SuppressWarnings("unchecked")
                    @Override
                    void dispose(String name, DataAccess dataStore) {
                        disposeCalled = true;
                        super.dispose(name, dataStore);
                    }
                };
            }
        }

        Catalog catalog = getCatalog();
        ResourcePool pool = new ResourcePool2(catalog);
        catalog.setResourcePool(pool);

        DataStoreInfo info = catalog.getDataStores().get(0);
        // force the datastore to be created
        DataAccess<? extends FeatureType, ? extends Feature> dataStore = pool.getDataStore(info);
        assertNotNull(dataStore);
        assertFalse(disposeCalled);
        pool.clear(info);
        assertTrue(disposeCalled);

        // force the datastore to be created
        dataStore = pool.getDataStore(info);
        assertNotNull(dataStore);
        disposeCalled = false;
        pool.dispose();
        assertTrue(disposeCalled);
    }
    
    public void testLRU() throws IOException {
        Catalog catalog = getCatalog();
        ResourcePool pool = new ResourcePool(catalog);
        pool.setFeatureTypeCacheSize(2);
        catalog.setResourcePool(pool);
        FeatureTypeInfo lakes = catalog.getFeatureTypeByName(MockData.LAKES.getNamespaceURI(),
                MockData.LAKES.getLocalPart());
        FeatureTypeInfo lines = catalog.getFeatureTypeByName(MockData.LINES.getNamespaceURI(),
                MockData.LINES.getLocalPart());
        FeatureTypeInfo locks = catalog.getFeatureTypeByName(MockData.LOCKS.getNamespaceURI(),
                MockData.LOCKS.getLocalPart());
        pool.getFeatureType(lakes);
        pool.getFeatureType(lines);
        pool.getFeatureType(locks);
        assertEquals("LRU cache size should never exceed set limit", 2, pool.featureTypeCache
                .size());
    }

}
