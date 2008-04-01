package org.geoserver.csv.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.geoserver.csv.GeoServerCsvService;
import org.geoserver.rest.AutoXMLFormat;
import org.geoserver.rest.DataFormat;
import org.geoserver.rest.JSONFormat;
import org.geoserver.rest.MapResource;
import org.geotools.data.DataSourceException;
import org.restlet.data.MediaType;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;

public class DataLayerResource extends MapResource {
    protected static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geoserver.csv");

    protected GeoServerCsvService csvService;

    private DataConfig dataConfig;

    private Data catalog;

    public DataLayerResource(GeoServerCsvService csvService, Data catalog,
            DataConfig dataConfig) {
        this.csvService = csvService;
        this.catalog = catalog;
        this.dataConfig = dataConfig;
    }

    @Override
    public Object getMap() {
        String layerName = (String) getRequest().getAttributes().get(
                "layerName");
        try {
            String description = csvService.getLayerDescription(layerName);
            Map<String, String> result = new HashMap<String, String>();
            result.put("name", layerName);
            result.put("title", description);
            return result;
        } catch (Exception e) {
            Response resp = getResponse();
            resp.setEntity("An error occurred while retrieving the layer "
                    + layerName + ": " + e.getMessage(), MediaType.TEXT_PLAIN);
            resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return null;
        }
    }

    @Override
    public Map getSupportedFormats() {
        Map<String, DataFormat> formats = new HashMap<String, DataFormat>();
        formats.put("json", new JSONFormat());
        formats.put("xml", new AutoXMLFormat());
        formats.put(null, formats.get("json")); // default
        return formats;
    }

    @Override
    public boolean allowDelete() {
        return true;
    }

    @Override
    public void handleDelete() {
        String layerName = (String) getRequest().getAttributes().get(
                "layerName");
        final String qualifiedName = csvService.getDataStoreId() + ":"
                + layerName;
        if (dataConfig.getFeatureTypeConfig(qualifiedName) == null) {
            Response resp = getResponse();
            resp.setEntity("Unknown layer " + layerName, MediaType.TEXT_PLAIN);
            resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
        }

        dataConfig.removeFeatureType(qualifiedName);
        catalog.load(dataConfig.toDTO());
        try {
            XMLConfigWriter.store((DataDTO) dataConfig.toDTO(),
                    GeoserverDataDirectory.getGeoserverDataDirectory());
        } catch (ConfigurationException e) {
            Response resp = getResponse();
            resp.setEntity("Error occurred trying to write out "
                    + "the GeoServer configuration", MediaType.TEXT_PLAIN);
            resp.setStatus(Status.SERVER_ERROR_INTERNAL);
        }
    }

}
