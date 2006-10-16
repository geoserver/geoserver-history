package org.geoserver.data;

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

import junit.framework.TestCase;

import org.geoserver.GeoServerExtensions;
import org.geoserver.GeoServerResourceLoader;
import org.geoserver.data.feature.InfoAdapterFactory;
import org.geotools.catalog.Service;
import org.geotools.catalog.property.PropertyServiceFactory;
import org.geotools.catalog.styling.SLDServiceFactory;

import org.geotools.data.property.PropertyDataStoreFactory;
import org.springframework.context.ApplicationContext;
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

	//WMS
	/**
     * cite namespace + uri
     */
    public static String CITE_PREFIX = "cite";
    public static String CITE_URI = "http://www.opengis.net/cite";
	
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
    
    //WFS
    /**
     * cdf namespace + uri
     */
    public static String CDF_PREFIX = "cdf";
    public static String CDF_URI = "http://www.opengis.net/cite/data";
    
    /**
     * cgf namespace + uri
     */
    public static String CGF_PREFIX = "cgf";
    public static String CGF_URI = "http://www.opengis.net/cite/geometry";
    
    /** featuretype name for CITE Deletes features */
    public static String DELETES_TYPE = "Deletes";
    
    /** featuretype name for CITE Fifteen features */
    public static String FIFTEEN_TYPE = "Fifteen";
    
    /** featuretype name for CITE Inserts features */
    public static String INSERTS_TYPE = "Inserts";
    
    /** featuretype name for CITE Inserts features */
    public static String LOCKS_TYPE = "Locks";
    
    /** featuretype name for CITE Nulls features */
    public static String NULLS_TYPE = "Nulls";
    
    /** featuretype name for CITE Other features */
    public static String OTHER_TYPE = "Other";
    
    /** featuretype name for CITE Nulls features */
    public static String SEVEN_TYPE = "Seven";
    
    /** featuretype name for CITE Updates features */
    public static String UPDATES_TYPE = "Updates";
    
    /** featuretype name for CITE Lines features */
    public static String LINES_TYPE = "Lines";
    
    /** featuretype name for CITE MLines features */
    public static String MLINES_TYPE = "MLines";
    
    /** featuretype name for CITE MPoints features */
    public static String MPOINTS_TYPE = "MPoints";
    
    /** featuretype name for CITE MPolygons features */
    public static String MPOLYGONS_TYPE = "MPolygons";
    
    /** featuretype name for CITE Points features */
    public static String POINTS_TYPE = "Points";
    
    /** featuretype name for CITE Polygons features */
    public static String POLYGONS_TYPE = "Polygons";
    
    /**
     * Data directory
     */
    File data;
    File featureTypes;
    File styles;
    
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
	   data = File.createTempFile("cite","test");
	   data.delete();
	   data.mkdir();
	   
	   //create a featureTypes directory
	   featureTypes = new File( data, "featureTypes" );
	   featureTypes.mkdir();
	   
	   //create the styles directory
	   styles = new File( data, "styles" );
	   styles.mkdir();
	   
	   //set up types
	   setup( CITE_PREFIX, BASIC_POLYGONS_TYPE );
	   setup( CITE_PREFIX, BRIDGES_TYPE );
	   setup( CITE_PREFIX, BUILDINGS_TYPE );
	   setup( CITE_PREFIX, DIVIDED_ROUTES_TYPE );
	   setup( CITE_PREFIX, FORESTS_TYPE );
	   setup( CITE_PREFIX, LAKES_TYPE );
	   setup( CITE_PREFIX, MAP_NEATLINE_TYPE );
	   setup( CITE_PREFIX, NAMED_PLACES_TYPE );
	   setup( CITE_PREFIX, PONDS_TYPE );
	   setup( CITE_PREFIX, ROAD_SEGMENTS_TYPE );
	   setup( CITE_PREFIX, STREAMS_TYPE );

	   setup( CDF_PREFIX, DELETES_TYPE );
	   setup( CDF_PREFIX, FIFTEEN_TYPE );
	   setup( CDF_PREFIX, INSERTS_TYPE );
	   setup( CDF_PREFIX, LOCKS_TYPE );
	   setup( CDF_PREFIX, NULLS_TYPE );
	   setup( CDF_PREFIX, OTHER_TYPE );
	   setup( CDF_PREFIX, SEVEN_TYPE );
	   setup( CDF_PREFIX, UPDATES_TYPE );

	   setup( CGF_PREFIX, LINES_TYPE );
	   setup( CGF_PREFIX, MLINES_TYPE );
	   setup( CGF_PREFIX, MPOINTS_TYPE );
	   setup( CGF_PREFIX, MPOLYGONS_TYPE );
	   setup( CGF_PREFIX, POINTS_TYPE );
	   setup( CGF_PREFIX, POLYGONS_TYPE );

	   context = new GenericApplicationContext();
	   GeoServerExtensions extensions = new GeoServerExtensions();
	   context.getBeanFactory().registerSingleton( "geoServerExtensions", extensions );
	   extensions.setApplicationContext( context );
	   
	   loader = new GeoServerResourceLoader( data );
	   context.getBeanFactory().registerSingleton( "resourceLoader", loader );
	   
	   
	   catalog = createCiteCatalog( context );
	}
    
    void setup( String prefix, String type ) throws Exception {
    	  properties( prefix, type );
		  info( prefix, type );
		  style( type );
    }
    
    void properties( String prefix, String type ) throws IOException {
    	
		//copy over the properties file
		InputStream from = DataTestSupport.class.getResourceAsStream( "data/" + type + ".properties" );
		File directory = new File( data, prefix );
		directory.mkdir();
		File to = new File( directory, type + ".properties" ); 
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
    
    void info( String prefix, String type ) throws Exception {
    	
    	File featureTypeDir = new File( featureTypes, prefix + "_" + type );
		featureTypeDir.mkdir();
		
		File info = new File( featureTypeDir, "info.xml" );
		info.createNewFile();
		
		FileWriter writer = new FileWriter( info );
		writer.write( "<featureType datastore=\"" + prefix + "\">" );
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
    
    void style( String type ) throws IOException {
    	
 		//copy over the properties file
    	if ( DataTestSupport.class.getResource( "data/" + type + ".sld" ) == null ) 
    		return;
    	
		InputStream from = DataTestSupport.class.getResourceAsStream( "data/" + type + ".sld" );
		
		File to = new File( styles, type + ".sld" ); 
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
    private GeoServerCatalog createCiteCatalog( GenericApplicationContext conext ) {
    		
		GeoServerResolveAdapterFactoryFinder adapterFinder 
    			= new GeoServerResolveAdapterFactoryFinder();
		GeoServerCatalog catalog = new DefaultGeoServerCatalog( adapterFinder );
		
		GeoServerServiceFinder finder = new GeoServerServiceFinder( catalog );
		PropertyServiceFactory factory = new PropertyServiceFactory();
		SLDServiceFactory sldFactory = new SLDServiceFactory();
		
		context.getBeanFactory().registerSingleton( "finder", finder );
		context.getBeanFactory().registerSingleton( "propertyServiceFactory", factory );
		context.getBeanFactory().registerSingleton( "sldServiceFactory", sldFactory );
		context.getBeanFactory().registerSingleton( 
			"adapter", new InfoAdapterFactory( catalog, loader ) 
		);
		adapterFinder.setApplicationContext( context );
		finder.setApplicationContext( context );
		
		//setup the namespaces
		catalog.getNamespaceSupport().declarePrefix( CITE_PREFIX, CITE_URI );
		catalog.getNamespaceSupport().declarePrefix( CDF_PREFIX, CDF_URI );
		catalog.getNamespaceSupport().declarePrefix( CGF_PREFIX, CGF_URI );
		
		//set up the services
		HashMap params = new HashMap();
		params.put( PropertyDataStoreFactory.DIRECTORY.key, new File( data, CITE_PREFIX ) );
		params.put( PropertyDataStoreFactory.NAMESPACE.key, CITE_URI );
		
		List services = finder.aquire( params );
		catalog.add( (Service) services.get( 0 ) );
		
		params = new HashMap();
		params.put( PropertyDataStoreFactory.DIRECTORY.key, new File( data, CDF_PREFIX ) );
		params.put( PropertyDataStoreFactory.NAMESPACE.key, CDF_URI );
		
		services = finder.aquire( params );
		catalog.add( (Service) services.get( 0 ) );
		
		params = new HashMap();
		params.put( PropertyDataStoreFactory.DIRECTORY.key, new File( data, CGF_PREFIX ) );
		params.put( PropertyDataStoreFactory.NAMESPACE.key, CGF_URI );
		
		services = finder.aquire( params );
		catalog.add( (Service) services.get( 0 ) );
		
		//setup the styles
		services = finder.aquire( new File( data, "styles").toURI() );
		catalog.add( (Service) services.get( 0 ) );
		
		return catalog;
	}
    
    protected void tearDown() throws Exception {
    		delete( styles );
    		delete( featureTypes );
    		delete( data );
    	}

    void delete( File dir ) throws IOException {
    		File[] files = dir.listFiles();
 	   for ( int i = 0; i < files.length; i++ ) {
 		   if ( files[i].isDirectory() ) {
 			   delete( files[i] );
 		   }
 		   else {
 			  files[i].delete();   
 		   }
 		   
 	   }
 	   
 	   dir.delete();	
    }
    
    /**
     * @return The qualified cite type names as an array of strings.
     */
    protected String[] citeTypeNames() {
    	ArrayList names = new ArrayList();
    	names.addAll( Arrays.asList( wmsCiteTypeNames() ) );
    	names.addAll( Arrays.asList( wfsCiteTypeNames() ) );
    	
    	return (String[]) names.toArray( new String[ names.size() ] );
    }
    
    protected String[] wmsCiteTypeNames() {
    	return new String[] {
			BASIC_POLYGONS_TYPE, BRIDGES_TYPE, BUILDINGS_TYPE,
			DIVIDED_ROUTES_TYPE, FORESTS_TYPE, LAKES_TYPE, MAP_NEATLINE_TYPE,
    		NAMED_PLACES_TYPE, PONDS_TYPE, ROAD_SEGMENTS_TYPE, STREAMS_TYPE 
		};
    }
    
    protected String[] wfsCiteTypeNames() {
    	return new String[] {
			DELETES_TYPE, FIFTEEN_TYPE, INSERTS_TYPE, LOCKS_TYPE, NULLS_TYPE, 
    		OTHER_TYPE, SEVEN_TYPE, UPDATES_TYPE, LINES_TYPE, MLINES_TYPE, 
		    MPOINTS_TYPE, MPOLYGONS_TYPE, POINTS_TYPE, POLYGONS_TYPE
    	};
    }
}
