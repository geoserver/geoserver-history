package org.geoserver.wfs.v1_1;

import org.geoserver.wfs.WFSTestSupport;
import org.geotools.gml3.bindings.GML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GetFeatureTest extends WFSTestSupport {

    public void testGet() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen&version=1.1.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());

        NodeList features = doc.getElementsByTagName("cdf:Fifteen");
        assertFalse(features.getLength() == 0);

        for (int i = 0; i < features.getLength(); i++) {
            Element feature = (Element) features.item(i);
            assertTrue(feature.hasAttribute("gml:id"));
        }
    }

    public void testPost() throws Exception {

        String xml = "<wfs:GetFeature " + "service=\"WFS\" "
                + "version=\"1.1.0\" "
                + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" " + "> "
                + "<wfs:Query typeName=\"cdf:Other\"> "
                + "<wfs:PropertyName>cdf:string2</wfs:PropertyName> "
                + "</wfs:Query> " + "</wfs:GetFeature>";

        Document doc = postAsDOM("wfs", xml);
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());

        NodeList features = doc.getElementsByTagName("cdf:Other");
        assertFalse(features.getLength() == 0);

        for (int i = 0; i < features.getLength(); i++) {
            Element feature = (Element) features.item(i);
            assertTrue(feature.hasAttribute("gml:id"));
        }

    }

    public void testPostFormEncoded() throws Exception {
        String request = "wfs?service=WFS&version=1.1.0&request=GetFeature&typename=sf:PrimitiveGeoFeature"
                + "&namespace=xmlns(sf=http://cite.opengeospatial.org/gmlsf)";

        Document doc = postAsDOM(request);
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());

        assertEquals(5, doc.getElementsByTagName("sf:PrimitiveGeoFeature")
                .getLength());
    }

    public void testPostWithFilter() throws Exception {
        String xml = "<wfs:GetFeature " + "service=\"WFS\" "
                + "version=\"1.1.0\" "
                + "outputFormat=\"text/xml; subtype=gml/3.1.1\" "
                + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" > "
                + "<wfs:Query typeName=\"cdf:Other\"> " + "<ogc:Filter> "
                + "<ogc:PropertyIsEqualTo> "
                + "<ogc:PropertyName>cdf:integers</ogc:PropertyName> "
                + "<ogc:Add> " + "<ogc:Literal>4</ogc:Literal> "
                + "<ogc:Literal>3</ogc:Literal> " + "</ogc:Add> "
                + "</ogc:PropertyIsEqualTo> " + "</ogc:Filter> "
                + "</wfs:Query> " + "</wfs:GetFeature>";

        Document doc = postAsDOM("wfs", xml);
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());

        NodeList features = doc.getElementsByTagName("cdf:Other");
        assertFalse(features.getLength() == 0);

        for (int i = 0; i < features.getLength(); i++) {
            Element feature = (Element) features.item(i);
            assertTrue(feature.hasAttribute("gml:id"));
        }
    }

    public void testResultTypeHitsGet() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cdf:Fifteen&version=1.1.0&resultType=hits&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());

        NodeList features = doc.getElementsByTagName("cdf:Fifteen");
        assertEquals(0, features.getLength());

        assertEquals("15", doc.getDocumentElement().getAttribute(
                "numberOfFeatures"));
    }

    public void testResultTypeHitsPost() throws Exception {
        String xml = "<wfs:GetFeature " + "service=\"WFS\" "
                + "version=\"1.1.0\" "
                + "outputFormat=\"text/xml; subtype=gml/3.1.1\" "
                + "xmlns:cdf=\"http://www.opengis.net/cite/data\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "resultType=\"hits\"> "
                + "<wfs:Query typeName=\"cdf:Seven\"/> " + "</wfs:GetFeature>";

        Document doc = postAsDOM("wfs", xml);
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());

        NodeList features = doc.getElementsByTagName("cdf:Fifteen");
        assertEquals(0, features.getLength());

        assertEquals("7", doc.getDocumentElement().getAttribute(
                "numberOfFeatures"));
    }

    public void testWithSRS() throws Exception {
        String xml = "<wfs:GetFeature xmlns:wfs=\"http://www.opengis.net/wfs\" version=\"1.1.0\" service=\"WFS\">"
                + "<wfs:Query xmlns:cdf=\"http://www.opengis.net/cite/data\" typeName=\"cdf:Other\" srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\"/>"
                + "</wfs:GetFeature>";

        Document dom = postAsDOM("wfs", xml);
        assertEquals(1, dom.getElementsByTagName("cdf:Other")
                .getLength());
    }

    public void testWithSillyLiteral() throws Exception {
        String xml = "<wfs:GetFeature xmlns:cdf=\"http://www.opengis.net/cite/data\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.1.0\" service=\"WFS\">"
                + "<wfs:Query  typeName=\"cdf:Other\" srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\">"
                + "<ogc:Filter>"
                + "  <ogc:PropertyIsEqualTo>"
                + "   <ogc:PropertyName>description</ogc:PropertyName>"
                + "   <ogc:Literal>"
                + "       <wfs:Native vendorId=\"foo\" safeToIgnore=\"true\"/>"
                + "   </ogc:Literal>"
                + "   </ogc:PropertyIsEqualTo>"
                + " </ogc:Filter>" + "</wfs:Query>" + "</wfs:GetFeature>";

        Document dom = postAsDOM("wfs", xml);
        assertEquals("wfs:FeatureCollection", dom.getDocumentElement()
                .getNodeName());
        assertEquals(0, dom.getElementsByTagName("cdf:Other")
                .getLength());
    }

    public void testWithGmlObjectId() throws Exception {
        String xml = "<wfs:GetFeature xmlns:cdf=\"http://www.opengis.net/cite/data\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.1.0\" service=\"WFS\">"
                + "<wfs:Query  typeName=\"cdf:Seven\" srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\">"
                + "</wfs:Query>" + "</wfs:GetFeature>";

        Document dom = postAsDOM("wfs", xml);
        assertEquals("wfs:FeatureCollection", dom.getDocumentElement()
                .getNodeName());
        assertEquals(7, dom.getElementsByTagName("cdf:Seven")
                .getLength());

        NodeList others = dom.getElementsByTagName("cdf:Seven");
        String id = ((Element) others.item(0)).getAttributeNS(GML.NAMESPACE,
                "id");
        assertNotNull(id);

        xml = "<wfs:GetFeature xmlns:cdf=\"http://www.opengis.net/cite/data\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.1.0\" service=\"WFS\">"
                + "<wfs:Query  typeName=\"cdf:Seven\" srsName=\"urn:x-ogc:def:crs:EPSG:6.11.2:4326\">"
                + "<ogc:Filter>"
                + "<ogc:GmlObjectId gml:id=\""
                + id
                + "\"/>"
                + "</ogc:Filter>" + "</wfs:Query>" + "</wfs:GetFeature>";
        dom = postAsDOM("wfs", xml);

        assertEquals(1, dom.getElementsByTagName("cdf:Seven")
                .getLength());
    }

}
