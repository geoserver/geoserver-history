/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import org.geoserver.data.test.TestData;
import org.geotools.data.complex.AppSchemaDataAccess;

/**
 * Abstract base class for test cases that test implementation of point geometry from scalar columns in {@link AppSchemaDataAccess} with
 * GeoServer.
 * 
 * @author Rob Atkinson, CSIRO 
 */
public abstract class XYGeomTestSupport extends GeoServerAbstractTestSupport {

    @Override
    protected TestData buildTestData() throws Exception {
        return new XYGeomMockData();
    }

}
