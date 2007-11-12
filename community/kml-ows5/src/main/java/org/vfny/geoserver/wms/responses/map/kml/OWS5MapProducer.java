package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.wms.WmsException;

public class OWS5MapProducer extends KMLMapProducer {

    public OWS5MapProducer(String mapFormat, String mime_type) {
        super(mapFormat, mime_type);
    }
    
    public void produceMap() throws WmsException {
        transformer = new OWS5Transformer();
        transformer.setIndentation(3);
    }
    

}
