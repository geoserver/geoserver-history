package org.vfny.geoserver.wms.responses.map.openlayers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class OpenLayersMapProducer implements GetMapProducer {

    /**
     * static freemaker configuration
     */
    static Configuration cfg;
    static {
        cfg = new Configuration();
        cfg.setClassForTemplateLoading(OpenLayersMapProducer.class, "");
    }
    
    /**
     * wms configuration
     */
    WMS wms;
    /**
     * The current template
     */
    Template template;
    /**
     * The current map context 
     */
    WMSMapContext mapContext;
    
    public OpenLayersMapProducer( WMS wms ) {
        this.wms = wms;
    }
    
    public void abort() {
        mapContext = null;
        template = null;
    }

    public String getContentDisposition() {
        return null;
    }

    public String getContentType() throws IllegalStateException {
        return "text/html";
    }

    public void produceMap(WMSMapContext map) throws WmsException {
        mapContext = map;
    }

    public void writeTo(OutputStream out) throws ServiceException, IOException {
       
        try {
            //create the template
            Template template = cfg.getTemplate( "OpenLayersMapTemplate.ftl");
            template.process( mapContext, new OutputStreamWriter( out ) );
        } 
        catch (TemplateException e) {
            throw new WmsException( e );
        }
        
        mapContext = null;
        template = null;
    }

}
