package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.wms.WmsException;

public class KML3MapProducer extends KMLMapProducer {

    public KML3MapProducer(String mapFormat, String mime_type) {
        super(mapFormat, mime_type);
    }
    
    public void produceMap() throws WmsException {
        transformer = new KML3Transformer();
        transformer.setIndentation(3);
    }
    

}
