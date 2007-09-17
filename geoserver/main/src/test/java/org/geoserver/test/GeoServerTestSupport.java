/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.test;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;
import com.mockrunner.mock.web.MockServletOutputStream;

import junit.framework.TestCase;
import org.geoserver.data.test.MockData;
import org.geoserver.ows.Dispatcher;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.data.FeatureSource;
import org.springframework.web.servlet.HandlerInterceptor;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 * Base test class for GeoServer unit tests.
 * <p>
 * Deriving from this test class provides the test case with preconfigured
 * geoserver and catalog objects.
 * </p>
 * <p>
 * This test case provides a spring application context which loads the
 * application contexts from all modules on the classpath.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class GeoServerTestSupport extends TestCase {
    /**
     * mock GeoServer data directory
     */
    protected MockData dataDirectory;

    /**
     * Application context
     */
    protected GeoServerTestApplicationContext applicationContext;

    /**
     * If subclasses overide they *must* call super.setUp() first.
     */
    protected void setUp() throws Exception {
        super.setUp();

        //set up the data directory
        dataDirectory = new MockData();
        dataDirectory.setUp();

        //copy the service configuration to the data directory
        dataDirectory.copyTo(GeoServerTestSupport.class.getResourceAsStream("services.xml"),
            "services.xml");

        //set up a mock servlet context
        MockServletContext servletContext = new MockServletContext();
        servletContext.setInitParameter("GEOSERVER_DATA_DIR",
            dataDirectory.getDataDirectoryRoot().getAbsolutePath());

        applicationContext = new GeoServerTestApplicationContext(new String[] {
                    "classpath*:/applicationContext.xml",
                    "classpath*:/applicationSecurityContext.xml"
                }, servletContext);

        applicationContext.refresh();
    }

    /**
     * If subclasses overide they *must* call super.tearDown() first.
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        //kill the context
        applicationContext.destroy();

        //kill the data directory
        dataDirectory.tearDown();
        GeoserverDataDirectory.destroy();
    }

    /**
     * Accessor for global catalog instance from the test application context.
     */
    protected Data getCatalog() {
        return (Data) applicationContext.getBean("data");
    }

    /**
     * Accessor for global geoserver instance from the test application context.
     */
    protected GeoServer getGeoServer() {
        return (GeoServer) applicationContext.getBean("geoServer");
    }
    
    /**
     * Accessor for global resource loader instance from the test application context.
     */
    protected GeoServerResourceLoader getResourceLoader() {
        return (GeoServerResourceLoader) applicationContext.getBean( "resourceLoader" );
    }

    /**
     * Loads a feature source from the catalog.
     *
     * @param typeName The qualified type name of the feature source.
     */
    protected FeatureSource getFeatureSource(QName typeName)
        throws IOException {
        return getCatalog().getFeatureSource(typeName.getPrefix(), typeName.getLocalPart());
    }

    /**
     * Convenience method for subclasses to create mock http servlet requests.
     * <p>
     * Examples of using this method are:
     * <pre>
     * <code>
     *   createRequest( "wfs?request=GetCapabilities" );  //get
     *   createRequest( "wfs" ); //post
     * </code>
     * </pre>
     * </p>
     * @param path The path for the request and optional the query string.
     * @return
     */
    protected MockHttpServletRequest createRequest(String path) {
        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setScheme("http");
        request.setServerName("localhost");
        request.setContextPath("/geoserver");
        request.setRequestURI(ResponseUtils.stripQueryString(ResponseUtils.appendPath(
                    "/geoserver/", path)));
        request.setQueryString(ResponseUtils.stripQueryString(path));
        request.setRemoteAddr("127.0.0.1");
        request.setServletPath(path);
        kvp(request, path);

        MockHttpSession session = new MockHttpSession();
        session.setupServletContext(new MockServletContext());
        request.setSession(session);

        request.setUserPrincipal(null);

        return request;
    }

    /**
     * Executes an ows request using the GET method.
     *
     * @param path The porition of the request after hte context, 
     *      example: 'wms?request=GetMap&version=1.1.1&..."
     * 
     * @return An input stream which is the result of the request.
     * 
     * @throws Exception
     */
    protected InputStream get( String path ) throws Exception {
        MockHttpServletResponse response = getAsServletResponse(path);
        return new ByteArrayInputStream( response.getOutputStreamContent().getBytes() );
    }
    
    /**
     * Executes an ows request using the GET method.
     *
     * @param path The porition of the request after hte context, 
     *      example: 'wms?request=GetMap&version=1.1.1&..."
     * 
     * @return the mock servlet response
     * 
     * @throws Exception
     */
    protected MockHttpServletResponse getAsServletResponse( String path ) throws Exception {
        MockHttpServletRequest request = createRequest( path ); 
        request.setMethod( "GET" );
        request.setBodyContent(new byte[]{});
        
        return dispatch( request );
    }
        
    /**
     * Executes an ows request using the POST method with key value pairs 
     * form encoded. 
     *
     * @param path The porition of the request after hte context, 
     *      example: 'wms?request=GetMap&version=1.1.1&..."
     * 
     * @return An input stream which is the result of the request.
     * 
     * @throws Exception
     */
    protected InputStream post( String path ) throws Exception {
        MockHttpServletRequest request = createRequest( path ); 
        request.setMethod( "POST" );
        request.setContentType( "application/x-www-form-urlencoded" );
        
        MockHttpServletResponse response = dispatch( request );
        return new ByteArrayInputStream( response.getOutputStreamContent().getBytes() );
    }
    
    /**
     * Executes an ows request using the POST method.
     * <p>
     * 
     * </p>
     * @param path The porition of the request after the context ( no query string ), 
     *      example: 'wms'. 
     * 
     * @return An input stream which is the result of the request.
     * 
     * @throws Exception
     */
    protected InputStream post( String path , String xml ) throws Exception {
        MockHttpServletRequest request = createRequest( path );
        request.setMethod( "POST" );
        request.setContentType( "application/xml" );
        request.setBodyContent(xml);
        
        MockHttpServletResponse response = dispatch( request );
        return new ByteArrayInputStream( response.getOutputStreamContent().getBytes() );
    }
    
    /**
     * Executes an ows request using the GET method and returns the result as an 
     * xml document.
     * 
     * @param path The porition of the request after hte context, 
     *      example: 'wms?request=GetMap&version=1.1.1&..."
     * 
     * @return A result of the request parsed into a dom.
     * 
     * @throws Exception
     */
    protected Document getAsDOM( String path ) throws Exception {
        return dom( get( path ) );
    }
    
    /**
     * Executes an ows request using the POST method with key value pairs 
     * form encoded, returning the result as a dom.
     *
     * @param path The porition of the request after hte context, 
     *      example: 'wms?request=GetMap&version=1.1.1&..."
     * 
     * @return An input stream which is the result of the request.
     * 
     * @throws Exception
     */
    protected Document postAsDOM( String path ) throws Exception {
        return dom( post( path ) );
    }
    
    /**
     * Executes an ows request using the POST method and returns the result as an
     * xml document.
     * <p>
     * 
     * </p>
     * @param path The porition of the request after the context ( no query string ), 
     *      example: 'wms'. 
     * 
     * @return An input stream which is the result of the request.
     * 
     * @throws Exception
     */
    protected Document postAsDOM( String path, String xml ) throws Exception {
            return dom( post( path, xml ) );
    }
    
    protected String getAsString(String path) throws Exception {
        return string(get(path));
    }
    
    /**
     * Parses a stream into a dom.
     */
    protected Document dom( InputStream input ) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
        factory.setNamespaceAware( true );
        //factory.setValidating( true );
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse( input );

        return dom;
    }
    
    /**
     * Parses a stream into a String
     */
    protected String string(InputStream input) throws Exception {
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        char[] buf = new char[8192];
        try {
            reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while((line = reader.readLine()) != null)
                sb.append(line);
        } finally {
            if(reader != null)
                reader.close();
        }
        return sb.toString();
    }
    
    /**
     * Utility method to print out a dom.
     */
    protected void print( Document dom ) throws Exception {
        TransformerFactory txFactory = TransformerFactory.newInstance();
      
        Transformer tx = txFactory.newTransformer();
        tx.setOutputProperty( OutputKeys.INDENT, "yes" );
          
        tx.transform( new DOMSource( dom ), new StreamResult( System.out ) );
    }

    /**
     * Convenience method for element.getElementsByTagName() to return the 
     * first element in the resulting node list.
     */
    protected Element getFirstElementByTagName( Element element, String name ) {
        NodeList elements = element.getElementsByTagName(name);
        if ( elements.getLength() > 0 ) {
            return (Element) elements.item(0);
        }
        
        return null;
    }
    
    /**
     * Convenience method for element.getElementsByTagName() to return the 
     * first element in the resulting node list.
     */
    protected Element getFirstElementByTagName( Document dom, String name ) {
        return getFirstElementByTagName( dom.getDocumentElement(), name );
    }
    
    /**
     * Sets up a template in a feature type directory.
     * 
     * @param featureTypeName The name of the feature type.
     * @param template The name of the template.
     * @param body The content of the template.
     * 
     * @throws IOException
     */
    protected void setupTemplate(QName featureTypeName,String template,String body)
        throws IOException {
        
        dataDirectory.copyToFeatureTypeDirectory( new ByteArrayInputStream(body.getBytes()), featureTypeName, template );
    }
    
    /*
     * Helper method to create the kvp params from the query string.
     */
    private void kvp(MockHttpServletRequest request, String path) {
        int index = path.indexOf('?');

        if (index == -1) {
            return;
        }

        String queryString = path.substring(index + 1);
        StringTokenizer st = new StringTokenizer(queryString, "&");

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String[] keyValuePair = token.split("=");
            request.setupAddParameter(keyValuePair[0], keyValuePair.length > 1 ?  keyValuePair[1]: "");
        }
    }
    
    /*
     * Helper method for dispatching an executing an ows request. 
     */
    private MockHttpServletResponse dispatch( MockHttpServletRequest request ) throws Exception {
        //create the response
        //final MockServletOutputStream output = new MockServletOutputStream();
        MockHttpServletResponse response = new MockHttpServletResponse() {
            public void setCharacterEncoding( String encoding ) {
                    
            }
//            public ServletOutputStream getOutputStream() throws IOException {
//                return output;
//            }
        };
        
        //look up the handler
        Dispatcher dispatcher = 
                (Dispatcher) applicationContext.getBean( "dispatcher" ); 
        //dispatcher.setApplicationContext( getGeoServer().getApplicationContext() );
        
        //excute the pre handler step
        Collection interceptors = 
            GeoServerExtensions.extensions(HandlerInterceptor.class, applicationContext );
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
    }
}
