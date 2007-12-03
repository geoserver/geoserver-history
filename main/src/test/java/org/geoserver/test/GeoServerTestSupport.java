/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.apache.log4j.LogManager;
import org.geoserver.data.test.MockData;
import org.geoserver.ows.Dispatcher;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.data.FeatureSource;
import org.geotools.util.logging.Log4JLoggerFactory;
import org.geotools.util.logging.Logging;
import org.springframework.web.servlet.HandlerInterceptor;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.mockrunner.mock.web.MockHttpServletRequest;
import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpSession;
import com.mockrunner.mock.web.MockServletContext;


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
     * Common logger for test cases
     */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.test"); 
    
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

        // create the data directory
        dataDirectory = new MockData();
        populateDataDirectory(dataDirectory);
        dataDirectory.setUpCatalog();
        dataDirectory.copyTo(getServicesFile().openStream(), "services.xml");
                 
        // setup quiet logging (we need to to this here because Data
        // is loaded before GoeServer has a chance to setup logging for good)
        try {
            Logging.ALL.setLoggerFactory(Log4JLoggerFactory.getInstance());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not configure log4j logging redirection", e);
        }
        GeoServer.suppressLoggingConfiguration();
        setupLogging(getClass().getResourceAsStream(getDefaultLogConfiguration()));

        // set up a mock servlet context
        MockServletContext servletContext = new MockServletContext();
        servletContext.setInitParameter("GEOSERVER_DATA_DIR",
            dataDirectory.getDataDirectoryRoot().getAbsolutePath());

        applicationContext = new GeoServerTestApplicationContext(new String[] {
                    "classpath*:/applicationContext.xml",
                    "classpath*:/applicationSecurityContext.xml"
                }, servletContext);

        applicationContext.refresh();
    }

	protected String getDefaultLogConfiguration() {
		return "/TEST_LOGGING.properties";
	}
    
    protected void setupLogging(InputStream loggingConfigStream) throws Exception {
    	GeoServer.configureGeoServerLogging(loggingConfigStream, false, true, null);
    }

    /**
     * Returns a default services.xml file with WMS, WFS and WCS enabled. Subclasses may
     * need to override this in order to test extra services or specific configurations
     * @return
     */
    protected URL getServicesFile() {
        return GeoServerTestSupport.class.getResource("services.xml");
    }

    /** 
     * Adds the desired type and coverages to the data directory. This method adds all well known
     * data types, subclasses may add their extra ones or decide to avoid the standar ones and 
     * build a custom list calling {@link MockData#addPropertiesType(QName, java.net.URL, java.net.URL)}
     * and {@link MockData#addCoverage(QName, InputStream, String)}
     * @throws IOException
     */
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        //set up the data directory
        dataDirectory.addWellKnownTypes(MockData.TYPENAMES);
    }

    /**
     * If subclasses overide they *must* call super.tearDown() first.
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        //kill the context
        applicationContext.destroy();
        applicationContext = null;
        
        //kill the data directory
        dataDirectory.tearDown();
        GeoserverDataDirectory.destroy();
        dataDirectory = null;
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
     * Given a qualified layer name returns a string in the form "prefix:localPart" if prefix
     * is available, "localPart" if prefix is null
     * @param layerName
     * @return
     */
    public String layerId(QName layerName) {
        if(layerName.getPrefix() != null)
            return layerName.getPrefix() + ":" + layerName.getLocalPart();
        else
            return layerName.getLocalPart();
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
     * @param path The portion of the request after the context, 
     *      example: 'wms?request=GetMap&version=1.1.1&..."
     * 
     * @return A result of the request parsed into a dom.
     * 
     * @throws Exception
     */
    protected Document getAsDOM( String path ) throws Exception {
        return getAsDOM(path, null);
    }
    
    /**
     * Executes an ows request using the GET method and returns the result as an 
     * xml document.
     * 
     * @param path The portion of the request after the context, 
     *      example: 'wms?request=GetMap&version=1.1.1&..."
     * @param the list of validation errors encountered during document parsing (validation
     *        will be activated only if this list is non null)
     * 
     * @return A result of the request parsed into a dom.
     * 
     * @throws Exception
     */
    protected Document getAsDOM(final String path, List<Exception> validationErrors)
            throws Exception {
        return dom(get(path), validationErrors);
    }

    
    /**
     * Executes an ows request using the POST method with key value pairs 
     * form encoded, returning the result as a dom.
     *
     * @param path The porition of the request after hte context, 
     *      example: 'wms?request=GetMap&version=1.1.1&..."
     * @param the list of validation errors encountered during document parsing (validation
     *        will be activated only if this list is non null)     
     * 
     * @return An input stream which is the result of the request.
     * 
     * @throws Exception
     */
    protected Document postAsDOM( String path ) throws Exception {
        return postAsDOM(path, (List<Exception>) null);
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
    protected Document postAsDOM( String path, List<Exception> validationErrors ) throws Exception {
        return dom( post( path ), validationErrors );
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
            return postAsDOM(path, xml, null);
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
    protected Document postAsDOM( String path, String xml, List<Exception> validationErrors ) throws Exception {
            return dom( post( path, xml ), validationErrors );
    }
    
    protected String getAsString(String path) throws Exception {
        return string(get(path));
    }
    
    /**
     * Parses a stream into a dom.
     */
    protected Document dom( InputStream input ) throws Exception {
        return dom(input, null);
    }
    
    /**
     * Parses a stream into a dom. If the validationErrors collection is provided (not null) then
     * schema validation is activated and the validation errors will be added to it.
     */
    private Document dom(InputStream is, final List<Exception> validationErrors)
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        if (validationErrors != null) {
            factory.setValidating(true);
            factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage",
                    "http://www.w3.org/2001/XMLSchema");
        }

        DocumentBuilder builder = factory.newDocumentBuilder();
        if (validationErrors != null) {
            builder.setErrorHandler(new ErrorHandler() {

                public void warning(SAXParseException exception) throws SAXException {
                    System.out.println(exception.getMessage());
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                    validationErrors.add(exception);
                }

                public void error(SAXParseException exception) throws SAXException {
                    validationErrors.add(exception);
                }

            });
        }
        return builder.parse(is);
    }

    /**
     * Given a list of validation exceptions, checks it's empty, or fails the test with a list
     * of the validation errors instead
     * @param validationErrors
     */
    protected void checkValidationErrors(List<Exception> validationErrors) {
        if (validationErrors != null && validationErrors.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Exception ve : validationErrors) {
                sb.append(ve.getMessage()).append("\n");
            }
            fail(sb.toString());
        }
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
            
            //check for any special characters
            if ( keyValuePair.length > 1 ) {
                //replace any equals or & characters
                keyValuePair[1] = keyValuePair[1].replaceAll( "%3D", "=" );
                keyValuePair[1] = keyValuePair[1].replaceAll( "%3d", "=" );
                keyValuePair[1] = keyValuePair[1].replaceAll( "%23", "&" );
            }
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
