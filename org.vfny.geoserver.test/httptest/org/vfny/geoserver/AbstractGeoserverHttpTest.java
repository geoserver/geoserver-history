package org.vfny.geoserver;

import java.io.FileInputStream;
import java.util.PropertyResourceBundle;

import junit.framework.TestCase;

public abstract class AbstractGeoserverHttpTest extends TestCase {

	/** test fixture properties **/
	PropertyResourceBundle properties;
	
	protected void setUp() throws Exception {
		properties = new PropertyResourceBundle(
    		new FileInputStream("httptest.properties")
		);
	}
	
	public String getProtocol() {
		return properties.getString("protocol") != null ?
			properties.getString("protocol") : "http";
	}
	
	public String getServer() {
		return properties.getString("server") != null ? 
			properties.getString("server") : "localhost";
	}
	
	public String getPort() {
		return properties.getString("port") != null ? 
			properties.getString("port") : "8080";
	}
	
	public String getContext() {
		return properties.getString("context") != null ? 
			properties.getString("context") : "geoserver";
	}
	
	public String getBaseUrl() {
		return getProtocol() + "://" + getServer() + ":" + getPort() + 
		"/" + getContext();
	}
}
