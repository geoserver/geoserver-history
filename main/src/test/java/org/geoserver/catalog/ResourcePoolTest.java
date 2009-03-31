/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.catalog;

import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;
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

}
