package org.geoserver.test;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.FileSystemResource;
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
	GenericApplicationContext applicationContext;
	
	public MockGeoServer() {
		applicationContext = new GenericApplicationContext();
	}
	
	/**
	 * Returns the application context used by the mock GeoServer.
	 */
	public GenericApplicationContext getApplicationContext() {
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
