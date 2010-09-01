package org.geoserver.python.format;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.wms.responses.featureInfo.AbstractFeatureInfoResponse;

public class PythonGetFeatureInfoOutputFormat extends AbstractFeatureInfoResponse {

    PythonVectorFormatAdapter adapter;
    
    public PythonGetFeatureInfoOutputFormat(PythonVectorFormatAdapter adapter) {
        this.adapter = adapter;
        format = adapter.getMimeType();
        supportedFormats = Arrays.asList(format, adapter.getName());
    }

    @Override
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        try {
            adapter.write((List)results, out);
        } 
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public HashMap<String, String> getResponseHeaders() {
        return null;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new PythonGetFeatureInfoOutputFormat(adapter);
    }
    
}
