package org.geoserver.csv.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.geoserver.csv.CsvService;
import org.geoserver.csv.LayerResult;
import org.geoserver.rest.JSONFormat;
import org.restlet.data.MediaType;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;

public class DataLayersResource extends AbstractLayersResource {
    public DataLayersResource(CsvService csvService) {
        super(csvService);
    }

    @Override
    public boolean allowPost() {
        return true;
    }

    @Override
    protected List<String> getLayers() throws IOException {
        return csvService.getDataLayers();
    }

    public void handlePost() {
        final Response resp = getResponse();
        File csv = null;
        try {
            FileItemFactory factory = new DiskFileItemFactory();
            RestletFileUpload upload = new RestletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(getRequest());

            // lookup the parameters, the file, the geometry layer name, and the
            // join field
            FileItem file = null;
            String geometryLayer = null;
            String joinField = null;
            for (FileItem fileItem : items) {
                if (fileItem.getFieldName().equalsIgnoreCase("geometryLayer"))
                    geometryLayer = fileItem.getString();
                else if (fileItem.getFieldName().equalsIgnoreCase("file"))
                    file = fileItem;
                else if (fileItem.getFieldName().equalsIgnoreCase("joinField"))
                    joinField = fileItem.getString();
            }

            if (file == null) {
                sendExtJsError(resp, Status.CLIENT_ERROR_BAD_REQUEST,
                        "'file' parameter is required but not provided");
                return;
            } else if (geometryLayer == null) {
                sendExtJsError(resp, Status.CLIENT_ERROR_BAD_REQUEST,
                        "'geometryLayer' parameter is required but not provided");
                return;
            } else if (joinField == null) {
                sendExtJsError(resp, Status.CLIENT_ERROR_BAD_REQUEST,
                        "'joinField' parameter is required but not provided");
                return;
            } else if (!csvService.getGeometryLayers().contains(geometryLayer)) {
                sendExtJsError(resp, Status.CLIENT_ERROR_BAD_REQUEST,
                        "geometryLayer " + geometryLayer
                                + " is unknown, valid values are "
                                + csvService.getGeometryLayers());
                return;
            }

            csv = File.createTempFile("csv", ".csv");
            IOUtils.copy(file.getInputStream(), new FileOutputStream(csv));
            List results = csvService.configureCsvFile(geometryLayer,
                    joinField, csv);
            JSONFormat format = new JSONFormat(MediaType.TEXT_HTML);
            resp.setEntity(format.makeRepresentation(convertToMap(results)));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "Error occurred managing csv file upload request", e);
            sendExtJsError(resp, Status.SERVER_ERROR_INTERNAL, e.getMessage());
        }
    }

    /**
     * Apparently extJs cannot handle 505, we have to send a 200 with the error
     * embedded in a JSON map...
     * 
     * @param resp
     * @param status
     * @param message
     */
    private void sendExtJsError(final Response resp, Status status,
            String message) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", "false");
        result.put("message", message);
        result.put("status", status.getCode());
        JSONFormat format = new JSONFormat(MediaType.TEXT_HTML);
        resp.setEntity(format.makeRepresentation(result));
    }

    private Object convertToMap(List<LayerResult> layers) {
        List<Map> dataLayers = new ArrayList<Map>();
        for (LayerResult layer : layers) {
            Map<String, Object> layerMap = new HashMap<String, Object>();
            layerMap.put("name", layer.getLayerName());
            layerMap.put("title", layer.getLayerDescription());
            layerMap.put("url", getRequest().getResourceRef().toString() + "/"
                    + layer.getLayerName());
            dataLayers.add(layerMap);
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", "true");
        result.put("dataLayers", dataLayers);
        return result;
    }

}
