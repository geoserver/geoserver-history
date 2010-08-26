package org.geoserver.monitor.ows.wms;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.geoserver.monitor.ows.RequestObjectHandler;
import org.geoserver.ows.util.OwsUtils;

public class GetMapHandler extends RequestObjectHandler {

    public GetMapHandler() {
        super("org.vfny.geoserver.wms.requests.GetMapRequest");
    }

    @Override
    public List<String> getLayers(Object request) {
        Object mapLayers = OwsUtils.get(request, "layers");
        
        List<String> layers = new ArrayList();
        for (int i = 0; i < Array.getLength(mapLayers); i++) {
            layers.add((String) OwsUtils.get(Array.get(mapLayers, i), "name"));
        }
        
        return layers;
    }

}
