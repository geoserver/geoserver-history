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
                resp.setEntity("'file' parameter is required but not provided", MediaType.TEXT_PLAIN);
                resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                return;
            } else if (geometryLayer == null) {
                resp
                        .setEntity("'geometryLayer' parameter is required but not provided", MediaType.TEXT_PLAIN);
                resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                return;
            } else if (joinField == null) {
                resp
                        .setEntity("'joinField' parameter is required but not provided", MediaType.TEXT_PLAIN);
                resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                return;
            } else if (!csvService.getGeometryLayers().contains(geometryLayer)) {
                resp.setEntity("geometryLayer " + geometryLayer
                        + " is unknown, valid values are "
                        + csvService.getGeometryLayers(), MediaType.TEXT_PLAIN);
                resp.setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
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
            resp.setStatus(Status.SERVER_ERROR_INTERNAL);
            resp.setEntity(e.getMessage(), MediaType.TEXT_PLAIN);
        }
    }

    private Object convertToMap(List<LayerResult> layers) {
        List<Map> dataLayers = new ArrayList<Map>();
        for (LayerResult layer : layers) {
            dataLayers.add(Collections.singletonMap(layer.getLayerName(), getRequest().getResourceRef().toString() + "/" + layer.getLayerName()));
        }
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("success", "true");
        result.put("dataLayers", dataLayers);
        return result;
    }

}
