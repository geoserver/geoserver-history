/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
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
