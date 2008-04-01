package org.geoserver.csv.rest;

import java.io.IOException;
import java.util.List;

import org.geoserver.csv.CsvService;

public class GeometryLayersResource extends AbstractLayersResource {
    public GeometryLayersResource(CsvService csvService) {
        super(csvService);
    }

    @Override
    protected List<String> getLayers() throws IOException {
        return csvService.getGeometryLayers();
    }

}
