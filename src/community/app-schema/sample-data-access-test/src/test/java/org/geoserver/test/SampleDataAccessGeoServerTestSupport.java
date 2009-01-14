/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.util.logging.Logger;

import org.geoserver.data.test.TestData;
import org.geotools.data.SampleDataAccess;
import org.geotools.util.logging.Logging;

/**
 * Abstract base class for test cases that test integration of {@link SampleDataAccess} with
 * GeoServer.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public abstract class SampleDataAccessGeoServerTestSupport extends GeoServerAbstractTestSupport {

    Logger LOGGER = Logging.getLogger(SampleDataAccessGeoServerTestSupport.class);

    @Override
    protected TestData buildTestData() throws Exception {
        return new SampleDataAccessMockData();
    }

}
