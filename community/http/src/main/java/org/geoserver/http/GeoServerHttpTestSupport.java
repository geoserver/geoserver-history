package org.geoserver.http;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import junit.framework.TestCase;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.PostMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

/**
 * Base class for http tests.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GeoServerHttpTestSupport extends TestCase {

	static protected Logger logger = Logger.getLogger( "org.geoserver.test" );
	
	/** test fixture properties **/
	PropertyResourceBundle properties;
	
	/**
	 * Override to disable tests if no server is online.
	 */
	public int countTestCases() {
		if ( isOffline() )
			return 0;
		
		return super.countTestCases();
	}
	
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
		"/" + getContext() + "/";
	}
	
	protected boolean isOffline() {
		try {
			WebConversation conversation = new WebConversation();
	        WebRequest request = 
	        	new GetMethodWebRequest(getBaseUrl()+"wfs?request=getCapabilities");
	        
	        conversation.getResponse( request );
		}
		catch(Exception e) {
			return true;
		} 
		
		return false;
		
	}
	
	protected WebResponse get( String path ) throws Exception {
		WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest( getBaseUrl() + path );
        
        return conversation.getResponse( request );
	}
	
	protected WebResponse post( String path, String xml ) throws Exception {
		InputStream input = new ByteArrayInputStream( xml.getBytes() );
		WebConversation conversation = new WebConversation();
		
	    PostMethodWebRequest request = 
	    		new PostMethodWebRequest(getBaseUrl()+ path, input, "text/xml" );
	    
	    return conversation.getResponse( request );
	}
	
	protected BufferedReader reader( WebResponse response ) throws Exception {
		return new BufferedReader( new InputStreamReader( response.getInputStream() ) );
	}
	
	protected Document dom( WebResponse response ) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		factory.setNamespaceAware( true );
		factory.setValidating( true );
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse( response.getInputStream() );
	}

	protected void print( WebResponse response, PrintStream stream ) throws Exception {
		BufferedReader reader = reader( response );
		String line = null;
		while( ( line = reader.readLine() ) != null ) {
			stream.println( line );
		}
		
		reader.close();
	}
}
