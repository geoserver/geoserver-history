package org.geoserver.wfs;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class GeometrylessTest extends WFSTestSupport {
    
    public void testGetFeature10() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cite:Geometryless&version=1.0.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());
        print(doc);

        NodeList featureMembers = doc.getElementsByTagName("gml:featureMember");
        assertFalse(featureMembers.getLength() == 0);
        NodeList features = doc.getElementsByTagName("cite:Geometryless");
        assertEquals(3, featureMembers.getLength());
    }
    
    public void testGetFeatureReproject10() throws Exception {
        getWFS().setFeatureBounding(true);
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cite:Geometryless&version=1.0.0&service=wfs&srsName=EPSG:900913");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());
        print(doc);

        NodeList featureMembers = doc.getElementsByTagName("gml:featureMember");
        assertFalse(featureMembers.getLength() == 0);
        NodeList features = doc.getElementsByTagName("cite:Geometryless");
        assertEquals(3, featureMembers.getLength());
    }
    
    public void testGetFeature11() throws Exception {
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cite:Geometryless&version=1.1.0&service=wfs");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());
        print(doc);

        NodeList featureMembers = doc.getElementsByTagName("gml:featureMembers");
        assertFalse(featureMembers.getLength() == 0);
        NodeList features = doc.getElementsByTagName("cite:Geometryless");
        assertEquals(3, features.getLength());
    }
    
    public void testGetFeatureReproject11() throws Exception {
        getWFS().setFeatureBounding(true);
        Document doc = getAsDOM("wfs?request=GetFeature&typename=cite:Geometryless&version=1.1.0&service=wfs&srsName=EPSG:900913");
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());

        NodeList featureMembers = doc.getElementsByTagName("gml:featureMembers");
        assertFalse(featureMembers.getLength() == 0);
        NodeList features = doc.getElementsByTagName("cite:Geometryless");
        assertEquals(3, features.getLength());
    }
    
    public void testGetFeatureReprojectPost() throws Exception {
        String request = "<wfs:GetFeature service=\"WFS\" xmlns:wfs=\"http://www.opengis.net/wfs\" " +
        		"version=\"1.0.0\"  outputFormat=\"GML2\" " +
        		"xmlns:topp=\"http://www.openplans.org/topp\" " +
        		"xmlns:ogc=\"http://www.opengis.net/ogc\" " +
        		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        		"xsi:schemaLocation=\"http://www.opengis.net/wfs " +
        		"http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd\">" +
        		"<wfs:Query typeName=\"cite:Geometryless\" srsName=\"EPSG:900913\"/></wfs:GetFeature>";
        System.out.println(request);
        Document doc = postAsDOM("wfs", request);
        
        assertEquals("wfs:FeatureCollection", doc.getDocumentElement()
                .getNodeName());

        NodeList featureMembers = doc.getElementsByTagName("gml:featureMember");
        assertEquals(3, featureMembers.getLength());
        NodeList features = doc.getElementsByTagName("cite:Geometryless");
        assertEquals(3, features.getLength());
    }
    
    public void testUpdate() throws Exception {
        // perform an update
        String update = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" "
                + "xmlns:cite=\"http://www.opengis.net/cite\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\"> "
                + "<wfs:Update typeName=\"cite:Geometryless\" > " + "<wfs:Property>"
                + "<wfs:Name>name</wfs:Name>" + "<wfs:Value>AnotherName</wfs:Value>"
                + "</wfs:Property>" + "<ogc:Filter>"
                + "<ogc:FeatureId fid=\"Geometryless.2\"/>"
                + "</ogc:Filter>"
                + "</wfs:Update>" + "</wfs:Transaction>";

        Document dom = postAsDOM("wfs", update);
        assertTrue(dom.getElementsByTagName("wfs:SUCCESS").getLength() != 0);

        // do another get feature
        dom = getAsDOM("wfs?request=GetFeature&typename=cite:Geometryless&version=1.0.0&service=wfs&featureId=Geometryless.2");
        assertEquals("AnotherName", dom.getElementsByTagName("cite:name").item(0)
                .getFirstChild().getNodeValue());
    }
    
    public void testDelete() throws Exception {
        // perform an update
        String insert = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" "
                + "xmlns:cite=\"http://www.opengis.net/cite\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\"> "
                + "<wfs:Delete typeName=\"cite:Geometryless\" > " 
                + "<ogc:Filter>"
                + "<ogc:FeatureId fid=\"Geometryless.2\"/>"
                + "</ogc:Filter>"
                + "</wfs:Delete>" + "</wfs:Transaction>";

        Document dom = postAsDOM("wfs", insert);
        assertTrue(dom.getElementsByTagName("wfs:SUCCESS").getLength() != 0);

        // do another get feature
        dom = getAsDOM("wfs?request=GetFeature&typename=cite:Geometryless&version=1.0.0&service=wfs&featureId=Geometryless.2");
        assertEquals(0, dom.getElementsByTagName("cite:Geometryless").getLength());
    }
    
    public void testInsert() throws Exception {
        // perform an insert
        String insert = "<wfs:Transaction service=\"WFS\" version=\"1.0.0\" "
                + "xmlns:cite=\"http://www.opengis.net/cite\" "
                + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
                + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
                + "xmlns:gml=\"http://www.opengis.net/gml\"> "
                + "<wfs:Insert > "
                + "<cite:Geometryless fid=\"Geometryless.4\">"
                + "<cite:name>Gimbo</cite:name>"
                + "<cite:number>1000</cite:number>"
                + "</cite:Geometryless>"
                + "</wfs:Insert>" + "</wfs:Transaction>";

        Document dom = postAsDOM("wfs", insert);
        assertTrue(dom.getElementsByTagName("wfs:SUCCESS").getLength() != 0);
        assertTrue(dom.getElementsByTagName("wfs:InsertResult").getLength() != 0);

        // do another get feature
        dom = getAsDOM("wfs?request=GetFeature&typename=cite:Geometryless&version=1.0.0&service=wfs");
        assertEquals(4, dom.getElementsByTagName("cite:Geometryless").getLength());
    }
    
    
}
