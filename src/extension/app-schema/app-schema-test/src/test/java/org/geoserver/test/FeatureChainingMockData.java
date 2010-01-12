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
     * Prefix for om namespace.
     */
    protected static final String OM_PREFIX = "om";

    /**
     * URI for om namespace.
     */
    protected static final String OM_URI = "http://www.opengis.net/om/1.0";

    /**
     * @see org.geoserver.test.AbstractAppSchemaMockData#addContent()
     */
    @Override
    public void addContent() {
        putNamespace(EX_PREFIX, EX_URI);
        putNamespace(OM_PREFIX, OM_URI);
        addFeatureType(GSML_PREFIX, "MappedFeature", "MappedFeaturePropertyfile.xml",
                "MappedFeaturePropertyfile.properties");
        addFeatureType(GSML_PREFIX, "GeologicUnit", "GeologicUnit.xml", "GeologicUnit.properties",
                "CGITermValue.xml", "CGITermValue.properties", "CompositionPart.xml",
                "CompositionPart.properties", "ControlledConcept.xml",
                "ControlledConcept.properties");
        // this is a mock type to test encoding complex type with simple content
        addFeatureType(EX_PREFIX, "FirstParentFeature", "ComplexTypeWithSimpleContent.xml",
                "ControlledConcept.properties", "simpleContent.xsd", "SimpleContent.properties");
        addFeatureType(EX_PREFIX, "SecondParentFeature", "ComplexTypeWithSimpleContent.xml",
                "ControlledConcept.properties", "simpleContent.xsd", "SimpleContent.properties");
        // test anyType encoding with om:result in om:observation type
        addFeatureType(OM_PREFIX, "Observation", "ObservationAnyTypeTest.xml",
                "MappedFeaturePropertyfile.properties");
    }

}
