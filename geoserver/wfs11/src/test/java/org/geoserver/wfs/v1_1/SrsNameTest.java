package org.geoserver.wfs.v1_1;

import org.geoserver.wfs.WFSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SrsNameTest extends WFSTestSupport {

    public void testWfs11() throws Exception {
        String q = "wfs?request=getfeature&service=wfs&version=1.1.0" +
        "&typename=cgf:Points";
        Document d = getAsDOM( q );
        
        assertEquals( "wfs:FeatureCollection", d.getDocumentElement().getNodeName() );

        NodeList boxes = d.getElementsByTagName("gml:Envelope");
        assertFalse( boxes.getLength() == 0 );
        for ( int i = 0; i < boxes.getLength(); i++ ) {
                Element box = (Element) boxes.item( i );
                assertEquals( "urn:x-ogc:def:crs:EPSG:6.11.2:32615", box.getAttribute("srsName"));
        }
        
        NodeList points = d.getElementsByTagName("gml:Point");
        assertFalse( points.getLength() == 0 );
        for ( int i = 0; i < points.getLength(); i++ ) {
                Element point = (Element) points.item( i );
                assertEquals( "urn:x-ogc:def:crs:EPSG:6.11.2:32615", point.getAttribute("srsName"));
        }
    }
}
