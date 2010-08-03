/*
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import junit.framework.Test;

import org.w3c.dom.Document;

/**
 * Validation testing with GeoServer
 * 
 * @author Victor Tey, CSIRO Exploration and Mining
 * 
 */
public class ValidationTest extends AbstractAppSchemaWfsTestSupport {

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        return new OneTimeTestSetup(new ValidationTest());
    }

    @Override
    protected NamespaceTestData buildTestData() {
        return new ValidationTestMockData();
    }

    /**
     * Test that when minOccur=0 the validation should let it pass
     */
    public void testAttributeMinOccur0() {
        Document doc = null;
        doc = getAsDOM("wfs?request=GetFeature&typename=gsml:GeologicUnit");
        LOGGER.info("WFS GetFeature&typename=gsml:GeologicUnit response:\n" + prettyString(doc));
        assertXpathCount(1, "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.1']/gml:name", doc);
        assertXpathCount(1, "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.2']/gml:name", doc);
        assertXpathCount(1, "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.3']/gml:name", doc);

        assertXpathCount(
                1,
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.1']/gsml:bodyMorphology/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathEvaluatesTo(
                "myBody1",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.1']/gsml:bodyMorphology/gsml:CGI_TermValue/gsml:value[@codeSpace='myBodyCodespace1']",
                doc);
        assertXpathEvaluatesTo(
                "compositionName",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.1']/gsml:composition/gsml:CompositionPart/gsml:lithology[1]/gsml:ControlledConcept/gml:name",
                doc);
        assertXpathEvaluatesTo(
                "myBody1",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.1']/gsml:composition/gsml:CompositionPart/gsml:lithology[2]/gsml:ControlledConcept/gml:name",
                doc);
        assertXpathEvaluatesTo(
                "myBody1",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.1']/gsml:rank[@codeSpace='myBodyCodespace1']",
                doc);

        assertXpathCount(
                0,
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.2']/gsml:bodyMorphology/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathEvaluatesTo(
                "compositionName",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.2']/gsml:composition/gsml:CompositionPart/gsml:lithology[1]/gsml:ControlledConcept/gml:name",
                doc);
        assertXpathCount(
                0,
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.2']/gsml:composition/gsml:CompositionPart/gsml:lithology[2]/gsml:ControlledConcept/gml:name",
                doc);
        assertXpathCount(0, "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.2']/gsml:rank", doc);

        assertXpathCount(
                1,
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.3']/gsml:bodyMorphology/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathEvaluatesTo(
                "myBody3",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.3']/gsml:bodyMorphology/gsml:CGI_TermValue/gsml:value[@codeSpace='myBodyCodespace3']",
                doc);
        assertXpathEvaluatesTo(
                "compositionName",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.3']/gsml:composition/gsml:CompositionPart/gsml:lithology[1]/gsml:ControlledConcept/gml:name",
                doc);
        assertXpathEvaluatesTo(
                "myBody3",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.3']/gsml:composition/gsml:CompositionPart/gsml:lithology[2]/gsml:ControlledConcept/gml:name",
                doc);

        assertXpathEvaluatesTo(
                "myBody3",
                "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.3']/gsml:rank[@codeSpace='myBodyCodespace3']",
                doc);
    }

    public void testSimpleContentInteger() {
        Document doc = null;
        doc = getAsDOM("wfs?request=GetFeature&typename=er:Commodity");
        LOGGER.info("WFS GetFeature&typename=er:Commodity response:\n" + prettyString(doc));
        assertXpathCount(1, "//er:Commodity[@gml:id='er.commodity.gu.1']/gml:name", doc);
        assertXpathCount(1, "//er:Commodity[@gml:id='er.commodity.gu.1']/er:commodityRank", doc);
        assertXpathEvaluatesTo("myName1", "//er:Commodity[@gml:id='er.commodity.gu.1']/gml:name",
                doc);
        assertXpathEvaluatesTo("1", "//er:Commodity[@gml:id='er.commodity.gu.1']/er:commodityRank",
                doc);

        assertXpathCount(1, "//er:Commodity[@gml:id='er.commodity.gu.2']/gml:name", doc);
        assertXpathCount(0, "//er:Commodity[@gml:id='er.commodity.gu.2']/er:commodityRank", doc);
        assertXpathEvaluatesTo("myName2", "//er:Commodity[@gml:id='er.commodity.gu.2']/gml:name",
                doc);

        assertXpathCount(1, "//er:Commodity[@gml:id='er.commodity.gu.3']/gml:name", doc);
        assertXpathCount(1, "//er:Commodity[@gml:id='er.commodity.gu.3']/er:commodityRank", doc);
        assertXpathEvaluatesTo("myName3", "//er:Commodity[@gml:id='er.commodity.gu.3']/gml:name",
                doc);
        assertXpathEvaluatesTo("3", "//er:Commodity[@gml:id='er.commodity.gu.3']/er:commodityRank",
                doc);
    }

    public void testAttributeMinOccur1() {
        Document doc = null;
        try {
            doc = getAsDOM("wfs?request=GetFeature&typename=gsml:MappedFeature");
            LOGGER.info("WFS GetFeature&typename=gsml:gsml:MappedFeature response:\n"
                    + prettyString(doc));
        } catch (Exception e) {
            LOGGER
                    .info("WFS GetFeature&typename=gsml:MappedFeature response, exception expected:\n"
                            + prettyString(doc));
            assertEquals("ows:ExceptionReport", doc.getDocumentElement().getNodeName());
        }
        assertXpathCount(0, "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.1']/gml:name", doc);
        assertXpathCount(0, "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.2']/gml:name", doc);
        assertXpathCount(0, "//gsml:GeologicUnit[@gml:id='gsml.geologicunit.gu.3']/gml:name", doc);
        String exceptionRpt = evaluate("//ows:ExceptionText", doc);
        assertTrue(exceptionRpt.contains("observationMethod requires a non null value"));
    }

}
