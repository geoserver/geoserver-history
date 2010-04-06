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
public class XlinkWfsTest extends AbstractAppSchemaWfsTestSupport {

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        return new OneTimeTestSetup(new XlinkWfsTest());
    }

    @Override
    protected NamespaceTestData buildTestData() {
        return new XlinkMockData();
    }

    /**
     * Test whether GetCapabilities returns wfs:WFS_Capabilities.
     */
    public void testGetCapabilities() {
        Document doc = getAsDOM("wfs?request=GetCapabilities");
        LOGGER.info("WFS GetCapabilities response:\n" + prettyString(doc));
        assertEquals("wfs:WFS_Capabilities", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test whether DescribeFeatureType returns xsd:schema.
     */
    public void testDescribeFeatureType() {
        Document doc = getAsDOM("wfs?request=DescribeFeatureType&typename=gsml:MappedFeature");
        LOGGER.info("WFS DescribeFeatureType response:\n" + prettyString(doc));
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test whether GetFeature returns wfs:FeatureCollection.
     */
    public void testGetFeature() {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:MappedFeature");
        LOGGER.info("WFS GetFeature response:\n" + prettyString(doc));
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test content of GetFeature response.
     */
    public void testGetFeatureContent() {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:MappedFeature");

        assertXpathCount(4, "//gsml:MappedFeature", doc);

        // mf1
        assertXpathEvaluatesTo("GUNTHORPE FORMATION",
                "//gsml:MappedFeature[@gml:id='mf1']/gml:name", doc);
        assertXpathEvaluatesTo("-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:shape//gml:posList", doc);
        assertXpathEvaluatesTo("urn:x-test:GeologicUnit:gu.25699",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification/@xlink:href", doc);

        // mf2
        assertXpathEvaluatesTo("MERCIA MUDSTONE GROUP",
                "//gsml:MappedFeature[@gml:id='mf2']/gml:name", doc);
        assertXpathEvaluatesTo("-1.3 52.5 -1.3 52.6 -1.2 52.6 -1.2 52.5 -1.3 52.5",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:shape//gml:posList", doc);
        assertXpathEvaluatesTo("urn:x-test:GeologicUnit:gu.25678",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification/@xlink:href", doc);

        // mf3
        assertXpathEvaluatesTo("CLIFTON FORMATION", "//gsml:MappedFeature[@gml:id='mf3']/gml:name",
                doc);
        assertXpathEvaluatesTo("-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:shape//gml:posList", doc);
        assertXpathEvaluatesTo("urn:x-test:GeologicUnit:gu.25678",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification/@xlink:href", doc);

        // mf4
        assertXpathEvaluatesTo("MURRADUC BASALT", "//gsml:MappedFeature[@gml:id='mf4']/gml:name",
                doc);
        assertXpathEvaluatesTo("-1.3 52.5 -1.3 52.6 -1.2 52.6 -1.2 52.5 -1.3 52.5",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:shape//gml:posList", doc);
        assertXpathEvaluatesTo("urn:x-test:GeologicUnit:gu.25682",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification/@xlink:href", doc);

    }

//    /**
//     * Test content of GeologicUnit GetFeature response, where it has multiple occurrence attributes.
//     * This is to test that automatic xlink:href is encoded when the id already exists in the document.
//     * The first 2 geologic unit features have the same occurrence of mapped features, so in
//     * the second instance, it should be encoded as an xlink:href to the mapped feature.
//     */
//    public void testXlinkAttributes() {
//        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:GeologicUnit");
//        LOGGER.info("WFS GetFeature&typename=gsml:GeologicUnit response:\n" + prettyString(doc));
//        assertXpathCount(3, "//gsml:GeologicUnit", doc);
//
//        // gu.25699
//        assertXpathCount(1, "//gsml:GeologicUnit[@gml:id='gu.25699']/gsml:occurence", doc);
//        assertXpathEvaluatesTo("mf1",
//                "//gsml:GeologicUnit[@gml:id='gu.25699']/gsml:occurence/gsml:MappedFeature/@gml:id", doc);
//        // gu.25678
//        assertXpathCount(3, "//gsml:GeologicUnit[@gml:id='gu.25678']/gsml:occurence", doc);
//        // should be an xlink:href to mf1 since it's already been encoded above
//        assertXpathEvaluatesTo("#mf1",
//                "//gsml:GeologicUnit[@gml:id='gu.25678']/gsml:occurence[1]/@xlink:href", doc);
//        // testing that nothing else is encoded apart from xlink:href
//        assertXpathCount(0, "//gsml:GeologicUnit[@gml:id='gu.25678']/gsml:occurence[1]/gsml:MappedFeature", doc);
//        assertXpathEvaluatesTo("mf2",
//                "//gsml:GeologicUnit[@gml:id='gu.25678']/gsml:occurence[2]/gsml:MappedFeature/@gml:id", doc);
//        assertXpathEvaluatesTo("mf3",
//                "//gsml:GeologicUnit[@gml:id='gu.25678']/gsml:occurence[3]/gsml:MappedFeature/@gml:id", doc);
//        // gu.25682
//        assertXpathCount(1, "//gsml:GeologicUnit[@gml:id='gu.25682']/gsml:occurence", doc);
//        assertXpathEvaluatesTo("mf4",
//                "//gsml:GeologicUnit[@gml:id='gu.25682']/gsml:occurence/gsml:MappedFeature/@gml:id", doc);
//    }
//    
//    /**
//     * In this test, gsml:MappedFeature features should be encoded as xlink:href as top level features.
//     * This is because they're already encoded inside gsml:GeologicUnit in the same document.
//     */
//    public void testTopLevelFeatures() {
//        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:GeologicUnit,gsml:MappedFeature");
//        LOGGER.info("WFS GetFeature&typename=gsml:GeologicUnit,gsml:MappedFeature response:\n" + prettyString(doc));
//        assertXpathCount(4, "//gml:featureMembers/gsml:MappedFeature", doc);
//
//        // mf1
//        assertXpathEvaluatesTo("#mf1", "//gml:featureMembers/gsml:MappedFeature[1]/@xlink:href", doc);
//        assertXpathCount(0, "//gml:featureMembers/gsml:MappedFeature[1]/@gml:id", doc);
//        assertXpathCount(0, "//gml:featureMembers/gsml:MappedFeature[1]/gml:name", doc);
//
//        // mf2
//        assertXpathEvaluatesTo("#mf2", "//gml:featureMembers/gsml:MappedFeature[2]/@xlink:href", doc);
//        assertXpathCount(0, "//gml:featureMembers/gsml:MappedFeature[2]/@gml:id", doc);
//        assertXpathCount(0, "//gml:featureMembers/gsml:MappedFeature[2]/gml:name", doc);
//
//        // mf3
//        assertXpathEvaluatesTo("#mf3", "//gml:featureMembers/gsml:MappedFeature[3]/@xlink:href", doc);
//        assertXpathCount(0, "//gml:featureMembers/gsml:MappedFeature[3]/@gml:id", doc);
//        assertXpathCount(0, "//gml:featureMembers/gsml:MappedFeature[3]/gml:name", doc);
//
//        // mf4
//        assertXpathEvaluatesTo("#mf4", "//gml:featureMembers/gsml:MappedFeature[4]/@xlink:href", doc);
//        assertXpathCount(0, "//gml:featureMembers/gsml:MappedFeature[4]/@gml:id", doc);
//        assertXpathCount(0, "//gml:featureMembers/gsml:MappedFeature[4]/gml:name", doc);
//    }
}
