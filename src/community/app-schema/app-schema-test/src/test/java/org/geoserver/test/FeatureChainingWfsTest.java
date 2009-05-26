/*
 * Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import junit.framework.Test;

import org.geotools.data.complex.AppSchemaDataAccess;
import org.w3c.dom.Document;

/**
 * WFS GetFeature to test integration of {@link AppSchemaDataAccess} with GeoServer.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class FeatureChainingWfsTest extends AbstractAppSchemaWfsTestSupport {

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        return new OneTimeTestSetup(new FeatureChainingWfsTest());
    }

    @Override
    protected NamespaceTestData buildTestData() {
        return new FeatureChainingMockData();
    }

    /**
     * Test whether GetCapabilities returns wfs:WFS_Capabilities.
     * 
     * @throws Exception
     */
    public void testGetCapabilities() throws Exception {
        Document doc = getAsDOM("wfs?request=GetCapabilities");
        LOGGER.info("WFS GetCapabilities response:\n" + prettyString(doc));
        assertEquals("wfs:WFS_Capabilities", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test whether DescribeFeatureType returns xsd:schema.
     * 
     * @throws Exception
     */
    public void testDescribeFeatureType() throws Exception {
        Document doc = getAsDOM("wfs?request=DescribeFeatureType&typename=gsml:MappedFeature");
        LOGGER.info("WFS DescribeFeatureType response:\n" + prettyString(doc));
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test whether GetFeature returns wfs:FeatureCollection.
     * 
     * @throws Exception
     */
    public void testGetFeature() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:MappedFeature");
        LOGGER.info("WFS GetFeature response:\n" + prettyString(doc));
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test nesting features of complex types with simple content. Previously the nested features
     * attributes weren't encoded, so this is to ensure that this works.
     * 
     * @throws Exception
     */
    public void testComplexTypeWithSimpleContent() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=ex:ParentFeature");
        LOGGER.info("WFS GetFeature response:\n" + prettyString(doc));
        assertXpathCount(2, "//ex:ParentFeature", doc);

        // 1
        assertXpathCount(3, "//ex:ParentFeature[@gml:id='1']/ex:nestedFeature", doc);
        assertXpathEvaluatesTo(
                "name_a",
                "//ex:ParentFeature[@gml:id='1']/ex:nestedFeature[1]/ex:SimpleContent/ex:someAttribute",
                doc);
        assertXpathEvaluatesTo(
                "name_b",
                "//ex:ParentFeature[@gml:id='1']/ex:nestedFeature[2]/ex:SimpleContent/ex:someAttribute",
                doc);
        assertXpathEvaluatesTo(
                "name_c",
                "//ex:ParentFeature[@gml:id='1']/ex:nestedFeature[3]/ex:SimpleContent/ex:someAttribute",
                doc);
        // 2
        assertXpathCount(1, "//ex:ParentFeature[@gml:id='2']/ex:nestedFeature", doc);
        assertXpathEvaluatesTo(
                "name_2",
                "//ex:ParentFeature[@gml:id='2']/ex:nestedFeature/ex:SimpleContent/ex:someAttribute",
                doc);
    }

    /**
     * Test content of GetFeature response.
     * 
     * @throws Exception
     */
    public void testGetFeatureContent() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:MappedFeature");

        assertXpathCount(4, "//gsml:MappedFeature", doc);

        // mf1
        assertXpathEvaluatesTo("GUNTHORPE FORMATION",
                "//gsml:MappedFeature[@gml:id='mf1']/gml:name", doc);
        assertXpathEvaluatesTo("-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:shape//gml:posList", doc);
        // gu.25699
        assertXpathEvaluatesTo("gu.25699", "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/@gml:id", doc);
        assertXpathEvaluatesTo("Yaugher Volcanic Group",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:name", doc);
        assertXpathMatches(".*Olivine basalt.*microgabbro.*",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:description", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor", doc);
        assertXpathEvaluatesTo("Blue", "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor" + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter", doc);
        assertXpathEvaluatesTo("x", "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition", doc);
        assertXpathEvaluatesTo("significant",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathEvaluatesTo(
                "interbedded component",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition" + "/gsml:CompositionPart/gsml:role",
                doc);
        // check occurence as xlink:href
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:occurence", doc);
        assertXpathEvaluatesTo("", "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit"
                + "/gsml:occurence[@xlink:href='urn:cgi:feature:MappedFeature:mf1']", doc);

        // mf2
        assertXpathEvaluatesTo("MERCIA MUDSTONE GROUP",
                "//gsml:MappedFeature[@gml:id='mf2']/gml:name", doc);
        assertXpathEvaluatesTo("-1.3 52.5 -1.3 52.6 -1.2 52.6 -1.2 52.5 -1.3 52.5",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:shape//gml:posList", doc);
        // gu.25678
        assertXpathEvaluatesTo("gu.25678", "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/@gml:id", doc);
        assertXpathEvaluatesTo("Yaugher Volcanic Group",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:name", doc);
        assertXpathMatches(".*Olivine basalt.*microgabbro.*",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:description", doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor", doc);
        assertXpathEvaluatesTo("Yellow", "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor[1]" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathEvaluatesTo("Blue", "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor[2]" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter", doc);
        assertXpathEvaluatesTo("y", "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter[1]" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathEvaluatesTo("x", "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter[2]" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition", doc);
        assertXpathEvaluatesTo("significant",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[1]"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathEvaluatesTo("interbedded component",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit[@gml:id='gu.25678']/gsml:composition[1]"
                        + "/gsml:CompositionPart/gsml:role", doc);
        assertXpathEvaluatesTo("minor", "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition[2]"
                + "/gsml:CompositionPart/gsml:proportion" + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathEvaluatesTo("interbedded component",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[2]"
                        + "/gsml:CompositionPart/gsml:role", doc);
        // check occurence as xlink:href
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:occurence", doc);
        assertXpathEvaluatesTo("", "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit"
                + "/gsml:occurence[@xlink:href='urn:cgi:feature:MappedFeature:mf2']", doc);
        assertXpathEvaluatesTo("", "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit"
                + "/gsml:occurence[@xlink:href='urn:cgi:feature:MappedFeature:mf3']", doc);

        // mf3
        assertXpathEvaluatesTo("CLIFTON FORMATION", "//gsml:MappedFeature[@gml:id='mf3']/gml:name",
                doc);
        assertXpathEvaluatesTo("-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:shape//gml:posList", doc);
        // gu.25678
        assertXpathEvaluatesTo("gu.25678", "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/@gml:id", doc);
        assertXpathEvaluatesTo("Yaugher Volcanic Group",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:name", doc);
        assertXpathMatches(".*Olivine basalt.*microgabbro.*",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:description", doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor", doc);
        assertXpathEvaluatesTo("Yellow", "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor[1]" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathEvaluatesTo("Blue", "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor[2]" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter", doc);
        assertXpathEvaluatesTo("y", "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter[1]" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathEvaluatesTo("x", "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter[2]" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition", doc);
        assertXpathEvaluatesTo("significant",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[1]"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathEvaluatesTo("interbedded component",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[1]"
                        + "/gsml:CompositionPart/gsml:role", doc);
        assertXpathEvaluatesTo("minor", "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition[2]"
                + "/gsml:CompositionPart/gsml:proportion" + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathEvaluatesTo("interbedded component",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[2]"
                        + "/gsml:CompositionPart/gsml:role", doc);
        // check occurence as xlink:href
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:occurence", doc);
        assertXpathEvaluatesTo("", "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit"
                + "/gsml:occurence[@xlink:href='urn:cgi:feature:MappedFeature:mf2']", doc);
        assertXpathEvaluatesTo("", "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit"
                + "/gsml:occurence[@xlink:href='urn:cgi:feature:MappedFeature:mf3']", doc);

        // mf4
        assertXpathEvaluatesTo("MURRADUC BASALT", "//gsml:MappedFeature[@gml:id='mf4']/gml:name",
                doc);
        assertXpathEvaluatesTo("-1.3 52.5 -1.3 52.6 -1.2 52.6 -1.2 52.5 -1.3 52.5",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:shape//gml:posList", doc);
        // gu.25682
        assertXpathEvaluatesTo("gu.25682", "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/@gml:id", doc);
        assertXpathEvaluatesTo("New Group",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:name", doc);
        assertXpathMatches(".*Olivine basalt.*",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:description", doc);
        assertXpathNotMatches(".*microgabbro.*",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:description", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor", doc);
        assertXpathEvaluatesTo("Red", "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor" + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter", doc);
        assertXpathEvaluatesTo("z", "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter" + "/gsml:CGI_TermValue/gsml:value",
                doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition", doc);
        assertXpathEvaluatesTo("significant",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathEvaluatesTo(
                "interbedded component",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition" + "/gsml:CompositionPart/gsml:role",
                doc);
        // check occurence as xlink:href
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:occurence", doc);
        assertXpathEvaluatesTo("", "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit"
                + "/gsml:occurence[@xlink:href='urn:cgi:feature:MappedFeature:mf4']", doc);

    }

}
