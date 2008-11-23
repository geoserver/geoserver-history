/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.jetty;

import org.geoserver.test.GeoServerTestSupport;

/**
 * Don't do anything special, just gather up the same Spring context as the real
 * Geoserver and make sure it can be loaded without issues
 * 
 * @author Andrea Aime - TOPP
 * 
 */
public class SpringContextTest extends GeoServerTestSupport {
	public void testStartStop() throws Exception {
		// Nothing to do, the base class will setup the spring context during
		// startup
	}
}
