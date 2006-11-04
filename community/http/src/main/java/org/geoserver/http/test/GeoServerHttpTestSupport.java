package org.geoserver.http.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.http.OWSDispatcher;
import org.geoserver.test.MockGeoServer;
import org.w3c.dom.Document;

import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockServletInputStream;
import com.mockobjects.servlet.MockServletOutputStream;

import junit.framework.TestCase;

/**
 * An abstract 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class GeoServerHttpTestSupport extends TestCase {

	/**
	 * Mock GeoServer instance
	 */
	MockGeoServer geoServer;
	
	/**
	 * Configures the mock GeoServer by registering beans from {@link HttpApplicationContext}. 
	 * <p>
	 * Subclasses should <b>extend</b> ( not override ), this method and register additional 
	 * application contexts.
	 * </p>
	 */
	protected void setUp() throws Exception {
		geoServer = new MockGeoServer();
		geoServer.loadBeanDefinitions( HttpApplicationContext.getApplicationContext() );
	}
	
	OWSDispatcher dispatcher() {
		return (OWSDispatcher) geoServer.getApplicationContext().getBean( "dispatcher" );
	}
	
	Map kvp( String path ) {
		int index = path.indexOf( '?' );
		if ( index == -1 ) {
			return Collections.EMPTY_MAP;
		}
		
		Map kvp = new HashMap();
		
		String queryString = path.substring( index + 1 );
		StringTokenizer st = new StringTokenizer( queryString, "&" );
		while( st.hasMoreTokens() ) {
			String token = st.nextToken();
			String[] keyValuePair = token.split( "=" );
			kvp.put( keyValuePair[ 0 ], keyValuePair[ 1] );
		}
		
		return kvp;
	}
	
	/**
	 * Executes an ows request using the GET method.
	 * <p>
	 * 
	 * </p>
	 * @param path The porition of the request after hte context, 
	 * 	example: 'wms?request=GetMap&version=1.1.1&..."
	 * 
	 * @return An input stream which is the result of the request.
	 * 
	 * @throws Exception
	 */
	protected InputStream get( String path ) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setupGetMethod( "GET" );
		request.setupGetContextPath( "/geoserver" );
		request.setupGetRequestURI( ResponseUtils.appendPath( "/geoserver/", path ) );
		request.setupGetParameterMap( kvp( path ) );
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setupOutputStream( new MockServletOutputStream() );
		        
        dispatcher().handleRequest( request, response );
        return new ByteArrayInputStream( response.getOutputStreamContents().getBytes() );
	}
	
	/**
	 * Executes an ows request using the POST method.
	 * <p>
	 * 
	 * </p>
	 * @param path The porition of the request after the context ( no query string ), 
	 * 	example: 'wms'. 
	 * 
	 * @return An input stream which is the result of the request.
	 * 
	 * @throws Exception
	 */
	protected InputStream post( String path , String xml ) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setupGetMethod( "POST" );
		request.setupGetContextPath( "/geoserver" );
		request.setupGetRequestURI( "/geoserver/" + path );
		
		MockServletInputStream input = new MockServletInputStream();
		input.setupRead( xml.getBytes() );
		request.setupGetInputStream( input );
		
		MockHttpServletResponse response = new MockHttpServletResponse();
		response.setupOutputStream( new MockServletOutputStream() );
		        
        dispatcher().handleRequest( request, response );
        return new ByteArrayInputStream( response.getOutputStreamContents().getBytes() );
	}
	
	/**
	 * Executes an ows request using the GET method and returns the result as an 
	 * xml document.
	 * 
	 * @param path The porition of the request after hte context, 
	 * 	example: 'wms?request=GetMap&version=1.1.1&..."
	 * 
	 * @return A result of the request parsed into a dom.
	 * 
	 * @throws Exception
	 */
	protected Document getAsDOM( String path ) throws Exception {
		return dom( get( path ) );
	}
	
	/**
	 * Executes an ows request using the POST method and returns the result as an
	 * xml document.
	 * <p>
	 * 
	 * </p>
	 * @param path The porition of the request after the context ( no query string ), 
	 * 	example: 'wms'. 
	 * 
	 * @return An input stream which is the result of the request.
	 * 
	 * @throws Exception
	 */
	protected Document postAsDOM( String path, String xml ) throws Exception {
		return dom( post( path, xml ) );
	}
	
	Document dom( InputStream input ) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
		factory.setNamespaceAware( true );
		factory.setValidating( true );
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse( input );
	}
}
