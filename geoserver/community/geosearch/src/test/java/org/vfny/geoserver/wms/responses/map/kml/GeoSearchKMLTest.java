package org.vfny.geoserver.wms.responses.map.kml;

import org.geoserver.test.GeoServerTestSupport;
import org.geoserver.data.test.MockData;

import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;
import com.mockrunner.mock.web.MockHttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.util.Iterator;
import java.util.List;
import java.io.IOException;

public class GeoSearchKMLTest extends RegionatingTestSupport {
    public void testOutput() throws Exception {
        final String path = 
            "wms?request=getmap&service=wms&version=1.1.1" + 
            "&format=" + KMLMapProducerFactory.MIME_TYPE + 
            "&layers=" + MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart() + 
            "&styles=" + MockData.BASIC_POLYGONS.getLocalPart() + 
            "&height=1024&width=1024&bbox=-180,-90,0,90&srs=EPSG:4326" +  
            "&featureid=BasicPolygons.1107531493643";

        Document document = getAsDOM(path);
        assertEquals("kml", document.getDocumentElement().getTagName());;
    }

    /**
     * Test that requests regionated by data actually return stuff.
     */
    public void testDataRegionator() throws Exception{
        final String path = 
            "wms?request=getmap&service=wms&version=1.1.1" + 
            "&format=" + KMLMapProducerFactory.MIME_TYPE + 
            "&layers=" + MockData.DIVIDED_ROUTES.getPrefix() + ":" + MockData.DIVIDED_ROUTES.getLocalPart() + 
            "&styles=" + MockData.DIVIDED_ROUTES.getLocalPart() + 
            "&height=1024&width=1024&srs=EPSG:4326" +  
            "&format_options=regionateBy:data;regionateAttr:NUM_LANES";

        Document document = getAsDOM(path + "&bbox=-180,-90,0,90");
        assertEquals("kml", document.getDocumentElement().getTagName());
        int westCount = document.getDocumentElement().getElementsByTagName("Placemark").getLength();

        assertStatusCodeForGet(204, path + "&bbox=0,-90,180,90");

        assertEquals(1, westCount);
    }

     /**
      * Test that requests regionated by geometry actually return stuff.
      */
     public void testGeometryRegionator() throws Exception{
        final String path = 
            "wms?request=getmap&service=wms&version=1.1.1" + 
            "&format=" + KMLMapProducerFactory.MIME_TYPE + 
            "&layers=" + MockData.DIVIDED_ROUTES.getPrefix() + ":" + MockData.DIVIDED_ROUTES.getLocalPart() + 
            "&styles=" + MockData.DIVIDED_ROUTES.getLocalPart() + 
            "&height=1024&width=1024&srs=EPSG:4326" +  
            "&format_options=regionateBy:geo";
        Document document = getAsDOM(path + "&bbox=-180,-90,0,90");
        assertEquals("kml", document.getDocumentElement().getTagName());
        assertEquals(1, document.getDocumentElement().getElementsByTagName("Placemark").getLength());

        assertStatusCodeForGet(204, path + "&bbox=0,-90,180,90");
    }

    /**
     * Test whether geometries that cross tiles get put into both of them.
     */
    public void testBigGeometries() throws Exception {
        final String path = 
            "wms?request=getmap&service=wms&version=1.1.1" + 
            "&format=" + KMLMapProducerFactory.MIME_TYPE + 
            "&layers=" + CENTERED_POLY.getPrefix() + ":" + CENTERED_POLY.getLocalPart() + 
            "&styles=" + 
            "&height=1024&width=1024&srs=EPSG:4326" +  
            "&format_options=regionateBy:geo";

        Document document = getAsDOM(path + "&bbox=-180,-90,0,90");
        assertEquals("kml", document.getDocumentElement().getTagName());
        assertEquals(1, document.getDocumentElement().getElementsByTagName("Placemark").getLength());

        assertStatusCodeForGet(204, path + "&bbox=0,-90,180,90");
    }
}
