package org.geoserver.wms;

import javax.servlet.ServletResponse;

import org.w3c.dom.Document;

public class GetMapTest extends WMSTestSupport {

    public void testWorkspaceQualified() throws Exception {
        
        Document doc = getAsDOM("cite/wms?request=getmap&service=wms" +
                "&layers=PrimitiveGeoFeature&width=100&height=100&format=image/png" +
                "&srs=epsg:4326&bbox=-180,-90,180,90", true);
        assertEquals("ServiceExceptionReport", doc.getDocumentElement().getNodeName());
        
        ServletResponse response = getAsServletResponse("cite/wms?request=getmap&service=wms" +
                "&layers=Lakes&width=100&height=100&format=image/png" +
                "&srs=epsg:4326&bbox=-180,-90,180,90");
        assertEquals("image/png", response.getContentType());
    }
    
    public void testLayerQualified() throws Exception {
        Document doc = getAsDOM("cite/Ponds/wms?request=getmap&service=wms" +
                "&layers=Forests&width=100&height=100&format=image/png" +
                "&srs=epsg:4326&bbox=-180,-90,180,90", true);
        assertEquals("ServiceExceptionReport", doc.getDocumentElement().getNodeName());
        
        ServletResponse response = getAsServletResponse("cite/Ponds/wms?request=getmap&service=wms" +
                "&layers=Ponds&width=100&height=100&format=image/png" +
                "&srs=epsg:4326&bbox=-180,-90,180,90");
        assertEquals("image/png", response.getContentType());
    }
    
}
