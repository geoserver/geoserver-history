package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.wms.WMSMapContext;
import org.geotools.map.MapLayer;

public class GeoSearchTransformer extends KMLTransformer {

    protected GeoSearchVectorTransformer createVectorTransformer(WMSMapContext mapContext, MapLayer layer){
        return new GeoSearchVectorTransformer(mapContext, layer);
    }
}

