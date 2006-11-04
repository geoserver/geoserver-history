package org.geoserver.wfs.test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides a test application context containing bean definitions wfs support 
 * classes.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WfsApplicationContext {

	public static InputStream getApplicationContext() throws IOException {
		return WfsApplicationContext.class.getResourceAsStream( "test-applicationContext.xml" );
	}
}
