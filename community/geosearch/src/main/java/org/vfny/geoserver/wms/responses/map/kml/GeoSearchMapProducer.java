package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;

public class GeoSearchMapProducer extends KMLMapProducer {
    
    
    public GeoSearchMapProducer(String format, String mimeType){
        super(format, mimeType);
    }

    public void produceMap() throws WmsException {
        final GetMapRequest request = mapContext.getRequest();
        
        // check module related parameters (extended data off by default, style on by default)
//        String extendedDataParam = (String) request.getFormatOptions().get("extendedData");
//        boolean extendedData = extendedDataParam != null && "true".equals(extendedDataParam.toLowerCase());
//        String styleParam = (String) request.getFormatOptions().get("style");
//        boolean style = styleParam == null || "true".equals(styleParam.toLowerCase());
        
        transformer = new GeoSearchTransformer();
        transformer.setIndentation(3);
    }
}
