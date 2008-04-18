package org.vfny.geoserver.wms.responses.map.kml;

import org.geoserver.test.GeoServerTestSupport;
import org.geoserver.data.test.MockData;

import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GeoSearchKMLTest extends GeoServerTestSupport {
    public void testOutput() throws Exception {
        final String path = 
            "wms?request=getmap&service=wms&version=1.1.1" + 
            "&format=" + KMLMapProducerFactory.MIME_TYPE + 
            "&layers=" + MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart() + 
            "&styles=" + MockData.BASIC_POLYGONS.getLocalPart() + 
            "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326" +  
            "&featureid=BasicPolygons.1107531493643";

        Document document = getAsDOM(path);
        assertEquals("kml", document.getDocumentElement().getTagName());;
    }
}
