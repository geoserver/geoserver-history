package org.geoserver.wfs;

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
        return (WFS) applicationContext.getBean( "wfs" );
    }
}
