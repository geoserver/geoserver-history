package org.geoserver.test;

import java.io.IOException;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.geoserver.data.test.MockData;
import org.geotools.data.FeatureSource;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;

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
        dataDirectory.copyTo( GeoServerTestSupport.class.getResourceAsStream( "services.xml" ), "services.xml" );
        
        //set up a mock servlet context
        MockServletContext servletContext = new MockServletContext();
        servletContext.setInitParameter( "GEOSERVER_DATA_DIR", dataDirectory.getDataDirectoryRoot().getAbsolutePath() );
       
        applicationContext = 
            new GeoServerTestApplicationContext( "classpath*:/applicationContext.xml", servletContext );
        
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
    }
    
    /**
     * Accessor for global catalog instance from the test application context.
     */
    protected Data getCatalog() {
        return (Data) applicationContext.getBean( "data" );
    }
    
    /**
     * Accessor for global geoserver instance from the test application context.
     */
    protected GeoServer getGeoServer() {
        return (GeoServer) applicationContext.getBean("geoServer");
    }
   
    /**
     * Loads a feature source from the catalog.
     * 
     * @param typeName The qualified type name of the feature source.
     */
    protected FeatureSource getFeatureSource( QName typeName ) throws IOException {
        return getCatalog().getFeatureSource( typeName.getPrefix(), typeName.getLocalPart() );
    }
   
}
