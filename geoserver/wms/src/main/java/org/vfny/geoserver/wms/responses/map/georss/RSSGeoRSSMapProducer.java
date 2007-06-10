package org.vfny.geoserver.wms.responses.map.georss;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;

public class RSSGeoRSSMapProducer implements GetMapProducer {
    
    /** format name */
    public static String FORMAT = "rss";
    
    /** mime type */
    public static String MIME_TYPE = "application/rss+xml";
    /**
     * current map context
     */
    WMSMapContext map;
    
    public String getContentType() throws IllegalStateException {
        //return MIME_TYPE;
        return "application/xml";
    }

    public void produceMap(WMSMapContext map) throws WmsException {
        this.map = map;
    }

    public void writeTo(OutputStream out) throws ServiceException, IOException {
        RSSGeoRSSTransformer tx = new RSSGeoRSSTransformer();
        try {
            tx.transform( map, out );
        } 
        catch (TransformerException e) {
            throw (IOException) new IOException().initCause( e );
        }
    }

    public void abort() {
    }
    
    public String getContentDisposition() {
        return "inline; filename=geoserver.xml";
    }

}
