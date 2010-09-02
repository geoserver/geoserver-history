package org.geoserver.python.format;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;

public class PythonVectorGetMapOutputFormat implements GetMapProducer {

    PythonVectorFormatAdapter adapter;
    WMSMapContext context;
    
    public PythonVectorGetMapOutputFormat(PythonVectorFormatAdapter adapter) {
        this.adapter = adapter;
    }
    
    public String getContentDisposition() {
        return null;
    }

    public String getContentType() throws IllegalStateException {
        return adapter.getMimeType();
    }

    public void setMapContext(WMSMapContext mapContext) {
        this.context = mapContext;
    }

    public WMSMapContext getMapContext() {
        return context;
    }

    public Set<String> getOutputFormatNames() {
        return Collections.singleton(adapter.getName());
    }
    
    public void setOutputFormat(String format) {
    }

    public String getOutputFormat() {
        return adapter.getName();
    }

    public void produceMap() throws WmsException {
    }
    
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        try {
            adapter.write(context, out);
        } 
        catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public void abort() {
    }
}
