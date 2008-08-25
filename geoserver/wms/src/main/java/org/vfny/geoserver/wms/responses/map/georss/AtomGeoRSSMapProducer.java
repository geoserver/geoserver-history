/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;

public class AtomGeoRSSMapProducer implements GetMapProducer {
    /** mime type */
    public static String MIME_TYPE = "application/atom+xml";

    /** format names/aliases */
    public static String[] FORMATS = { MIME_TYPE, "atom", "application/atom xml" };

    /**
     * current map context
     */
    WMSMapContext map;

    private String outputFormat = MIME_TYPE;

    public AtomGeoRSSMapProducer() {
    }

    public String getContentType() throws IllegalStateException {
        return MIME_TYPE;
    }

    /**
     * @deprecated
     */
    public void setContentType(String mime) {
        throw new UnsupportedOperationException();
    }

    public void produceMap() throws WmsException {
    }

    public void writeTo(OutputStream out) throws ServiceException, IOException {
        AtomGeoRSSTransformer tx = new AtomGeoRSSTransformer();
        GetMapRequest request = map.getRequest();

        String geometryEncoding = (String) request.getFormatOptions().get("encoding");
        if ("gml".equals(geometryEncoding)) {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.GML);
        } else if ("latlong".equals(geometryEncoding)) {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.LATLONG);
        } else {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.SIMPLE);
        }

        WMS wms = request.getWMS();
        Charset encoding = wms.getCharSet();
        tx.setEncoding(encoding);
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

    public WMSMapContext getMapContext() {
        return map;
    }

    public void setMapContext(WMSMapContext mapContext) {
        this.map = mapContext;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String format) {
        for (int i = 0; i < FORMATS.length; i++) {
            if (FORMATS[i].equalsIgnoreCase(format)) {
                this.outputFormat = FORMATS[i];
                return;
            }
        }
        throw new IllegalArgumentException(format + " is not supported by this producer");
    }

    /**
     * @see GetMapProducer#getOutputFormatNames()
     */
    public List<String> getOutputFormatNames() {
        return Arrays.asList(FORMATS);
    }

}
