/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.test;

import org.geoserver.data.test.MockData;
import org.geoserver.test.ows.KvpRequestReaderTestSupport;
import org.geoserver.wcs.WCSInfo;

/**
 * Base support class for wcs tests.
 * 
 * @author Andrea Aime, TOPP
 * 
 */
public abstract class CoverageTestSupport extends KvpRequestReaderTestSupport {
    protected static final String BASEPATH = "wcs";

    /**
     * @return The global wcs instance from the application context.
     */
    protected WCSInfo getWCS() {
        WCSInfo wcs = getGeoServer().getService(WCSInfo.class);
        return wcs;
    }

    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        dataDirectory.addWellKnownCoverageTypes();
    }
}
