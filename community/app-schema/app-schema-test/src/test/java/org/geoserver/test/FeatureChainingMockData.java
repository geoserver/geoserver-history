/* 
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
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


    @Override
    public void addContent() {
        addFeatureType(GSML_NAMESPACE_PREFIX, "MappedFeature", "MappedFeaturePropertyfile.xml",
                "MappedFeaturePropertyfile.properties");
        addFeatureType(GSML_NAMESPACE_PREFIX, "GeologicUnit", "GeologicUnit.xml",
                "GeologicUnit.properties");
        addFeatureType(GSML_NAMESPACE_PREFIX, "CompositionPart", "CompositionPart.xml",
                "CompositionPart.properties");
        addFeatureType(GSML_NAMESPACE_PREFIX, "CGI_TermValue", "CGITermValue.xml",
                "CGITermValue.properties");
        addFeatureType(GSML_NAMESPACE_PREFIX, "ControlledConcept", "ControlledConcept.xml",
                "ControlledConcept.properties");
        // this is a mock type to test encoding complex type with simple content
        addFeatureType("ex", "ParentFeature", "ComplexTypeWithSimpleContent.xml",
                "ControlledConcept.properties", "simpleContent.xsd", "SimpleContent.properties");
    }

}
