package org.geoserver.monitor.ows.wms;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.geoserver.monitor.ows.RequestObjectHandler;
import org.geoserver.ows.util.OwsUtils;

public class GetFeatureInfoHandler extends RequestObjectHandler {

    public GetFeatureInfoHandler() {
        super("org.vfny.geoserver.wms.requests.GetFeatureInfoRequest");
    }

    @Override
    public List<String> getLayers(Object request) {
        Object queryLayers = OwsUtils.get(request, "queryLayers");
        List<String> layers = new ArrayList();
        for (int i = 0; i < Array.getLength(queryLayers); i++) {
            layers.add((String) OwsUtils.get(Array.get(queryLayers, i), "name"));
        }
        
        return layers;
    }

}
