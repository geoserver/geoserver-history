package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;

public class GeoSearchMapProducer extends KMLMapProducer {
    
    Data catalog;
    
    public GeoSearchMapProducer(String format, String mimeType, Data catalog){
        super(format, mimeType);
        this.catalog = catalog;
    }

    public void produceMap() throws WmsException {
        final GetMapRequest request = mapContext.getRequest();
        
        // check module related parameters (extended data off by default, style on by default)
//        String extendedDataParam = (String) request.getFormatOptions().get("extendedData");
//        boolean extendedData = extendedDataParam != null && "true".equals(extendedDataParam.toLowerCase());
//        String styleParam = (String) request.getFormatOptions().get("style");
//        boolean style = styleParam == null || "true".equals(styleParam.toLowerCase());
        
        transformer = new GeoSearchTransformer( catalog );
        transformer.setIndentation(3);
    }
}
