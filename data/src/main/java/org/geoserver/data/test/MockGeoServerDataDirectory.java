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

import org.geoserver.data.CatalogWriter;
import org.geotools.data.property.PropertyDataStoreFactory;

/**
 * Class used to build a mock GeoServer data directory.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class MockGeoServerDataDirectory {

	// WMS
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

	// WFS
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

	public static String[] citeTypeNames = new String[] {
		BASIC_POLYGONS_TYPE, BRIDGES_TYPE, BUILDINGS_TYPE,
		DIVIDED_ROUTES_TYPE, FORESTS_TYPE, LAKES_TYPE, MAP_NEATLINE_TYPE,
		NAMED_PLACES_TYPE, PONDS_TYPE, ROAD_SEGMENTS_TYPE, STREAMS_TYPE,
		DELETES_TYPE, FIFTEEN_TYPE, INSERTS_TYPE, LOCKS_TYPE, NULLS_TYPE, 
		OTHER_TYPE, SEVEN_TYPE, UPDATES_TYPE, LINES_TYPE, MLINES_TYPE, 
	    MPOINTS_TYPE, MPOLYGONS_TYPE, POINTS_TYPE, POLYGONS_TYPE
	};
	
	public static String[] wmsCiteTypeNames = new String[] {
		BASIC_POLYGONS_TYPE, BRIDGES_TYPE, BUILDINGS_TYPE,
		DIVIDED_ROUTES_TYPE, FORESTS_TYPE, LAKES_TYPE, MAP_NEATLINE_TYPE,
		NAMED_PLACES_TYPE, PONDS_TYPE, ROAD_SEGMENTS_TYPE, STREAMS_TYPE 
	};
	
	public static String[] wfsCiteTypeNames = new String[] {
		DELETES_TYPE, FIFTEEN_TYPE, INSERTS_TYPE, LOCKS_TYPE, NULLS_TYPE, 
		OTHER_TYPE, SEVEN_TYPE, UPDATES_TYPE, LINES_TYPE, MLINES_TYPE, 
	    MPOINTS_TYPE, MPOLYGONS_TYPE, POINTS_TYPE, POLYGONS_TYPE
	};
	
	/** the base of the data directory */
	File data;
	File featureTypes;
	File styles;

	/**
	 * @param base
	 *            Base of the GeoServer data directory.
	 * 
	 * @throws IOException
	 */
	public MockGeoServerDataDirectory() throws IOException {
		data = File.createTempFile("mock", "data");
	}

	/**
	 * @return The root of the data directory.
	 */
	public File getDataDirectoryRoot() {
		return data;
	}
	
	/**
	 * Sets up the data directory, creating all the necessary files.
	 * 
	 * @throws IOException
	 */
	public void setUp() throws IOException {
		data.delete();
		data.mkdir();

		// create a featureTypes directory
		featureTypes = new File(data, "featureTypes");
		featureTypes.mkdir();

		// create the styles directory
		styles = new File(data, "styles");
		styles.mkdir();

		// set up types
		setup(CITE_PREFIX, BASIC_POLYGONS_TYPE);
		setup(CITE_PREFIX, BRIDGES_TYPE);
		setup(CITE_PREFIX, BUILDINGS_TYPE);
		setup(CITE_PREFIX, DIVIDED_ROUTES_TYPE);
		setup(CITE_PREFIX, FORESTS_TYPE);
		setup(CITE_PREFIX, LAKES_TYPE);
		setup(CITE_PREFIX, MAP_NEATLINE_TYPE);
		setup(CITE_PREFIX, NAMED_PLACES_TYPE);
		setup(CITE_PREFIX, PONDS_TYPE);
		setup(CITE_PREFIX, ROAD_SEGMENTS_TYPE);
		setup(CITE_PREFIX, STREAMS_TYPE);

		setup(CDF_PREFIX, DELETES_TYPE);
		setup(CDF_PREFIX, FIFTEEN_TYPE);
		setup(CDF_PREFIX, INSERTS_TYPE);
		setup(CDF_PREFIX, LOCKS_TYPE);
		setup(CDF_PREFIX, NULLS_TYPE);
		setup(CDF_PREFIX, OTHER_TYPE);
		setup(CDF_PREFIX, SEVEN_TYPE);
		setup(CDF_PREFIX, UPDATES_TYPE);

		setup(CGF_PREFIX, LINES_TYPE);
		setup(CGF_PREFIX, MLINES_TYPE);
		setup(CGF_PREFIX, MPOINTS_TYPE);
		setup(CGF_PREFIX, MPOLYGONS_TYPE);
		setup(CGF_PREFIX, POINTS_TYPE);
		setup(CGF_PREFIX, POLYGONS_TYPE);
		
		//create the catalog.xml
		CatalogWriter writer = new CatalogWriter();
		
		//set up the datastores
		HashMap dataStores = new HashMap();
		
		HashMap params = new HashMap();
		params.put( PropertyDataStoreFactory.DIRECTORY.key, new File( data, CITE_PREFIX ) );
		params.put( PropertyDataStoreFactory.NAMESPACE.key, CITE_URI );
		dataStores.put( CITE_PREFIX, params );
		
		params = new HashMap();
		params.put( PropertyDataStoreFactory.DIRECTORY.key, new File( data, CDF_PREFIX ) );
		params.put( PropertyDataStoreFactory.NAMESPACE.key, CDF_URI );
		dataStores.put( CDF_PREFIX, params );
		
		params = new HashMap();
		params.put( PropertyDataStoreFactory.DIRECTORY.key, new File( data, CGF_PREFIX ) );
		params.put( PropertyDataStoreFactory.NAMESPACE.key, CGF_URI );
		dataStores.put( CGF_PREFIX, params );
		
		writer.dataStores( dataStores );
		
		//setup the namespaces
		HashMap namespaces = new HashMap();
		namespaces.put( CITE_PREFIX, CITE_URI );
		namespaces.put( CDF_PREFIX, CDF_URI );
		namespaces.put( CGF_PREFIX, CGF_URI );
		
		writer.namespaces( namespaces );
		
		//styles
		HashMap styles = new HashMap();
		
		for ( int i = 0; i < wmsCiteTypeNames.length; i++ ) {
			String type = wmsCiteTypeNames[ i ];
			styles.put( type, type + ".sld" );
		}
		
		writer.styles( styles );
		
		writer.write( new File( data, "catalog.xml" ) );
	}

	void setup(String prefix, String type) throws IOException {
		properties(prefix, type);
		info(prefix, type);
		style(type);
	}

	void properties(String prefix, String type) throws IOException {

		// copy over the properties file
		InputStream from = DataTestSupport.class.getResourceAsStream("data/"
				+ type + ".properties");
		File directory = new File(data, prefix);
		directory.mkdir();
		File to = new File(directory, type + ".properties");
		copy(from, to);

	}

	void copy(InputStream from, File to) throws IOException {
		InputStream in = new BufferedInputStream(from);
		OutputStream out = new BufferedOutputStream(new FileOutputStream(to));

		int b = 0;
		while ((b = in.read()) != -1)
			out.write(b);

		in.close();
		out.flush();
		out.close();
	}

	void info(String prefix, String type) throws IOException {

		File featureTypeDir = new File(featureTypes, prefix + "_" + type);
		featureTypeDir.mkdir();

		File info = new File(featureTypeDir, "info.xml");
		info.createNewFile();

		FileWriter writer = new FileWriter(info);
		writer.write("<featureType datastore=\"" + prefix + "\">");
		writer.write("<name>" + type + "</name>");
		writer.write("<SRS>4326</SRS>");
		writer.write("<title>" + type + "</title>");
		writer.write("<abstract>abstract about " + type + "</abstract>");
		writer.write("<numDecimals value=\"0\"/>");
		writer.write("<keywords>" + type + "</keywords>");
		writer
				.write("<latLonBoundingBox dynamic=\"false\" minx=\"0\" miny=\"0\" maxx=\"0\" maxy=\"0\"/>");
		writer.write("<styles default=\"someStyle\"/>");
		writer.write("</featureType>");

		writer.flush();
		writer.close();
	}

	void style(String type) throws IOException {

		// copy over the properties file
		if (DataTestSupport.class.getResource("data/" + type + ".sld") == null)
			return;

		InputStream from = DataTestSupport.class.getResourceAsStream("data/"
				+ type + ".sld");

		File to = new File(styles, type + ".sld");
		copy(from, to);
	}

	/**
	 * Kills the data directory, deleting all the files.
	 * 
	 * @throws IOException
	 */
	public void tearDown() throws IOException {
		delete(styles);
		delete(featureTypes);
		delete(data);
	}

	void delete(File dir) throws IOException {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				delete(files[i]);
			} else {
				files[i].delete();
			}

		}

		dir.delete();
	}
}
