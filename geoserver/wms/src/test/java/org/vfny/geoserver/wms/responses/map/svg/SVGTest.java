package org.vfny.geoserver.wms.responses.map.svg;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.vfny.geoserver.config.WMSConfig;
import org.w3c.dom.Document;

public class SVGTest extends WMSTestSupport {
    
    public void testBasicSvgGenerator() throws Exception {
        getWMS().setSvgRenderer(WMSConfig.SVG_SIMPLE);
            Document doc = getAsDOM(
                "wms?request=getmap&service=wms&version=1.1.1" + 
                "&format=" + SvgMapProducerFactory.MIME_TYPE + 
                "&layers=" + MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart() + 
                "&styles=" + MockData.BASIC_POLYGONS.getLocalPart() + 
                "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326" +  
                "&featureid=BasicPolygons.1107531493643"
            );
//            print(doc);
            
            assertEquals( 1, doc.getElementsByTagName("svg").getLength());
            assertEquals( 1, doc.getElementsByTagName("g").getLength());
    }
    
    public void testBatikSvgGenerator() throws Exception {
        getWMS().setSvgRenderer(WMSConfig.SVG_BATIK);
        Document doc = getAsDOM(
            "wms?request=getmap&service=wms&version=1.1.1" + 
            "&format=" + SvgMapProducerFactory.MIME_TYPE + 
            "&layers=" + MockData.BASIC_POLYGONS.getPrefix() + ":" + MockData.BASIC_POLYGONS.getLocalPart() + 
            "&styles=" + MockData.BASIC_POLYGONS.getLocalPart() + 
            "&height=1024&width=1024&bbox=-180,-90,180,90&srs=EPSG:4326" +  
            "&featureid=BasicPolygons.1107531493643"
        );
//        print(doc);
        
        assertEquals( 1, doc.getElementsByTagName("svg").getLength());
        assertTrue(doc.getElementsByTagName("g").getLength() > 1);
}
    
}
