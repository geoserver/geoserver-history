/* 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataAccessFinder;

/**
 * Mock data for DataAccessIntegrationWfsTest.
 * {@link org.geoserver.test.DataAccessIntegrationWfsTest} Adapted from FeatureChainingMockData.
 * 
 * @author Rini Angreani, Curtin University of Technology
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
@SuppressWarnings("serial")
public class DataAccessIntegrationMockData extends AbstractAppSchemaMockData {

    private static final String GSML_PREFIX = AbstractAppSchemaMockData.GSML_NAMESPACE_PREFIX;

    /**
     * Mineral Occurrence prefix
     */
    private static final String MO_PREFIX = "mo";

    /**
     * Mineral Occurrence URI
     */
    private static final String MO_URI = "urn:cgi:xmlns:GGIC:MineralOccurrence:1.0";

    @Override
    protected void addContent() {
        setNamespace(MO_PREFIX, MO_URI);
        addFeatureType(GSML_PREFIX, "MappedFeature", "MappedFeatureAsOccurrence.xml",
                "MappedFeaturePropertyfile.properties");
        // GeologicUnit is the output type with a mock MO:EarthResource data access as an
        // input
        addFeatureType(GSML_PREFIX, "GeologicUnit", "EarthResourceToGeologicUnit.xml",
                "EarthResource.properties");
        addFeatureType(GSML_PREFIX, "CompositionPart", "CompositionPart.xml",
                "CompositionPart.properties");
        addFeatureType(GSML_PREFIX, "CGI_TermValue", "CGITermValue.xml", "CGITermValue.properties");
        addFeatureType(GSML_PREFIX, "ControlledConcept", "ControlledConcept.xml",
                "ControlledConcept.properties");

        // create mock minOcc data access which is a non app-schema type
        // this comes from GeoTools - DataAccessIntegrationTest class
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("dbtype", "mo-data-access");
        try {
            params.put("directory", new URL(getFeatureTypesBaseDir().toURL().toString()
                    + getDataStoreName(GSML_PREFIX, "GeologicUnit")));
            // side effect is to register in AppSchemaDataAccessRegistry
            DataAccessFinder.getDataStore(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
