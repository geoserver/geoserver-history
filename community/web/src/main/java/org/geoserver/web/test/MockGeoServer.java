package org.geoserver.web.test;

import java.io.InputStream;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.xml.sax.InputSource;

/**
 * Class used to build a mock geoserver instance.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class MockGeoServer {

	/**
	 * application context used to load beans.
	 */
	GenericWebApplicationContext applicationContext;
	
	public MockGeoServer() {
		applicationContext = new GenericWebApplicationContext();
	}
	
	/**
	 * Returns the application context used by the mock GeoServer.
	 */
	public GenericWebApplicationContext getApplicationContext() {
		return applicationContext;
	}
	
	/**
	 * Loads beans defined in an application context into the 
	 * application context of the mock GeoServer instance.
	 * 
	 * @param beanDefinitionFile An xml file containing bean definitions.
	 */
	public void loadBeanDefinitions( InputStream beanDefinitions ) {
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader( applicationContext );
		reader.loadBeanDefinitions( new InputSource( beanDefinitions ) );
	}
	
}
