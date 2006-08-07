package org.geoserver.http;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

import junit.framework.TestCase;

public class GeoServerHttpTestSupport extends TestCase {

	static protected Logger logger = Logger.getLogger( "org.geoserver.test" );
	
	/** test fixture properties **/
	PropertyResourceBundle properties;
	
	protected void setUp() throws Exception {
		try {
			properties = new PropertyResourceBundle(
				new FileInputStream("httptest.properties")
			);
		} 
		catch (FileNotFoundException e) {
			//ignore
			logger.warning( "Could not find httptest.properties, using defaults" );
		} 
	}
	
	public String getProtocol() {
		return properties != null && properties.getString("protocol") != null ?
			properties.getString("protocol") : "http";
	}
	
	public String getServer() {
		return properties != null && properties.getString("server") != null ? 
			properties.getString("server") : "localhost";
	}
	
	public String getPort() {
		return properties != null && properties.getString("port") != null ? 
			properties.getString("port") : "8080";
	}
	
	public String getContext() {
		return properties != null && properties.getString("context") != null ? 
			properties.getString("context") : "geoserver";
	}
	
	public String getBaseUrl() {
		return getProtocol() + "://" + getServer() + ":" + getPort() + 
		"/" + getContext();
	}
	
	protected boolean isOffline() {
		try {
			WebConversation conversation = new WebConversation();
	        WebRequest request = 
	        	new GetMethodWebRequest(getBaseUrl()+"/wfs?request=getCapabilities");
	        
	        conversation.getResponse( request );
		}
		catch(Exception e) {
			return true;
		} 
		
		return false;
		
	}
}
