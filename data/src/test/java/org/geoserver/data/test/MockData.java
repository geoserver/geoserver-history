/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.data.test;

import org.geoserver.data.CatalogWriter;
import org.geotools.data.property.PropertyDataStoreFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;


/**
 * Class used to build a mock GeoServer data directory.
 * <p>
 * Data is based off the wms and wfs "cite" datasets.
 * </p>
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class MockData {
    // //// WMS 1.1.1
    /**
     * WMS 1.1.1 cite namespace + uri
     */
    public static String CITE_PREFIX = "cite";
    public static String CITE_URI = "http://www.opengis.net/cite";

    /** featuretype name for WMS 1.1.1 CITE BasicPolygons features */
    public static QName BASIC_POLYGONS = new QName(CITE_URI, "BasicPolygons", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Bridges features */
    public static QName BRIDGES = new QName(CITE_URI, "Bridges", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Buildings features */
    public static QName BUILDINGS = new QName(CITE_URI, "Buildings", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Divided Routes features */
    public static QName DIVIDED_ROUTES = new QName(CITE_URI, "DividedRoutes", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Forests features */
    public static QName FORESTS = new QName(CITE_URI, "Forests", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Lakes features */
    public static QName LAKES = new QName(CITE_URI, "Lakes", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Map Neatliine features */
    public static QName MAP_NEATLINE = new QName(CITE_URI, "MapNeatline", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Named Places features */
    public static QName NAMED_PLACES = new QName(CITE_URI, "NamedPlaces", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Ponds features */
    public static QName PONDS = new QName(CITE_URI, "Ponds", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Road Segments features */
    public static QName ROAD_SEGMENTS = new QName(CITE_URI, "RoadSegments", CITE_PREFIX);

    /** featuretype name for WMS 1.1.1 CITE Streams features */
    public static QName STREAMS = new QName(CITE_URI, "Streams", CITE_PREFIX);

    // /// WFS 1.0
    /**
     * WFS 1.0 cdf namespace + uri
     */
    public static String CDF_PREFIX = "cdf";
    public static String CDF_URI = "http://www.opengis.net/cite/data";

    /** featuretype name for WFS 1.0 CITE Deletes features */
    public static QName DELETES = new QName(CDF_URI, "Deletes", CDF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Fifteen features */
    public static QName FIFTEEN = new QName(CDF_URI, "Fifteen", CDF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Inserts features */
    public static QName INSERTS = new QName(CDF_URI, "Inserts", CDF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Inserts features */
    public static QName LOCKS = new QName(CDF_URI, "Locks", CDF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Nulls features */
    public static QName NULLS = new QName(CDF_URI, "Nulls", CDF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Other features */
    public static QName OTHER = new QName(CDF_URI, "Other", CDF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Nulls features */
    public static QName SEVEN = new QName(CDF_URI, "Seven", CDF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Updates features */
    public static QName UPDATES = new QName(CDF_URI, "Updates", CDF_PREFIX);

    /**
     * cgf namespace + uri
     */
    public static String CGF_PREFIX = "cgf";
    public static String CGF_URI = "http://www.opengis.net/cite/geometry";

    /** featuretype name for WFS 1.0 CITE Lines features */
    public static QName LINES = new QName(CGF_URI, "Lines", CGF_PREFIX);

    /** featuretype name for WFS 1.0 CITE MLines features */
    public static QName MLINES = new QName(CGF_URI, "MLines", CGF_PREFIX);

    /** featuretype name for WFS 1.0 CITE MPoints features */
    public static QName MPOINTS = new QName(CGF_URI, "MPoints", CGF_PREFIX);

    /** featuretype name for WFS 1.0 CITE MPolygons features */
    public static QName MPOLYGONS = new QName(CGF_URI, "MPolygons", CGF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Points features */
    public static QName POINTS = new QName(CGF_URI, "Points", CGF_PREFIX);

    /** featuretype name for WFS 1.0 CITE Polygons features */
    public static QName POLYGONS = new QName(CGF_URI, "Polygons", CGF_PREFIX);

    // //// WFS 1.1
    /**
     * sf namespace + uri
     */
    public static String SF_PREFIX = "sf";
    public static String SF_URI = "http://cite.opengeospatial.org/gmlsf";
    public static QName PRIMITIVEGEOFEATURE = new QName(SF_URI, "PrimitiveGeoFeature", SF_PREFIX);
    public static QName AGGREGATEGEOFEATURE = new QName(SF_URI, "AggregateGeoFeature", SF_PREFIX);

    // DEFAULT
    public static String DEFAULT_PREFIX = "gs";
    public static String DEFAULT_URI = "http://geoserver.org";

    // public static QName ENTIT\u00C9G\u00C9N\u00C9RIQUE = new QName( SF_URI,
    // "Entit\u00E9G\u00E9n\u00E9rique", SF_PREFIX );

    /**
     * List of all cite types names
     */
    public static QName[] TYPENAMES = new QName[] {
            // WMS 1.1.1
            BASIC_POLYGONS, BRIDGES, BUILDINGS, DIVIDED_ROUTES, FORESTS, LAKES, MAP_NEATLINE,
            NAMED_PLACES, PONDS, ROAD_SEGMENTS, STREAMS, // WFS 1.0
            DELETES, FIFTEEN, INSERTS, LOCKS, NULLS, OTHER, SEVEN, UPDATES, LINES, MLINES, MPOINTS,
            MPOLYGONS, POINTS, POLYGONS, // WFS 1.1
            PRIMITIVEGEOFEATURE, AGGREGATEGEOFEATURE, /* ENTIT\u00C9G\u00C9N\u00C9RIQUE */
        };

    /**
     * List of wms type names.
     */
    public static QName[] WMS_TYPENAMES = new QName[] {
            BASIC_POLYGONS, BRIDGES, BUILDINGS, DIVIDED_ROUTES, FORESTS, LAKES, MAP_NEATLINE,
            NAMED_PLACES, PONDS, ROAD_SEGMENTS, STREAMS
        };

    /**
     * List of wfs 1.0 type names.
     */
    public static QName[] WFS10_TYPENAMES = new QName[] {
            DELETES, FIFTEEN, INSERTS, LOCKS, NULLS, OTHER, SEVEN, UPDATES, LINES, MLINES, MPOINTS,
            MPOLYGONS, POINTS, POLYGONS
        };

    /**
     * List of wfs 1.1 type names.
     */
    public static QName[] WFS11_TYPENAMES = new QName[] {
            PRIMITIVEGEOFEATURE, AGGREGATEGEOFEATURE, /* ENTIT\u00C9G\u00C9N\u00C9RIQUE */
        };

    /** the base of the data directory */
    File data;

    /** the 'featureTypes' directory, under 'data' */
    File featureTypes;

    /** the 'styles' directory, under 'data' */
    File styles;

    /** the 'plugIns' directory under 'data */
    File plugIns;

    /** the 'validation' directory under 'data */
    File validation;

    /** the 'templates' director under 'data' */
    File templates;

    /**
     * @param base
     *            Base of the GeoServer data directory.
     *
     * @throws IOException
     */
    public MockData() throws IOException {
        data = File.createTempFile("mock", "data");
    }

    /**
     * @return The root of the data directory.
     */
    public File getDataDirectoryRoot() {
        return data;
    }

    /**
     * @return the "featureTypes" directory under the root
     */
    public File getFeatureTypesDirectory() {
        return featureTypes;
    }
    
    /**
     * Copies some content to a file under the base of the data directory.
     * <p>
     * The <code>location</code> is considred to be a path relative to the
     * data directory root.
     * </p>
     * <p>
     * Note that the resulting file will be deleted when {@link #tearDown()}
     * is called.
     * </p>
     * @param input The content to copy.
     * @param location A relative path
     */
    public void copyTo(InputStream input, String location)
        throws IOException {
        copy(input, new File(getDataDirectoryRoot(), location));
    }
    
    /**
     * Copies some content to a file udner a specific feature type directory 
     * of the data directory.
     * Example:
     * <p>
     *  <code>
     *    dd.copyToFeautreTypeDirectory(input,MockData.PrimitiveGeoFeature,"info.xml");
     *  </code>
     * </p>
     * @param input The content to copy.
     * @param featureTypeName The name of the feature type.
     * @param location The resulting location to copy to relative to the 
     *  feautre type directory.
     */
    public void copyToFeatureTypeDirectory(InputStream input, QName featureTypeName, String location )
        throws IOException {
        
        copyTo(input, "featureTypes" + File.separator + featureTypeName.getPrefix() 
                + "_" + featureTypeName.getLocalPart() + File.separator + location );
    }
    
    /**
     * Adds a new feature type.
     * 
     */
    public void addFeatureType( QName name ) throws IOException {
        //create a dummy properties file
        File directory = new File(data, name.getPrefix());
        if ( !directory.exists() ) {
            directory.mkdir();    
        }
        
        File f = new File(directory, name.getLocalPart() + ".properties");
        f.createNewFile();
        
        BufferedWriter w = new BufferedWriter( new FileWriter( f ) );
        w.write( "_=" );
        w.flush();
        w.close();
        
        //set up the info directory
        info( name );
    }
    
    public void setUp() throws IOException {
        setUp(TYPENAMES);
    }

    /**
     * Sets up the data directory, creating all the necessary files.
     *
     * @throws IOException
     */
    public void setUp(QName[] typeNames) throws IOException {
        data.delete();
        data.mkdir();

        // create a featureTypes directory
        featureTypes = new File(data, "featureTypes");
        featureTypes.mkdir();

        // create the styles directory
        styles = new File(data, "styles");
        styles.mkdir();
        //copy over the minimal style
        copy(MockData.class.getResourceAsStream("Default.sld"), new File(styles, "Default.sld"));

        //plugins
        plugIns = new File(data, "plugIns");
        plugIns.mkdir();

        //validation
        validation = new File(data, "validation");
        validation.mkdir();

        //templates
        templates = new File(data, "templates");
        templates.mkdir();

        //set up the types
        for (int i = 0; i < typeNames.length; i++) {
            setup(typeNames[i]);
        }

        // create the catalog.xml
        CatalogWriter writer = new CatalogWriter();

        // set up the datastores
        HashMap dataStores = new HashMap();

        HashMap params = new HashMap();
        params.put(PropertyDataStoreFactory.DIRECTORY.key, new File(data, CITE_PREFIX));
        params.put(PropertyDataStoreFactory.NAMESPACE.key, CITE_URI);
        dataStores.put(CITE_PREFIX, params);

        params = new HashMap();
        params.put(PropertyDataStoreFactory.DIRECTORY.key, new File(data, CDF_PREFIX));
        params.put(PropertyDataStoreFactory.NAMESPACE.key, CDF_URI);
        dataStores.put(CDF_PREFIX, params);

        params = new HashMap();
        params.put(PropertyDataStoreFactory.DIRECTORY.key, new File(data, CGF_PREFIX));
        params.put(PropertyDataStoreFactory.NAMESPACE.key, CGF_URI);
        dataStores.put(CGF_PREFIX, params);

        params = new HashMap();
        params.put(PropertyDataStoreFactory.DIRECTORY.key, new File(data, SF_PREFIX));
        params.put(PropertyDataStoreFactory.NAMESPACE.key, SF_URI);
        dataStores.put(SF_PREFIX, params);

        HashMap dataStoreNamepaces = new HashMap();
        dataStoreNamepaces.put(CITE_PREFIX, CITE_PREFIX);
        dataStoreNamepaces.put(CDF_PREFIX, CDF_PREFIX);
        dataStoreNamepaces.put(CGF_PREFIX, CGF_PREFIX);
        dataStoreNamepaces.put(SF_PREFIX, SF_PREFIX);

        writer.dataStores(dataStores, dataStoreNamepaces);

        // setup the namespaces
        HashMap namespaces = new HashMap();
        namespaces.put(CITE_PREFIX, CITE_URI);
        namespaces.put(CDF_PREFIX, CDF_URI);
        namespaces.put(CGF_PREFIX, CGF_URI);
        namespaces.put(SF_PREFIX, SF_URI);
        namespaces.put(DEFAULT_PREFIX, DEFAULT_URI);
        namespaces.put("", DEFAULT_URI);

        writer.namespaces(namespaces);

        // styles
        HashMap styles = new HashMap();

        List typeList = Arrays.asList(typeNames);
        for (int i = 0; i < WMS_TYPENAMES.length; i++) {
            if(typeList.contains(WMS_TYPENAMES[i])) {
                QName type = WMS_TYPENAMES[i];
                styles.put(type.getLocalPart(), type.getLocalPart() + ".sld");
            }
        }

        styles.put("Default", "Default.sld");

        writer.styles(styles);

        writer.write(new File(data, "catalog.xml"));
    }

    void setup(QName type) throws IOException {
        properties(type);
        info(type);
        style(type);
    }

    void properties(QName name) throws IOException {
        // copy over the properties file
        InputStream from = MockData.class.getResourceAsStream(name.getLocalPart() + ".properties");
        
        File directory = new File(data, name.getPrefix());
        directory.mkdir();

        File to = new File(directory, name.getLocalPart() + ".properties");
        copy(from, to);     
    }

    void copy(InputStream from, File to) throws IOException {
        OutputStream out = new FileOutputStream(to);

        byte[] buffer = new byte[4096];
        int bytes = 0;
        while ((bytes = from.read(buffer)) != -1)
            out.write(buffer, 0, bytes);

        from.close();
        out.flush();
        out.close();
    }

    void info(QName name) throws IOException {
        String type = name.getLocalPart();
        String prefix = name.getPrefix();

        File featureTypeDir = new File(featureTypes, prefix + "_" + type);
        featureTypeDir.mkdir();

        File info = new File(featureTypeDir, "info.xml");
        info.createNewFile();

        FileWriter writer = new FileWriter(info);
        writer.write("<featureType datastore=\"" + prefix + "\">");
        writer.write("<name>" + type + "</name>");
        writer.write("<SRS>4326</SRS>");
        // this mock type may have wrong SRS compared to the actual one in the property files...
        // let's configure SRS handling not to alter the original one, and have 4326 used only
        // for capabilities
        writer.write("<SRSHandling>2</SRSHandling>");
        writer.write("<title>" + type + "</title>");
        writer.write("<abstract>abstract about " + type + "</abstract>");
        writer.write("<numDecimals value=\"8\"/>");
        writer.write("<keywords>" + type + "</keywords>");
        writer.write(
            "<latLonBoundingBox dynamic=\"false\" minx=\"-180\" miny=\"-90\" maxx=\"180\" maxy=\"90\"/>");

        if (MockData.class.getResource(type + ".sld") != null) {
            writer.write("<styles default=\"" + type + "\"/>");
        } else {
            writer.write("<styles default=\"Default\"/>");
        }

        writer.write("</featureType>");

        writer.flush();
        writer.close();
    }

    void style(QName name) throws IOException {
        String type = name.getLocalPart();

        //if there is not  astyle named "type".sld, use minimal
        InputStream from = null;

        if (MockData.class.getResource(type + ".sld") != null) {
            from = MockData.class.getResourceAsStream(type + ".sld");
        } else {
            from = MockData.class.getResourceAsStream("Default.sld");
        }

        File to = new File(styles, type + ".sld");
        copy(from, to);
    }

    /**
     * Kills the data directory, deleting all the files.
     *
     * @throws IOException
     */
    public void tearDown() throws IOException {
        delete(templates);
        delete(validation);
        delete(plugIns);
        delete(styles);
        delete(featureTypes);
        delete(data);

        styles = null;
        featureTypes = null;
        data = null;
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
