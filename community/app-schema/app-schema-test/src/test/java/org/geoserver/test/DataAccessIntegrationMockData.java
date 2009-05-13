package org.geoserver.test;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.geoserver.data.test.MockData;
import org.geoserver.data.test.TestData;
import org.geoserver.data.util.IOUtils;
import org.geotools.data.DataAccessFinder;

/**
 * Mock data for DataAccessIntegrationWfsTest.
 * {@link org.geoserver.test.DataAccessIntegrationWfsTest} Adapted from FeatureChainingMockData.
 * 
 * @author Rini Angreani, Curtin University of Technology
 */
@SuppressWarnings("serial")
public class DataAccessIntegrationMockData implements TestData {
    /**
     * GeoSciml prefix
     */
    public static final String GSML_PREFIX = "gsml";

    /**
     * Mineral Occurrence prefix
     */
    public static final String MO_PREFIX = "mo";

    /**
     * GeoSciml URI
     */
    public static final String GSML_URI = "http://www.cgi-iugs.org/xml/GeoSciML/2";

    /**
     * Mineral Occurrence URI
     */
    public static final String MO_URI = "urn:cgi:xmlns:GGIC:MineralOccurrence:1.0";

    /**
     * Map of data store name to data store connection parameters map.
     */
    public final Map<String, Map<String, Serializable>> datastoreParams = new LinkedHashMap<String, Map<String, Serializable>>();

    /**
     * Map of data store name to namespace prefix.
     */
    public final Map<String, String> datastoreNamespacePrefixes = new LinkedHashMap<String, String>();

    /**
     * Test target root directory
     */
    private File data;

    /**
     * Feature types directory
     */
    private File featureTypesBaseDir;

    /**
     * Map of namespace prefix to namespace URI.
     */
    public static final Map<String, String> NAMESPACES = new LinkedHashMap<String, String>() {
        {
            put(GSML_PREFIX, GSML_URI);
            put(MO_PREFIX, MO_URI);
        }
    };

    /**
     * Return the root of mock data directory
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

        data = IOUtils.createRandomDirectory("./target", "data-access-integration-mock", "data");
        data.delete();
        data.mkdir();

        // create a featureTypes directory
        featureTypesBaseDir = new File(data, "featureTypes");
        featureTypesBaseDir.mkdir();

        FeatureChainingMockData.addFeatureType(GSML_PREFIX, "MappedFeature",
                "MappedFeatureAsOccurrence.xml", "MappedFeaturePropertyfile.properties", data,
                featureTypesBaseDir, datastoreParams, datastoreNamespacePrefixes);
        // GeologicUnit is the output type with a mock MO:EarthResource data access as an
        // input
        FeatureChainingMockData.addFeatureType(GSML_PREFIX, "GeologicUnit",
                "EarthResourceToGeologicUnit.xml", "EarthResource.properties", data,
                featureTypesBaseDir, datastoreParams, datastoreNamespacePrefixes);
        FeatureChainingMockData.addFeatureType(GSML_PREFIX, "CompositionPart",
                "CompositionPart.xml", "CompositionPart.properties", data, featureTypesBaseDir,
                datastoreParams, datastoreNamespacePrefixes);
        FeatureChainingMockData.addFeatureType(GSML_PREFIX, "CGI_TermValue", "CGITermValue.xml",
                "CGITermValue.properties", data, featureTypesBaseDir, datastoreParams,
                datastoreNamespacePrefixes);
        FeatureChainingMockData.addFeatureType(GSML_PREFIX, "ControlledConcept",
                "ControlledConcept.xml", "ControlledConcept.properties", data, featureTypesBaseDir,
                datastoreParams, datastoreNamespacePrefixes);

        FeatureChainingMockData.setUpCatalog(datastoreParams, datastoreNamespacePrefixes,
                NAMESPACES, data);
        FeatureChainingMockData.copyTo(MockData.class.getResourceAsStream("services.xml"),
                "services.xml", data);

        // create mock minOcc data access which is a non app-schema type
        // this comes from GeoTools - DataAccessIntegrationTest class
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("dbtype", "mo-data-access");
        params.put("directory", new URL(featureTypesBaseDir.toURL().toString()
                + FeatureChainingMockData.getDataStoreName(GSML_PREFIX, "GeologicUnit")));
        DataAccessFinder.getDataStore(params);
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

}
