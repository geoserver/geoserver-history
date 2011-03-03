/*
 * Copyright (c) 2001 - 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import junit.framework.Test;

import org.w3c.dom.Document;

/**
 * Test two WFS feature types (XSD elements) with the same XSD type.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 */
public class DuplicateTypeTest extends AbstractAppSchemaWfsTestSupport {

    public static Test suite() {
        return new OneTimeTestSetup(new DuplicateTypeTest());
    }

    @Override
    protected NamespaceTestData buildTestData() {
        return new DuplicateTypeMockData();
    }

    /**
     * Test GetFeature for gsml:DuplicateMappedFeature.
     */
    public void testGetDuplicateMappedFeature() throws Exception {
        String request = "GetFeature&typename=gsml:DuplicateMappedFeature";
        Document doc = getAsDOM("wfs?request=" + request);
        LOGGER.info("WFS " + request + " response:\n" + prettyString(doc));
        assertXpathEvaluatesTo("4", "/wfs:FeatureCollection/@numberOfFeatures", doc);
        assertXpathCount(4, "//gsml:DuplicateMappedFeature", doc);
    }

    /**
     * Test GetFeature for gsml:MappedFeature.
     */
    public void testGetMappedFeature() throws Exception {
        String request = "GetFeature&typename=gsml:MappedFeature";
        Document doc = getAsDOM("wfs?request=" + request);
        LOGGER.info("WFS " + request + " response:\n" + prettyString(doc));
        assertXpathEvaluatesTo("4", "/wfs:FeatureCollection/@numberOfFeatures", doc);
        assertXpathCount(4, "//gsml:MappedFeature", doc);
    }

}
