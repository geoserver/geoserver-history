package org.geoserver.web.test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.geoserver.data.test.MockGeoServerDataDirectory;
import org.geoserver.http.test.HttpApplicationContext;
import org.geoserver.http.util.ResponseUtils;
import org.geoserver.ows.http.OWSDispatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.w3c.dom.Document;

import com.mockobjects.servlet.MockHttpServletRequest;
import com.mockobjects.servlet.MockHttpServletResponse;
import com.mockobjects.servlet.MockServletContext;
import com.mockobjects.servlet.MockServletInputStream;
import com.mockobjects.servlet.MockServletOutputStream;

/**
 * An abstract 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class GeoServerHttpTestSupport extends TestCase {

	/**
	 * Mock Data directory instance
	 */
	MockGeoServerDataDirectory data;
	/**
	 * Mock GeoServer instance
	 */
	 MockGeoServer geoServer;
	
	/**
	 * Internal flag to initialize application context.
	 */
	boolean initialize;
	
	/**
	 * Configures the mock GeoServer by registering beans from {@link HttpApplicationContext}. 
	 * <p>
	 * Subclasses should <b>extend</b> ( not override ), this method and register additional 
	 * application contexts.
	 * </p>
	 */
	protected final void setUp() throws Exception {
		data = new MockGeoServerDataDirectory();
		data.setUp();	
		
		geoServer = new MockGeoServer();
		geoServer.getApplicationContext().setServletContext( new MockServletContext() );
		geoServer.loadBeanDefinitions( HttpApplicationContext.getBeanDefinitions() );
	
		System.getProperties().put( 
			"GEOSERVER_DATA_DIR", data.getDataDirectoryRoot().getAbsolutePath() 
		);
		
		initialize = true;
	
		setUpInternal();
	}
	
	/**
     * Setup hook called by {@link #setUp()}. 
     * <p>
     * Subclasses should override this method if they need to initializatoin,
     * the default implementation does nothing.
     * </p>
     * @throws Exception
     */
	protected void setUpInternal() throws Exception {
	}
	
	protected void tearDown() throws Exception {
		
		data.tearDown();
		
		initialize = false;

		tearDownInternal();
		
	}
	
	/**
     * Tears down teh data directory and application context.
     * <p>
	 * This method is marked as final, it calls {@link #setUpInternal()}, any 
	 * initialization should be done there.
	 * </p>
     */
	protected final void tearDownInternal() throws Exception {
	}
	
	/**
	 * Log which controls response logging, default is false, override and return 
	 * true to log.
	 */
	protected boolean isLogging() {
		return false;
	}
	/**
	 * @return The mock geoserver instance.
	 */
	protected MockGeoServer getGeoServer() {
		return geoServer;
	}
	
	MockHttpServletResponse dispatch( MockHttpServletRequest request ) throws Exception {
		
		
		//create the response
		MockHttpServletResponse response = new MockHttpServletResponse() {
			public void setCharacterEncoding( String encoding ) {
				
			}
		};
		
		response.setupOutputStream( new MockServletOutputStream() );
		
		//look up the handler
		OWSDispatcher dispatcher = 
			(OWSDispatcher) geoServer.getApplicationContext().getBean( "dispatcher" ); 
		dispatcher.setApplicationContext( getGeoServer().getApplicationContext() );
		
		//excute the pre handler step
		Collection interceptors = 
			getGeoServer().getApplicationContext().getBeansOfType( HandlerInterceptor.class ).values();
		for ( Iterator i = interceptors.iterator(); i.hasNext(); ) {
			HandlerInterceptor interceptor = (HandlerInterceptor) i.next();
			interceptor.preHandle( request, response, dispatcher );
		}
		
		//execute 
		dispatcher.handleRequest( request, response );
		
		//execute the post handler step
		for ( Iterator i = interceptors.iterator(); i.hasNext(); ) {
			HandlerInterceptor interceptor = (HandlerInterceptor) i.next();
			interceptor.postHandle( request, response, dispatcher, null );
		}
		
		return response;
		
//		MockServletContext context = new MockServletContext() {
//			public InputStream getResourceAsStream(String string) {
//				if ( "/WEB-INF/dispatcher-servlet.xml".equals( string ) ) {
//					try {
//						return HttpApplicationContext.getDispatcherServletDefinitions();
//					} 
//					catch (IOException e) {
//						throw new RuntimeException( e );
//					}
//				}
//				
//				return super.getResourceAsStream( string );
//			}
//		};
//		context.setupGetAttribute( 
//			WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, geoServer.getApplicationContext() 
//		);
//		
//		MockServletConfig servletConfig = new MockServletConfig() {
//			public String getServletName() {
//				return "dispatcher";
//			}
//		};
//		
//		servletConfig.setServletContext( context );
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
	 *
	 * @param path The porition of the request after hte context, 
	 * 	example: 'wms?request=GetMap&version=1.1.1&..."
	 * 
	 * @return An input stream which is the result of the request.
	 * 
	 * @throws Exception
	 */
	protected InputStream get( String path ) throws Exception {
		MockHttpServletRequest request = request( path ); 
		request.setupGetMethod( "GET" );
		
		MockHttpServletResponse response = dispatch( request );
        return new ByteArrayInputStream( response.getOutputStreamContents().getBytes() );
	}
	
	/**
	 * Executes an ows request using the POST method with key value pairs 
	 * form encoded. 
	 *
	 * @param path The porition of the request after hte context, 
	 * 	example: 'wms?request=GetMap&version=1.1.1&..."
	 * 
	 * @return An input stream which is the result of the request.
	 * 
	 * @throws Exception
	 */
	protected InputStream post( String path ) throws Exception {
		MockHttpServletRequest request = request( path ); 
		request.setupGetMethod( "POST" );
		request.setupGetContentType( "application/x-www-form-urlencoded" );
		
		MockHttpServletResponse response = dispatch( request );
        return new ByteArrayInputStream( response.getOutputStreamContents().getBytes() );
	}
	
	/**
	 * Executes an ows request using the POST method with key value pairs 
	 * form encoded, returning the result as a dom.
	 *
	 * @param path The porition of the request after hte context, 
	 * 	example: 'wms?request=GetMap&version=1.1.1&..."
	 * 
	 * @return An input stream which is the result of the request.
	 * 
	 * @throws Exception
	 */
	protected Document postAsDOM( String path ) throws Exception {
		return dom( post( path ) );
	}
	
	protected MockHttpServletRequest request( String path ) {
		if ( initialize ) {
			geoServer.getApplicationContext().refresh();
			initialize = false;
		}
			
		MockHttpServletRequest request = new MockHttpServletRequest() {
			String encoding = "UTF-8";
			
			public int getServerPort() {
				return 8080;
			}
			
			public String getCharacterEncoding() {
				return encoding;
			}
			
			public void setCharacterEncoding(String encoding) {
				this.encoding = encoding;
			}
			
		};
		
		request.setupScheme( "http" );
		request.setupServerName( "localhost" );
		request.setupGetContextPath( "/geoserver" );
		request.setupGetRequestURI( 
			ResponseUtils.stripQueryString( ResponseUtils.appendPath( "/geoserver/", path ) ) 
		);
		request.setupQueryString( ResponseUtils.stripQueryString( path ));
		request.setupGetRemoteAddr( "127.0.0.1" );
		request.setupGetServletPath( path );
		request.setupGetParameterMap( kvp( path ) );
		request.setupGetUserPrincipal( null );
		request.setSession( null );
		
		request.setupGetInputStream( new MockServletInputStream() );	
		
		return request;
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
		MockHttpServletRequest request = request( path );
		request.setupGetMethod( "POST" );
		request.setupGetContentType( "application/xml" );
		
		((MockServletInputStream) request.getInputStream()).setupRead( xml.getBytes() );
		
		MockHttpServletResponse response = dispatch( request );
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
		//factory.setValidating( true );
		
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document dom = builder.parse( input );
		
		if ( isLogging() ) {
			TransformerFactory txFactory = TransformerFactory.newInstance();
			
			Transformer tx = txFactory.newTransformer();
			tx.setOutputProperty( OutputKeys.INDENT, "yes" );
			
			tx.transform( new DOMSource( dom ), new StreamResult( System.out ) );
		}
		
		return dom;
	}
}
