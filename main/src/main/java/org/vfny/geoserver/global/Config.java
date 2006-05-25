package org.vfny.geoserver.global;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.vfny.geoserver.global.xml.XMLConfigReader;

public class Config implements ApplicationContextAware {

	WebApplicationContext context;
	
	XMLConfigReader reader;
	
	public XMLConfigReader getXMLReader() throws ConfigurationException {
		return reader;
	}
	
	public File dataDirectory() {
		ServletContext sc = this.context.getServletContext();
		return GeoserverDataDirectory.getGeoserverDataDirectory(sc);
	}
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = (WebApplicationContext) context;
		
		
		ServletContext sc = this.context.getServletContext();
		
		try {
			reader = new XMLConfigReader(dataDirectory(),sc);
		} 
		catch (ConfigurationException e) {
			String msg = "Error creating xml config reader";
			throw new BeanInitializationException(msg,e);
		}
		
	}
	
}
