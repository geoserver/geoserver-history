package org.vfny.geoserver.wms.responses.map.openlayers;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.GetMapProducerFactorySpi;

public class OpenLayersMapProducerFactory implements GetMapProducerFactorySpi {

    public boolean canProduce(String mapFormat) {
        return "openlayers".equalsIgnoreCase( mapFormat );
    }

    public GetMapProducer createMapProducer(String mapFormat, WMS wms)
            throws IllegalArgumentException {
        
        return new OpenLayersMapProducer( wms );
    }

    public String getName() {
        return "OpenLayers Map";
    }

    public Set getSupportedFormats() {
        return Collections.singleton( "openlayers" );
    }

    public boolean isAvailable() {
        return true;
    }

    public Map getImplementationHints() {
        return null;
    }

}
