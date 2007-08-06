/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;

import javax.xml.transform.TransformerException;


public class RSSGeoRSSMapProducer implements GetMapProducer {
    /** format names/aliases */
    public static HashSet FORMATS = new HashSet();
    static {
        FORMATS.add("rss");
        FORMATS.add("application/rss xml");
    }

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
            tx.transform(map, out);
        } catch (TransformerException e) {
            throw (IOException) new IOException().initCause(e);
        }
    }

    public void abort() {
    }

    public String getContentDisposition() {
        return "inline; filename=geoserver.xml";
    }
}
