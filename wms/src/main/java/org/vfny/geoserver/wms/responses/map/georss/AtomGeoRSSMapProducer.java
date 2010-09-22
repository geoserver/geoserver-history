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
import java.util.Set;
import java.util.TreeSet;

import javax.xml.transform.TransformerException;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMS;
import org.geoserver.wms.request.GetMapRequest;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;

public class AtomGeoRSSMapProducer implements GetMapProducer {
    /** mime type */
    public static String MIME_TYPE = "application/atom+xml";

    /** format names/aliases */
    public static final Set<String> FORMAT_NAMES;
    static{
        String[] FORMATS = { MIME_TYPE, "atom", "application/atom xml" };
        Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        names.addAll(Arrays.asList(FORMATS));
        FORMAT_NAMES = Collections.unmodifiableSet(names);
    }
    
    /**
     * current map context
     */
    WMSMapContext map;

    private String outputFormat = MIME_TYPE;

    private WMS wms;

    public AtomGeoRSSMapProducer(WMS wms) {
        this.wms = wms;
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
        AtomGeoRSSTransformer tx = new AtomGeoRSSTransformer(wms);
        GetMapRequest request = map.getRequest();

        String geometryEncoding = (String) request.getFormatOptions().get("encoding");
        if ("gml".equals(geometryEncoding)) {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.GML);
        } else if ("latlong".equals(geometryEncoding)) {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.LATLONG);
        } else {
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.SIMPLE);
        }

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
        if ( FORMAT_NAMES.contains(format) ){
            this.outputFormat = format;
        } else {
            throw new IllegalArgumentException(format + " is not supported by " + 
                    getClass().getSimpleName());
        }
    }

    /**
     * @see GetMapProducer#getOutputFormatNames()
     */
    public Set<String> getOutputFormatNames() {
        return FORMAT_NAMES;
    }

}
