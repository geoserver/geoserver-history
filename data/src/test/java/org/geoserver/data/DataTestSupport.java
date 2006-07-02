package org.geoserver.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.catalog.Service;
import org.geotools.catalog.property.PropertyServiceFactory;
import org.geotools.data.property.PropertyDataStoreFactory;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Abstract test class for tests which need data or a catalog.
 * <p>
 * This class creates the 11 cite test types in a 
 * {@link org.geotools.data.property.PropertyDataStore} and populates a 
 * instance of the catalog with them.
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
     * Temporary directory for property files.
     */
    File tmp;
    
    /**
     * The catalog
     */
    protected GeoServerCatalog catalog;
    
    /**
	 * Creates an instance of the geoserver catalog populated with cite data.
	 * 
	 */
    protected void setUp() throws Exception {
	   tmp = File.createTempFile("cite","test");
	   tmp.delete();
	   tmp.mkdir();
	   
	   copy( BASIC_POLYGONS_TYPE );
	   copy(  BRIDGES_TYPE );
	   copy(  BUILDINGS_TYPE );
	   copy(  DIVIDED_ROUTES_TYPE );
	   copy(  FORESTS_TYPE );
	   copy(  LAKES_TYPE );
	   copy(  MAP_NEATLINE_TYPE );
	   copy(  NAMED_PLACES_TYPE );
	   copy(  PONDS_TYPE );
	   copy(  ROAD_SEGMENTS_TYPE );
	   copy(  STREAMS_TYPE );
	   
	   catalog = createCiteCatalog();
	   
	}
    
    void copy( String type ) throws IOException {
    		File from = new File( 
			getClass().getResource( "data" + File.separator + type + ".properties").getFile() 
		);
    		
    		File to = new File( tmp, type + ".properties" ); 
    		
    		InputStream in = new BufferedInputStream( new FileInputStream( from ) );
    		OutputStream out = new BufferedOutputStream( new FileOutputStream( to ) );
    		
    		int b = 0;
    		while ( ( b = in.read() ) != -1 ) out.write( b );
    		
    		in.close();
    		out.flush();
    		out.close();
    }
    
    GeoServerCatalog createCiteCatalog() {
		GeoServerCatalog catalog = new GeoServerCatalog();
		GeoServerServiceFinder finder = new GeoServerServiceFinder( catalog );
		PropertyServiceFactory factory = new PropertyServiceFactory();
		
		GenericApplicationContext context = new GenericApplicationContext();
		context.getBeanFactory().registerSingleton( "finder", finder );
		context.getBeanFactory().registerSingleton( "psf", factory );
		
		finder.setApplicationContext( context );
		
		HashMap params = new HashMap();
		params.put( PropertyDataStoreFactory.DIRECTORY.key, tmp );
		
		List services = finder.aquire( params );
		catalog.add( (Service) services.get( 0 ) );
		
		return catalog;
	}
    
    protected void tearDown() throws Exception {
	   File[] files = tmp.listFiles();
	   for ( int i = 0; i < files.length; i++ ) {
		   files[i].delete();
	   }
	   
	   tmp.delete();
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
