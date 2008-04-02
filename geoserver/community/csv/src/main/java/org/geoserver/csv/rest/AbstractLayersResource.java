package org.geoserver.csv.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geoserver.csv.CsvService;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geoserver.rest.RestletException;
import org.restlet.data.Status;

public abstract class AbstractLayersResource extends MapResource {
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.csv");
    protected CsvService csvService;

    public AbstractLayersResource(CsvService csvService) {
        this.csvService = csvService;
    }

    @Override
    public Object getMap() throws RestletException {
        try {
            List<String> layers = getLayers();
            List<Map<String, String>> layerList = new ArrayList<Map<String, String>>();
            for (String layer : layers) {
                Map<String, String> layerMap = new LinkedHashMap<String, String>();
                layerMap.put("name", layer);
                layerMap.put("title", csvService.getLayerDescription(layer));
                layerList.add(layerMap);
            }
            return layerList;
        } catch (Exception e) {
            throw new RestletException("Error occurred computing the layer list: " + e.getMessage(), 
                    Status.SERVER_ERROR_INTERNAL);
        }

    }

    protected abstract List<String> getLayers() throws IOException;

    @Override
    public Map getSupportedFormats() {
        Map<String, DataFormat> formats = new HashMap<String, DataFormat>();
        formats.put("json", new JSONFormat());
        formats.put("xml", new AutoXMLFormat());
        formats.put(null, formats.get("json")); // default
        return formats;
    }

}
