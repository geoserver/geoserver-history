/* 
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.geoserver.data.CatalogWriter;
import org.geoserver.data.test.MockData;
import org.geoserver.data.test.TestData;
import org.geoserver.data.util.IOUtils;
import org.geotools.data.complex.AppSchemaDataAccess;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Mock data for testing integration of {@link AppSchemaDataAccess} with GeoServer.
 * 
 * Inspired by {@link MockData}.
 */
@SuppressWarnings("serial")
public class XlinkMockData implements TestData {

    public static final String TEST_DATA = "/test-data/";

    public static final String GSML_NAMESPACE_PREFIX = "gsml";

    public static final String OASIS_CATALOG = "mappedPolygons.oasis.xml";

    public static final String SCHEMAS_DIR = "commonSchemas_new";

    public static final String GSML_NAMESPACE_URI = "http://www.cgi-iugs.org/xml/GeoSciML/2";

    /**
     * Map of namespace prefix to namespace URI.
     */
    public static final Map<String, String> NAMESPACES = new LinkedHashMap<String, String>() {
        {
            put(GSML_NAMESPACE_PREFIX, GSML_NAMESPACE_URI);
            put("gml", "http://www.opengis.net/gml");
            put("xlink", "http://www.w3.org/1999/xlink");
            put("sa", "http://www.opengis.net/sampling/1.0");
            put("om", "http://www.opengis.net/om/1.0");
            put("cv", "http://www.opengis.net/cv/0.2.1");
            put("swe", "http://www.opengis.net/swe/1.0.1");
            put("sml", "http://www.opengis.net/sensorML/1.0.1");
        }
    };

    /**
     * Map of data store name to data store connection parameters map.
     */
    public final Map<String, Map<String, Serializable>> datastoreParams = new LinkedHashMap<String, Map<String, Serializable>>();

    /**
     * Map of data store name to namespace prefix.
     */
    public final Map<String, String> datastoreNamespacePrefixes = new LinkedHashMap<String, String>();

    /**
     * Use FeatureTypeInfo constants for srs handling as values
     */
    public static final String KEY_SRS_HANDLINGS = "srsHandling";

    /**
     * The feature type alias, a string
     */
    public static final String KEY_ALIAS = "alias";

    /**
     * The style name
     */
    public static final String KEY_STYLE = "style";

    /**
     * The srs code (a number) for this layer
     */
    public static final String KEY_SRS_NUMBER = "srs";

    /**
     * The lon/lat envelope as a JTS Envelope
     */
    public static final String KEY_LL_ENVELOPE = "ll_envelope";

    /**
     * The native envelope as a JTS Envelope
     */
    public static final String KEY_NATIVE_ENVELOPE = "native_envelope";

    static final Envelope DEFAULT_ENVELOPE = new Envelope(-180, 180, -90, 90);

    private File data;

    /** the 'featureTypes' directory, under 'data' */
    File featureTypesBaseDir;

    /**
     * Constructor. Creates the mock data directory and adds all the feature types.
     * 
     * @throws IOException
     */
    public XlinkMockData() throws Exception {
        data = IOUtils.createRandomDirectory("./target", "app-schema-mock", "data");
        data.delete();
        data.mkdir();

        // create a featureTypes directory
        featureTypesBaseDir = new File(data, "featureTypes");
        featureTypesBaseDir.mkdir();

        addFeatureType(GSML_NAMESPACE_PREFIX, "MappedFeature", "MappedFeatureXlink.xml",
                "MappedFeaturePropertyfile.properties");
    }

    /**
     * Returns the root of the mock data directory,
     * 
     * @see org.geoserver.data.test.TestData#getDataDirectoryRoot()
     */
    public File getDataDirectoryRoot() {
        return data;
    }

    /**
     * Returns true.
     * 
     * @see org.geoserver.data.test.TestData#isTestDataAvailable()
     */
    public boolean isTestDataAvailable() {
        return true;
    }

    /**
     * Configures mock data directory.
     * 
     * @see org.geoserver.data.test.TestData#setUp()
     */
    public void setUp() throws Exception {
        setUpCatalog();
        copyTo(MockData.class.getResourceAsStream("services.xml"), "services.xml");
    }

    /**
     * Removes the mock data directory.
     * 
     * @see org.geoserver.data.test.TestData#tearDown()
     */
    public void tearDown() throws Exception {
        IOUtils.delete(data);
        data = null;
    }

    /**
     * Writes catalog.xml to the data directory.
     * 
     * @throws IOException
     */
    @SuppressWarnings("serial")
    protected void setUpCatalog() throws IOException {
        CatalogWriter writer = new CatalogWriter();
        writer.dataStores(datastoreParams, datastoreNamespacePrefixes, Collections
                .<String> emptySet());
        writer.coverageStores(new HashMap<String, Map<String, String>>(),
                new HashMap<String, String>(), Collections.<String> emptySet());
        writer.namespaces(NAMESPACES);
        writer.styles(Collections.<String, String> emptyMap());
        writer.write(new File(data, "catalog.xml"));
    }

    /**
     * Copies from an {@link InputStream} to path under the mock data directory.
     * 
     * @param input
     *            source from which file content is copied
     * @param location
     *            path relative to mock data directory
     */
    public void copyTo(InputStream input, String location) throws IOException {
        IOUtils.copy(input, new File(getDataDirectoryRoot(), location));
    }

    /**
     * Write an info.xml file describing a feature type to the feature type directory.
     * 
     * <p>
     * 
     * Stolen from {@link MockData}.
     * 
     * @param namespacePrefix
     *            namespace prefix of the WFS feature type
     * @param typeName
     *            namespace prefix of the WFS feature type
     * @throws IOException
     */
    public void writeInfoFile(String namespacePrefix, String typeName) throws IOException {

        // prepare extra params default
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(KEY_STYLE, "Default");
        params.put(KEY_SRS_HANDLINGS, 2);
        params.put(KEY_ALIAS, null);

        Integer srs = 4326;

        params.put(KEY_SRS_NUMBER, srs);

        File featureTypeDir = getFeatureTypeDir(namespacePrefix, typeName);
        featureTypeDir.mkdir();

        File info = new File(featureTypeDir, "info.xml");
        info.delete();
        info.createNewFile();

        FileWriter writer = new FileWriter(info);
        writer.write("<featureType datastore=\"" + getDataStoreName(namespacePrefix, typeName)
                + "\">");
        writer.write("<name>" + typeName + "</name>");
        if (params.get(KEY_ALIAS) != null)
            writer.write("<alias>" + params.get(KEY_ALIAS) + "</alias>");
        writer.write("<SRS>" + params.get(KEY_SRS_NUMBER) + "</SRS>");
        // this mock type may have wrong SRS compared to the actual one in the property files...
        // let's configure SRS handling not to alter the original one, and have 4326 used only
        // for capabilities
        writer.write("<SRSHandling>" + params.get(KEY_SRS_HANDLINGS) + "</SRSHandling>");
        writer.write("<title>" + typeName + "</title>");
        writer.write("<abstract>abstract about " + typeName + "</abstract>");
        writer.write("<numDecimals value=\"8\"/>");
        writer.write("<keywords>" + typeName + "</keywords>");
        Envelope llEnvelope = (Envelope) params.get(KEY_LL_ENVELOPE);
        if (llEnvelope == null)
            llEnvelope = DEFAULT_ENVELOPE;
        writer.write("<latLonBoundingBox dynamic=\"false\" minx=\"" + llEnvelope.getMinX()
                + "\" miny=\"" + llEnvelope.getMinY() + "\" maxx=\"" + llEnvelope.getMaxX()
                + "\" maxy=\"" + llEnvelope.getMaxY() + "\"/>");

        Envelope nativeEnvelope = (Envelope) params.get(KEY_NATIVE_ENVELOPE);
        if (nativeEnvelope != null)
            writer.write("<nativeBBox dynamic=\"false\" minx=\"" + nativeEnvelope.getMinX()
                    + "\" miny=\"" + nativeEnvelope.getMinY() + "\" maxx=\""
                    + nativeEnvelope.getMaxX() + "\" maxy=\"" + nativeEnvelope.getMaxY() + "\"/>");

        String style = (String) params.get(KEY_STYLE);
        if (style == null)
            style = "Default";
        writer.write("<styles default=\"" + style + "\"/>");

        writer.write("</featureType>");

        writer.flush();
        writer.close();
    }

    /**
     * Build the connection parameters map for a data store.
     * 
     * @param namespacePrefix
     *            namespace prefix of the WFS feature type
     * @param typeName
     *            local name of the WFS feature type
     * @param mappingFileName
     *            file name of the app-schema mapping file
     * @return
     * @throws MalformedURLException
     */
    @SuppressWarnings("serial")
    public Map<String, Serializable> buildDatastoreParams(final String namespacePrefix,
            final String typeName, final String mappingFileName) throws MalformedURLException {
        return new LinkedHashMap<String, Serializable>() {
            {
                put("dbtype", "app-schema");
                put("url", new File(new File(featureTypesBaseDir, getDataStoreName(namespacePrefix,
                        typeName)), mappingFileName).toURI().toURL());
            }
        };
    }

    /**
     * Add one feature type, copying its resources and registering, creating its info.xml, and
     * adding it to catalog.xml.
     * 
     * @param namespacePrefix
     *            namespace prefix of the WFS feature type
     * @param typeName
     *            local name of the WFS feature type
     * @param mappingFileName
     *            file name of the app-schema mapping file
     * @param propertyFileName
     *            file name of the property file containing the data for this feature
     * @throws Exception
     */
    public void addFeatureType(String namespacePrefix, String typeName, String mappingFileName,
            String propertyFileName) throws Exception {
        writeInfoFile(namespacePrefix, typeName);
        copyMappingAndPropertyFiles(namespacePrefix, typeName, mappingFileName, propertyFileName);
        copyFileToFeatureTypeDir(namespacePrefix, typeName, OASIS_CATALOG);
        IOUtils.deepCopy(new File(getClass().getResource(TEST_DATA + SCHEMAS_DIR).toURI()),
                new File(getFeatureTypeDir(namespacePrefix, typeName), SCHEMAS_DIR));
        datastoreParams.put(getDataStoreName(namespacePrefix, typeName), buildDatastoreParams(
                namespacePrefix, typeName, mappingFileName));
        datastoreNamespacePrefixes
                .put(getDataStoreName(namespacePrefix, typeName), namespacePrefix);
    }

    /**
     * Get the name of the data store for a feature type. This is used to construct the name of the
     * feature type directory as well as the name of the data store.
     * 
     * @param namespacePrefix
     *            namespace prefix of the WFS feature type
     * @param typeName
     *            local name of the WFS feature type
     * @return name of the data store for the feature type
     */
    public String getDataStoreName(String namespacePrefix, String typeName) {
        return namespacePrefix + "_" + typeName;
    }

    /**
     * Get the file for the directory that contains the mapping and property files.
     * 
     * @param namespacePrefix
     *            namespace prefix of the WFS feature type
     * @param typeName
     *            local name of the WFS feature type
     * @return directory that contains the mapping and property files
     */
    public File getFeatureTypeDir(String namespacePrefix, String typeName) {
        return new File(featureTypesBaseDir, getDataStoreName(namespacePrefix, typeName));
    }

    /**
     * Copy the mapping and property files to the feature type directory.
     * 
     * @param namespacePrefix
     *            namespace prefix of the WFS feature type
     * @param typeName
     *            local name of the WFS feature type
     * @param mappingFileName
     *            name of the mapping file for this feature type
     * @param properyFileName
     *            name of the property file containing the the data for this feature type
     * @throws IOException
     */
    public void copyMappingAndPropertyFiles(String namespacePrefix, String typeName,
            String mappingFileName, String properyFileName) throws IOException {
        copyFileToFeatureTypeDir(namespacePrefix, typeName, mappingFileName);
        copyFileToFeatureTypeDir(namespacePrefix, typeName, properyFileName);
    }

    /**
     * Copy a file from the test-data directory to a feature type directory.
     * 
     * @param namespacePrefix
     *            namespace prefix of the WFS feature type
     * @param typeName
     *            local name of the WFS feature type
     * @param fileName
     *            short name of the file in test-data to copy
     * @throws IOException
     */
    public void copyFileToFeatureTypeDir(String namespacePrefix, String typeName, String fileName)
            throws IOException {
        copyTo(getClass().getResourceAsStream(TEST_DATA + fileName), "featureTypes" + "/"
                + getDataStoreName(namespacePrefix, typeName) + "/" + fileName);
    }

}
