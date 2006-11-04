package org.geoserver.data.test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides a test application context containing bean definitions of data support 
 * classes. 
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class DataApplicationContext {

	public static InputStream getApplicationContext( ) throws IOException {
		return DataApplicationContext.class.getResourceAsStream( "test-applicationContext.xml" );
	}
}
