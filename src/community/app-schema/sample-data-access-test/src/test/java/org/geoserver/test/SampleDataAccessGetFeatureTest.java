/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import org.geotools.data.SampleDataAccess;

/**
 * WFS GetFeature to test integration of {@link SampleDataAccess} with GeoServer.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class SampleDataAccessGetFeatureTest extends SampleDataAccessGeoServerTestSupport {

    /**
     * Test if GetFeature returns parsable XML.
     * 
     * @throws Exception
     */
    public void testGetFeature() throws Exception {
        getAsDOM("wfs?request=GetFeature&typename=gsml:MappedFeature");
    }

}
