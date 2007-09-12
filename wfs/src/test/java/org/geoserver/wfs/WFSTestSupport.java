/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wfs;

import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.WfsFactory;

import org.geoserver.platform.Operation;
import org.geoserver.platform.Service;
import org.geoserver.test.GeoServerTestSupport;


/**
 * Base support class for wfs tests.
 * <p>
 * Deriving from this test class provides the test case with preconfigured
 * geoserver and wfs objects.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WFSTestSupport extends GeoServerTestSupport {
    /**
     * @return The global wfs instance from the application context.
     */
    protected WFS getWFS() {
        return (WFS) applicationContext.getBean("wfs");
    }
}
