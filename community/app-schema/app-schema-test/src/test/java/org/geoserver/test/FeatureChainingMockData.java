/* 
 * Copyright (c) 2001 - 20089 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import org.geoserver.data.test.MockData;
import org.geotools.data.complex.AppSchemaDataAccess;

/**
 * Mock data for testing integration of {@link AppSchemaDataAccess} with GeoServer.
 * 
 * Inspired by {@link MockData}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class FeatureChainingMockData extends AbstractAppSchemaMockData {

    /**
     * Prefix for ex namespace.
     */
    protected static final String EX_PREFIX = "ex";

    /**
     * URI for ex namespace.
     */
    protected static final String EX_URI = "http://example.com";

    /**
     * @see org.geoserver.test.AbstractAppSchemaMockData#addContent()
     */
    @Override
    public void addContent() {
        putNamespace(EX_PREFIX, EX_URI);
        addFeatureType(GSML_PREFIX, "MappedFeature", "MappedFeaturePropertyfile.xml",
                "MappedFeaturePropertyfile.properties");
        addFeatureType(GSML_PREFIX, "GeologicUnit", "GeologicUnit.xml", "GeologicUnit.properties");
        addFeatureType(GSML_PREFIX, "CompositionPart", "CompositionPart.xml",
                "CompositionPart.properties");
        addFeatureType(GSML_PREFIX, "CGI_TermValue", "CGITermValue.xml", "CGITermValue.properties");
        addFeatureType(GSML_PREFIX, "ControlledConcept", "ControlledConcept.xml",
                "ControlledConcept.properties");
        // this is a mock type to test encoding complex type with simple content
        addFeatureType(EX_PREFIX, "ParentFeature", "ComplexTypeWithSimpleContent.xml",
                "ControlledConcept.properties", "simpleContent.xsd", "SimpleContent.properties");
    }

}
