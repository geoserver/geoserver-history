/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.georss;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.transform.TransformerException;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapOutputFormat;
import org.geoserver.wms.WMS;
import org.geoserver.wms.WMSMapContext;
import org.geoserver.wms.request.GetMapRequest;
import org.vfny.geoserver.wms.WmsException;


public class RSSGeoRSSMapProducer implements GetMapOutputFormat {

    /** the actual mime type for the response header */
    private static String MIME_TYPE = "application/xml";

    /** format names/aliases */
    public static final Set<String> FORMAT_NAMES;
    static{
        String[] FORMATS = {
            "application/rss+xml",
            "rss",
            "application/rss xml"
        };
        Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
        names.addAll(Arrays.asList(FORMATS));
        FORMAT_NAMES = Collections.unmodifiableSet(names);
    }

    /**
     * current map context
     */
    WMSMapContext map;

    private String outputFormat = "application/rss+xml";

    private WMS wms;

    public RSSGeoRSSMapProducer(WMS wms){
        this.wms = wms;
    }
    
    public String getContentType() throws IllegalStateException {
        return MIME_TYPE;
    }

    public void produceMap() throws WmsException {
		//nothing to do, the actual work is done in writeTo since its purely streamed
	}

    public void writeTo(OutputStream out) throws ServiceException, IOException {
        RSSGeoRSSTransformer tx = new RSSGeoRSSTransformer(wms);
        GetMapRequest request = map.getRequest();

        String geometryEncoding = (String)request.getFormatOptions().get("encoding");
        if ("gml".equals(geometryEncoding)){
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.GML);
        } else if ("latlong".equals(geometryEncoding)){
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
    }

    public void abort() {
    }

    public String getContentDisposition() {
        return "inline; filename=geoserver.xml";
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
        if (FORMAT_NAMES.contains(format)) {
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
