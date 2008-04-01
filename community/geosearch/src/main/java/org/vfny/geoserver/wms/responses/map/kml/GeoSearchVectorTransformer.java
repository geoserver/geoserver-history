package org.vfny.geoserver.wms.responses.map.kml;

import org.vfny.geoserver.wms.WMSMapContext;
import org.geotools.map.MapLayer;
import java.util.logging.Logger;

public class GeoSearchVectorTransformer extends KMLVectorTransformer {

    Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.geosearch");

    public GeoSearchVectorTransformer(WMSMapContext mapContext, MapLayer mapLayer) {
        super(mapContext, mapLayer);
        LOGGER.info("Starting up geosearch vector transformer");
    }
}
