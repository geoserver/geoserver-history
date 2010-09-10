package org.geoserver.python.wfs;

import java.io.IOException;
import java.io.OutputStream;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geoserver.wfs.WFSGetFeatureOutputFormat;

public class PythonWFSGetFeatureOutputFormat extends WFSGetFeatureOutputFormat {

    PythonFeatureOutputFormatAdapter adapter;
    
    public PythonWFSGetFeatureOutputFormat(PythonFeatureOutputFormatAdapter adapter) {
        super(adapter.getName());
        this.adapter = adapter;
        
    }
    
    @Override
    public String getMimeType(Object value, Operation operation) throws ServiceException {
        return adapter.getMimeType();
    }

    @Override
    protected void write(FeatureCollectionType featureCollection, OutputStream output,
            Operation getFeature) throws IOException, ServiceException {
    
        try {
            adapter.write(featureCollection, output);
        } 
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }
}
