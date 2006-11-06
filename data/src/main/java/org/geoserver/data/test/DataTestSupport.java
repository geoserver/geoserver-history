package org.geoserver.data.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geoserver.GeoServerExtensions;
import org.geoserver.GeoServerResourceLoader;
import org.geoserver.data.CatalogLoader;
import org.geoserver.data.DefaultGeoServerCatalog;
import org.geoserver.data.GeoServerCatalog;
import org.geoserver.data.GeoServerResolveAdapterFactoryFinder;
import org.geoserver.data.GeoServerServiceFinder;
import org.geoserver.data.feature.InfoAdapterFactory;
import org.geotools.catalog.Service;
import org.geotools.catalog.adaptable.AdaptingServiceFinder;
import org.geotools.catalog.property.PropertyServiceFactory;
import org.geotools.catalog.styling.SLDServiceFactory;
import org.geotools.data.property.PropertyDataStoreFactory;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Abstract test class for tests which need data or a catalog.
 * <p>
 * This class creates populates the catalog with data mimicing the WMS 1.1.1 and WFS 1.0 
 * cite setup.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class DataTestSupport extends TestCase {
    protected static final Logger LOGGER = Logger.getLogger("org.geoserver.unittests");

    /**
     * Mock data directory 
     */
    protected MockGeoServerDataDirectory data;
    
	/**
     * The catalog
     */
    protected GeoServerCatalog catalog;
    
    /**
     * Resource loader
     */
    protected GeoServerResourceLoader loader;
    
    /**
     * Application context
     */
    protected GenericApplicationContext context;
    
    /**
	 * Creates an instance of the geoserver catalog populated with cite data.
	 * 
	 */
    protected void setUp() throws Exception {
	   data = new MockGeoServerDataDirectory();
	   data.setUp();
	   
	   context = new GenericApplicationContext();
	   GeoServerExtensions extensions = new GeoServerExtensions();
	   context.getBeanFactory().registerSingleton( "geoServerExtensions", extensions );
	   extensions.setApplicationContext( context );
	   
	   loader = new GeoServerResourceLoader( data.getDataDirectoryRoot() );
	   context.getBeanFactory().registerSingleton( "resourceLoader", loader );
	   
	   catalog = createCiteCatalog( context );
	}
    
    protected void tearDown() throws Exception {
    	data.tearDown();
    }
    
    /**
     * Creates the geosrever catalog and populates it with cite data.
     * <p>
     * Subclasses should override/extend as necessary to provide a custom 
     * catalog. This default implementation returns an instanceof 
     * {@link DefaultGeoServerCatalog}. 
     * </p>
     * @return A popluated geoserver catalog.
     * @throws Exception 
     */
    private GeoServerCatalog createCiteCatalog( GenericApplicationContext conext ) throws Exception {
    		
		GeoServerResolveAdapterFactoryFinder adapterFinder 
    			= new GeoServerResolveAdapterFactoryFinder();
		DefaultGeoServerCatalog catalog = new DefaultGeoServerCatalog( adapterFinder );
		
		GeoServerServiceFinder finder = new GeoServerServiceFinder( catalog );
		AdaptingServiceFinder adaptingFinder = new AdaptingServiceFinder( catalog, finder );
		
		PropertyServiceFactory factory = new PropertyServiceFactory();
		SLDServiceFactory sldFactory = new SLDServiceFactory();
		
		context.getBeanFactory().registerSingleton( "finder", adaptingFinder );
		context.getBeanFactory().registerSingleton( "propertyServiceFactory", factory );
		context.getBeanFactory().registerSingleton( "sldServiceFactory", sldFactory );
		context.getBeanFactory().registerSingleton( 
			"adapter", new InfoAdapterFactory( catalog, loader ) 
		);
		adapterFinder.setApplicationContext( context );
		finder.setApplicationContext( context );
		
		GeoServerResourceLoader resourceLoader = 
			new GeoServerResourceLoader( data.getDataDirectoryRoot() );
		
		CatalogLoader catalogLoader = new CatalogLoader( resourceLoader, catalog, adaptingFinder );
		catalogLoader.afterPropertiesSet();
		
		return catalog;
	}
    
    
    protected String[] citeTypeNames() {
    	return MockGeoServerDataDirectory.citeTypeNames;
    }
    
    protected String[] wmsCiteTypeNames() {
    	return MockGeoServerDataDirectory.wmsCiteTypeNames;
    }
    
    protected String[] wfsCiteTypeNames() {
    	return MockGeoServerDataDirectory.wfsCiteTypeNames;
    }
}
