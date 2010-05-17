package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;

public class OWS5MapProducer extends KMLMapProducer {

    public OWS5MapProducer(String mapFormat, String mime_type) {
        super(mapFormat, mime_type);
    }
    
    public void produceMap() throws WmsException {
        final GetMapRequest request = mapContext.getRequest();
        
        // check module related parameters (extended data off by default, style on by default)
        String extendedDataParam = (String) request.getFormatOptions().get("extendedData");
        boolean extendedData = extendedDataParam != null && "true".equals(extendedDataParam.toLowerCase());
        String styleParam = (String) request.getFormatOptions().get("style");
        boolean style = styleParam == null || "true".equals(styleParam.toLowerCase());
        
        transformer = new OWS5Transformer(extendedData, style);
        transformer.setIndentation(3);
    }
    

}
