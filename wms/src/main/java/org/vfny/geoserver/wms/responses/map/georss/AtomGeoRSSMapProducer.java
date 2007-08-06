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


public class AtomGeoRSSMapProducer implements GetMapProducer {
    /** format names/aliases */
    public static HashSet FORMATS = new HashSet();
    static {
        FORMATS.add("atom");
        FORMATS.add("application/atom xml");
    }

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
            tx.transform(map, out);
        } catch (TransformerException e) {
            throw (IOException) new IOException().initCause(e);
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
