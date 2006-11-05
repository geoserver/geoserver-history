package org.geoserver.web.test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Provides a test application context containing bean definitions of web support 
 * classes. 
 * <p>
 * <pre>
 * &lt;beans>
 *
 *	  &lt;!-- GeoServer data directory initializer -->
 *	  &lt;bean id="dataDirectoryInitializer" class="org.geoserver.web.GeoServerDataDirectoryInitializer"/>
 *	
 *  &lt;/beans>
 * </pre>
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WebApplicationContext {

	public static InputStream getBeanDefinitions() throws IOException {
		return WebApplicationContext.class.getResourceAsStream( "test-applicationContext.xml" );
	}
}
