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
 * @author Rob Atkinson, CSIRO
 */
public class XYGeomTest extends XYGeomTestSupport {

    /**
     * Read-only test so can use one-time setup.
     * 
     * @return
     */
    public static Test suite() {
        return new OneTimeTestSetup(new XYGeomTest());
    }

    @Override
    protected void oneTimeSetUp() throws Exception {        
        super.oneTimeSetUp();
        // Setup XMLUnit namespaces
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("ows", "http://www.opengis.net/ows");
        namespaces.put("ogc", "http://www.opengis.net/ogc");
        namespaces.put("xs", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("xsd", "http://www.w3.org/2001/XMLSchema");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put(XYGeomMockData.TEST_NAMESPACE_PREFIX,
                XYGeomMockData.TEST_NAMESPACE_URI);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    /**
     * Test whether GetCapabilities returns wfs:WFS_Capabilities.
     * 
     * @throws Exception
     */
  /*  public void testGetCapabilities() throws Exception {
        Document doc = getAsDOM("wfs?request=GetCapabilities");
        LOGGER.info("WFS GetCapabilities response:\n" + prettyString(doc));
        assertEquals("wfs:WFS_Capabilities", doc.getDocumentElement().getNodeName());
    }*/

    /**
     * Test whether DescribeFeatureType returns xsd:schema.
     * 
     * @throws Exception
     */
    public void testDescribeFeatureType() throws Exception {
        Document doc = getAsDOM("wfs?request=DescribeFeatureType&typename=test:PointFeature");
        LOGGER.info("WFS DescribeFeatureType response:\n" + prettyString(doc));
        assertEquals("xsd:schema", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test whether GetFeature returns wfs:FeatureCollection.
     * 
     * @throws Exception
     */
    public void testGetFeature() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=test:PointFeature");
        LOGGER.info("WFS GetFeature response:\n" + prettyString(doc));
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement().getNodeName());
    }

    /**
     * Test content of GetFeature response.
     * 
     * @throws Exception
     */
    public void testGetFeatureContent() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=test:PointFeature");

        assertXpathCount(2, "//test:PointFeature", doc);

  /*      // mf1
        XMLAssert.assertXpathEvaluatesTo("GUNTHORPE FORMATION",
                "//gsml:MappedFeature[@gml:id='mf1']/gml:name", doc);
        XMLAssert.assertXpathEvaluatesTo("-1.2 52.5 -1.2 52.6 -1.1 52.6 -1.1 52.5 -1.2 52.5",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:shape//gml:posList", doc);
        XMLAssert.assertXpathEvaluatesTo("urn:x-test:GeologicUnit:gu.25699",
                "//gsml:MappedFeature[@gml:id='mf1']/gsml:specification/@xlink:href", doc);

*/
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
