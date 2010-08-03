/* 
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataAccessFinder;
import org.geotools.data.DataUtilities;

/**
 * Mock data for DataAccessIntegrationWfsTest.
 * {@link org.geoserver.test.DataAccessIntegrationWfsTest} Adapted from FeatureChainingMockData.
 * 
 * @author Rini Angreani, Curtin University of Technology
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class DataAccessIntegrationMockData extends AbstractAppSchemaMockData {

    /**
     * Earth Resource namespace prefix
     */
    private static final String ER_PREFIX = "er";

    /**
     * Earth Resource namespace URI
     */
    private static final String ER_URI = "urn:cgi:xmlns:GGIC:EarthResource:1.1";

    /**
     * @see org.geoserver.test.AbstractAppSchemaMockData#addContent()
     */
    @Override
    protected void addContent() {
        putNamespace(ER_PREFIX, ER_URI);
        addFeatureType(GSML_PREFIX, "MappedFeature", "MappedFeatureAsOccurrence.xml",
                "MappedFeaturePropertyfile.properties");
        // GeologicUnit is the output type with a mock MO:EarthResource data access as an
        // input
        addFeatureType(GSML_PREFIX, "GeologicUnit", "EarthResourceToGeologicUnit.xml",
                "EarthResource.properties", "CGITermValue.xml", "CGITermValue.properties",
                "exposureColor.properties", "CompositionPart.xml", "CompositionPart.properties", 
                "ControlledConcept.xml", "ControlledConcept.properties");

        // create mock Earth Resource data access which is a non app-schema type
        // this comes from GeoTools - DataAccessIntegrationTest class
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("dbtype", "er-data-access");
        try {
            params.put("directory", DataUtilities.fileToURL(new File(getFeatureTypesBaseDir(),
                    getDataStoreName(GSML_PREFIX, "GeologicUnit"))));
            // side effect is to register in AppSchemaDataAccessRegistry
            DataAccessFinder.getDataStore(params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
