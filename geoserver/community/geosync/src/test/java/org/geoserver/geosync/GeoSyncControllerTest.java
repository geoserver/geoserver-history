package org.geoserver.geosync;

import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

public class GeoSyncControllerTest extends GeoServerTestSupport {

   
    public void testFeed() throws Exception {
        String insert = "<wfs:Transaction service=\"WFS\" version=\"1.1.0\" "
            + "xmlns:cgf=\"http://www.opengis.net/cite/geometry\" "
            + "xmlns:ogc=\"http://www.opengis.net/ogc\" "
            + "xmlns:wfs=\"http://www.opengis.net/wfs\" "
            + "xmlns:gml=\"http://www.opengis.net/gml\"> "
            + "<wfs:Insert srsName=\"EPSG:32615\"> " + "<cgf:Points>" + "<cgf:pointProperty>"
            + "<gml:Point>" + "<gml:pos>1 1</gml:pos>" + "</gml:Point>"
            + "</cgf:pointProperty>" + "<cgf:id>t0003</cgf:id>" + "</cgf:Points>"
            + "</wfs:Insert>" + "</wfs:Transaction>";
        
        Document d = postAsDOM( "wfs", insert );
        assertEquals( "wfs:TransactionResponse", d.getDocumentElement().getNodeName() );
        
        d = getAsDOM("/history?request=feed&a=b");
        assertEquals( "feed", d.getDocumentElement().getNodeName() );
        
        assertNotNull( getFirstElementByTagName( d, "wfs:Insert" ) );
            
    }
}
