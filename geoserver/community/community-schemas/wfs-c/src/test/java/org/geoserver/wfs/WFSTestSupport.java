/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
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
public abstract class WFSTestSupport extends GeoServerTestSupport {
    
    /**
     * Should disabled tests be skipped?
     */
    private static final boolean SKIP_DISABLED = true;

    /**
     * Return true if disabled tests are to be skipped
     * 
     * <p>
     * 
     * This is a centralised place to enable tests artificially disabled by
     * inserting an early return using the idiom:
     * 
     * <pre>
     * if (skipDisabled()) {
     *     return; // FIXME: this test is disabled by default
     * }
     * </pre>
     * 
     * Enable or disabled these tests by changing SKIP_DISABLED.
     * 
     * @return true if disabled tests should be skipped
     */
    protected static boolean skipDisabled() {
        if (SKIP_DISABLED) {
            LOGGER.warning("Skipping disabled test");
        }
        return SKIP_DISABLED;
    }

    /**
     * @return The global wfs instance from the application context.
     */
    protected WFS getWFS() {
        return (WFS) applicationContext.getBean("wfs");
    }
            
}
