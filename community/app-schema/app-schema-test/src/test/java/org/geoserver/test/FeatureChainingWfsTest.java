/*
 * Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geotools.data.complex.AppSchemaDataAccess;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * WFS GetFeature to test integration of {@link AppSchemaDataAccess} with GeoServer.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class FeatureChainingWfsTest extends FeatureChainingTestSupport {

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        return new OneTimeTestSetup(new FeatureChainingWfsTest());
    }

    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();

        // Setup XMLUnit namespaces
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("ows", "http://www.opengis.net/ows");
        namespaces.put("ogc", "http://www.opengis.net/ogc");
        namespaces.put("xs", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("xsd", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put(FeatureChainingMockData.GSML_NAMESPACE_PREFIX,
                FeatureChainingMockData.GSML_NAMESPACE_URI);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
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
     * Test content of GetFeature response.
     * 
     * @throws Exception
     */
    public void testGetFeatureContent() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=gsml:MappedFeature");

        assertXpathCount(4, "//gsml:MappedFeature", doc);

        // mf1
        XMLAssert.assertXpathEvaluatesTo("GUNTHORPE FORMATION",
                "//gsml:MappedFeature[@gml:id='mf1']/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:shape//gml:posList", doc);
        XMLAssert.assertXpathEvaluatesTo("gu.25699",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/@gml:id", doc);
        XMLAssert.assertXpathEvaluatesTo("Yaugher Volcanic Group",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:name", doc);
        assertXpathMatches(".*Olivine basalt.*microgabbro.*",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:description", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor", doc);
        XMLAssert.assertXpathEvaluatesTo("Blue",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:exposureColor"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter", doc);
        XMLAssert.assertXpathEvaluatesTo("x",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:outcropCharacter"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition", doc);
        XMLAssert.assertXpathEvaluatesTo("significant",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert
                .assertXpathEvaluatesTo("interbedded component",
                        "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification"
                                + "/gsml:GeologicUnit/gsml:composition"
                                + "/gsml:CompositionPart/gsml:role", doc);

        // mf2
        XMLAssert.assertXpathEvaluatesTo("MERCIA MUDSTONE GROUP",
                "//gsml:MappedFeature[@gml:id='mf2']/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("-1.3 52.5 -1.3 52.6 -1.2 52.6 -1.2 52.5 -1.3 52.5",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:shape//gml:posList", doc);
        XMLAssert.assertXpathEvaluatesTo("gu.25678",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/@gml:id", doc);
        XMLAssert.assertXpathEvaluatesTo("Yaugher Volcanic Group",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:name", doc);
        assertXpathMatches(".*Olivine basalt.*microgabbro.*",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:description", doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor", doc);
        XMLAssert.assertXpathEvaluatesTo("Yellow",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:exposureColor[1]"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert.assertXpathEvaluatesTo("Blue",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:exposureColor[2]"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter", doc);
        XMLAssert.assertXpathEvaluatesTo("y",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:outcropCharacter[1]"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert.assertXpathEvaluatesTo("x",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:outcropCharacter[2]"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition", doc);
        XMLAssert.assertXpathEvaluatesTo("significant",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[1]"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert.assertXpathEvaluatesTo("interbedded component",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit[@gml:id='gu.25678']/gsml:composition[1]"
                        + "/gsml:CompositionPart/gsml:role", doc);
        XMLAssert.assertXpathEvaluatesTo("minor",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[2]"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert.assertXpathEvaluatesTo("interbedded component",
                "//gsml:MappedFeature[@gml:id='mf2']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[2]"
                        + "/gsml:CompositionPart/gsml:role", doc);

        // mf3
        XMLAssert.assertXpathEvaluatesTo("CLIFTON FORMATION",
                "//gsml:MappedFeature[@gml:id='mf3']/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:shape//gml:posList", doc);
        XMLAssert.assertXpathEvaluatesTo("gu.25678",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/@gml:id", doc);
        XMLAssert.assertXpathEvaluatesTo("Yaugher Volcanic Group",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:name", doc);
        assertXpathMatches(".*Olivine basalt.*microgabbro.*",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gml:description", doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:exposureColor", doc);
        XMLAssert.assertXpathEvaluatesTo("Yellow",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:exposureColor[1]"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert.assertXpathEvaluatesTo("Blue",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:exposureColor[2]"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter", doc);
        XMLAssert.assertXpathEvaluatesTo("y",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:outcropCharacter[1]"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert.assertXpathEvaluatesTo("x",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:outcropCharacter[2]"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(2, "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition", doc);
        XMLAssert.assertXpathEvaluatesTo("significant",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[1]"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert.assertXpathEvaluatesTo("interbedded component",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[1]"
                        + "/gsml:CompositionPart/gsml:role", doc);
        XMLAssert.assertXpathEvaluatesTo("minor",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[2]"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert.assertXpathEvaluatesTo("interbedded component",
                "//gsml:MappedFeature[@gml:id='mf3']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition[2]"
                        + "/gsml:CompositionPart/gsml:role", doc);

        // mf4
        XMLAssert.assertXpathEvaluatesTo("MURRADUC BASALT",
                "//gsml:MappedFeature[@gml:id='mf4']/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("-1.3 52.5 -1.3 52.6 -1.2 52.6 -1.2 52.5 -1.3 52.5",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:shape//gml:posList", doc);
        XMLAssert.assertXpathEvaluatesTo("gu.25682",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/@gml:id", doc);
        XMLAssert.assertXpathEvaluatesTo("New Group",
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
        XMLAssert.assertXpathEvaluatesTo("Red",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:exposureColor"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:outcropCharacter", doc);
        XMLAssert.assertXpathEvaluatesTo("z",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:outcropCharacter"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        assertXpathCount(1, "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                + "/gsml:GeologicUnit/gsml:composition", doc);
        XMLAssert.assertXpathEvaluatesTo("significant",
                "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                        + "/gsml:GeologicUnit/gsml:composition"
                        + "/gsml:CompositionPart/gsml:proportion"
                        + "/gsml:CGI_TermValue/gsml:value", doc);
        XMLAssert
                .assertXpathEvaluatesTo("interbedded component",
                        "//gsml:MappedFeature[@gml:id='mf4']/gsml:specification"
                                + "/gsml:GeologicUnit/gsml:composition"
                                + "/gsml:CompositionPart/gsml:role", doc);

    }

    /**
     * Assert that there are count matches of xpath in doc.
     * 
     * @param count
     *            expected number of matches
     * @param xpath
     *            xpath expression
     * @param doc
     *            document under test
     * @throws Exception
     */
    public void assertXpathCount(int count, String xpath, Document doc) throws Exception {
        XpathEngine engine = XMLUnit.newXpathEngine();
        NodeList nodes = engine.getMatchingNodes(xpath, doc);
        assertEquals(count, nodes.getLength());
    }

    /**
     * Assert that the xpath string value matches the regex.
     * 
     * @param regex
     *            regular expression that must be matched
     * @param xpath
     *            xpath expression
     * @param doc
     *            document under test
     * @throws Exception
     */
    public void assertXpathMatches(String regex, String xpath, Document doc) throws Exception {
        XpathEngine engine = XMLUnit.newXpathEngine();
        String value = engine.evaluate(xpath, doc);
        assertTrue(value.matches(regex));
    }

    /**
     * Assert that the xpath string value does not match the regex.
     * 
     * @param regex
     *            regular expression that must not be matched
     * @param xpath
     *            xpath expression
     * @param doc
     *            document under test
     * @throws Exception
     */
    public void assertXpathNotMatches(String regex, String xpath, Document doc) throws Exception {
        XpathEngine engine = XMLUnit.newXpathEngine();
        String value = engine.evaluate(xpath, doc);
        assertFalse(value.matches(regex));
    }

    /**
     * Return {@link Document} as a pretty-printed string.
     * 
     * @param doc
     * @return
     * @throws Exception
     */
    public String prettyString(Document doc) throws Exception {
        OutputStream out = new ByteArrayOutputStream();
        prettyPrint(doc, out);
        return out.toString();
    }

    /**
     * Pretty-print a {@link Document} to an {@link OutputStream}.
     * 
     * @param doc
     * @param out
     * @throws Exception
     */
    public void prettyPrint(Document doc, OutputStream out) throws Exception {
        OutputFormat format = new OutputFormat(doc);
        format.setLineWidth(80);
        format.setIndenting(true);
        format.setIndent(4);
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(doc);
    }

}
