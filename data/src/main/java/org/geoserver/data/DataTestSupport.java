package org.geoserver.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.geoserver.GeoServerResourceLoader;
import org.geoserver.data.feature.InfoAdapterFactory;
import org.geotools.catalog.Service;
import org.geotools.catalog.property.PropertyServiceFactory;
import org.geotools.catalog.styling.SLDServiceFactory;

import org.geotools.data.property.PropertyDataStoreFactory;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Abstract test class for tests which need data or a catalog.
 * <p>
 * This class creates populates the catalog with data mimicing the WMS cite
 * setup.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class DataTestSupport extends TestCase {

	 /** featuretype name for CITE BasicPolygons features */
    public static String BASIC_POLYGONS_TYPE = "BasicPolygons";

    /** featuretype name for CITE Bridges features */
    public static String BRIDGES_TYPE = "Bridges";

    /** featuretype name for CITE Buildings features */
    public static String BUILDINGS_TYPE = "Buildings";

    /** featuretype name for CITE Divided Routes features */
    public static String DIVIDED_ROUTES_TYPE = "DividedRoutes";

    /** featuretype name for CITE Forests features */
    public static String FORESTS_TYPE = "Forests";

    /** featuretype name for CITE Lakes features */
    public static String LAKES_TYPE = "Lakes";

    /** featuretype name for CITE Map Neatliine features */
    public static String MAP_NEATLINE_TYPE = "MapNeatline";

    /** featuretype name for CITE Named Places features */
    public static String NAMED_PLACES_TYPE = "NamedPlaces";

    /** featuretype name for CITE Ponds features */
    public static String PONDS_TYPE = "Ponds";

    /** featuretype name for CITE Road Segments features */
    public static String ROAD_SEGMENTS_TYPE = "RoadSegments";

    /** featuretype name for CITE Streams features */
    public static String STREAMS_TYPE = "Streams";
    
    /**
     * cite namespace + uri
     */
    public static String CITE_PREFIX = "cite";
    public static String CITE_URI = "http://www.opengis.net/cite";
    
    /**
     * Temporary directory for property files.
     */
    File tmp;
    
    /**
     * The catalog
     */
    protected GeoServerCatalog catalog;
    
    /**
     * Resource loader
     */
    protected GeoServerResourceLoader loader;
    
    /**
	 * Creates an instance of the geoserver catalog populated with cite data.
	 * 
	 */
    protected void setUp() throws Exception {
	   tmp = File.createTempFile("cite","test");
	   tmp.delete();
	   tmp.mkdir();
	   
	   //create a featureTypes directory
	   File featureTypes = new File( tmp, "featureTypes" );
	   featureTypes.mkdir();
	   
	   //create the styles directory
	   File styles = new File( tmp, "styles" );
	   styles.mkdir();
	   
	   for ( int i = 0; i < citeTypeNames().length; i++ ) {
		   String type = citeTypeNames()[i];
		   copy( type );
		   info( type, featureTypes );
		   style( type, styles );
	   }
	   
	   loader = new GeoServerResourceLoader( tmp );
	   catalog = createCiteCatalog();
	}
    
    void copy( String type ) throws IOException {
    		//copy over the properties file
    		InputStream from = DataTestSupport.class.getResourceAsStream( "data/" + type + ".properties" );
    		File to = new File( tmp, type + ".properties" ); 
    		copy( from, to );
   	}

    void copy ( InputStream from, File to ) throws IOException {
    		InputStream in = new BufferedInputStream( from );
		OutputStream out = new BufferedOutputStream( new FileOutputStream( to ) );
		
		int b = 0;
		while ( ( b = in.read() ) != -1 ) out.write( b );
		
		in.close();
		out.flush();
		out.close();	
    }
    
    void info( String type, File dir ) throws Exception {
    		File featureTypeDir = new File( dir, type );
    		featureTypeDir.mkdir();
    		
    		File info = new File( featureTypeDir, "info.xml" );
    		info.createNewFile();
    		
    		FileWriter writer = new FileWriter( info );
    		writer.write( "<featureType datastore=\"cite\">" );
    		writer.write( "<name>" + type + "</name>" );
    		writer.write( "<SRS>4326</SRS>" );
    		writer.write( "<title>" + type + "</title>" );
    		writer.write( "<abstract>abstract about " + type + "</abstract>" );
    		writer.write( "<numDecimals value=\"0\"/>" );
    		writer.write( "<keywords>" + type + "</keywords>" );
    		writer.write( "<latLonBoundingBox dynamic=\"false\" minx=\"0\" miny=\"0\" maxx=\"0\" maxy=\"0\"/>" );
    		writer.write( "<styles default=\"someStyle\"/>" );
    		writer.write( "</featureType>" );
    		
    		writer.flush();
    		writer.close();
    	}
    
    void style( String type, File dir ) throws IOException {
 		//copy over the properties file
		InputStream from = DataTestSupport.class.getResourceAsStream( "data/" + type + ".sld" );
		File to = new File( dir, type + ".sld" ); 
		copy( from, to );	
    }
    
    /**
     * Creates the geosrever catalog and populates it with cite data.
     * <p>
     * Subclasses should override/extend as necessary to provide a custom 
     * catalog. This default implementation returns an instanceof 
     * {@link DefaultGeoServerCatalog}. 
     * </p>
     * @return A popluated geoserver catalog.
     */
    protected GeoServerCatalog createCiteCatalog() {
    		
    		GeoServerResolveAdapterFactoryFinder adapterFinder 
    			= new GeoServerResolveAdapterFactoryFinder();
		GeoServerCatalog catalog = new DefaultGeoServerCatalog( adapterFinder );
		
		GeoServerServiceFinder finder = new GeoServerServiceFinder( catalog );
		PropertyServiceFactory factory = new PropertyServiceFactory();
		SLDServiceFactory sldFactory = new SLDServiceFactory();
		
		GenericApplicationContext context = new GenericApplicationContext();
		context.getBeanFactory().registerSingleton( "finder", finder );
		context.getBeanFactory().registerSingleton( "propertyServiceFactory", factory );
		context.getBeanFactory().registerSingleton( "sldServiceFactory", sldFactory );
		context.getBeanFactory().registerSingleton( 
			"adapter", new InfoAdapterFactory( catalog, loader ) 
		);
		adapterFinder.setApplicationContext( context );
		finder.setApplicationContext( context );
		
		//set up the data
		HashMap params = new HashMap();
		params.put( PropertyDataStoreFactory.DIRECTORY.key, tmp );
		
		List services = finder.aquire( params );
		catalog.add( (Service) services.get( 0 ) );
		
		//setup the styles
		services = finder.aquire( new File( tmp, "styles").toURI() );
		catalog.add( (Service) services.get( 0 ) );
		
		//setup the namespaces
		catalog.getNamespaceSupport().declarePrefix( "", CITE_URI );
		catalog.getNamespaceSupport().declarePrefix( CITE_PREFIX, CITE_URI );
		
		return catalog;
	}
    
    protected void tearDown() throws Exception {
    		delete( new File( tmp, "styles" ) );
    		delete( new File( tmp, "featureTypes" ) );
    		delete( tmp );
    	}

    void delete( File dir ) throws IOException {
    		File[] files = dir.listFiles();
 	   for ( int i = 0; i < files.length; i++ ) {
 		   files[i].delete();
 	   }
 	   
 	   dir.delete();	
    }
    
    /**
     * @return The cite type names as an array of strings.
     */
    protected String[] citeTypeNames() {
    		return new String[]{
			BASIC_POLYGONS_TYPE, BRIDGES_TYPE, BUILDINGS_TYPE,
			DIVIDED_ROUTES_TYPE, FORESTS_TYPE, LAKES_TYPE, MAP_NEATLINE_TYPE,
    			NAMED_PLACES_TYPE, PONDS_TYPE, ROAD_SEGMENTS_TYPE, STREAMS_TYPE 
		};
    }
}
