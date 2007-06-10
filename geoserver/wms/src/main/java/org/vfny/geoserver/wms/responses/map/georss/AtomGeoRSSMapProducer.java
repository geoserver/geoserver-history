package org.vfny.geoserver.wms.responses.map.georss;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;

public class AtomGeoRSSMapProducer implements GetMapProducer {

    /** format name */
    public static String FORMAT = "atom";
    
    /** mime type */
    public static String MIME_TYPE = "application/atom+xml";
    
    /**
     * current map context
     */
    WMSMapContext map;
    
    public String getContentType() throws IllegalStateException {
        return MIME_TYPE;
    }

    public void produceMap(WMSMapContext map) throws WmsException {
        this.map = map;
    }

    public void writeTo(OutputStream out) throws ServiceException, IOException {
        AtomGeoRSSTransformer tx = new AtomGeoRSSTransformer();
        try {
            tx.transform( map, out );
        } 
        catch (TransformerException e) {
            throw (IOException) new IOException().initCause( e );
        }
        
        map = null;
    }
    
    public void abort() {
        map = null;
    }

    public String getContentDisposition() {
        return null;
    }

}
