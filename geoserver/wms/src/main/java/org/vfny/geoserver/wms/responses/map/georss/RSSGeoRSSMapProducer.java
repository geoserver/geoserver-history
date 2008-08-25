/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;


public class RSSGeoRSSMapProducer implements GetMapProducer {

    /** the actual mime type for the response header */
    private static String MIME_TYPE = "application/xml";

    /** format names/aliases */
    public static String[] OUTPUT_FORMATS = {
        "application/rss+xml",
        "rss",
        "application/rss xml"
    };

    /**
     * current map context
     */
    WMSMapContext map;

    private String outputFormat = "application/rss+xml";

    public RSSGeoRSSMapProducer(){
    }
    
    public String getContentType() throws IllegalStateException {
        return MIME_TYPE;
    }

    public void produceMap() throws WmsException {
		//nothing to do, the actual work is done in writeTo since its purely streamed
	}

    public void writeTo(OutputStream out) throws ServiceException, IOException {
        RSSGeoRSSTransformer tx = new RSSGeoRSSTransformer();
        GetMapRequest request = map.getRequest();

        String geometryEncoding = (String)request.getFormatOptions().get("encoding");
        if ("gml".equals(geometryEncoding)){
            tx.setGeometryEncoding(GeoRSSTransformerBase.GeometryEncoding.GML);
        } else if ("latlong".equals(geometryEncoding)){
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
        for (int i = 0; i < OUTPUT_FORMATS.length; i++) {
            if (OUTPUT_FORMATS[i].equalsIgnoreCase(format)) {
                this.outputFormat = OUTPUT_FORMATS[i];
                return;
            }
        }
        throw new IllegalArgumentException(format + " is not supported by this producer");
 	}

	/**
	 * @see GetMapProducer#getOutputFormatNames()
	 */
    public List<String> getOutputFormatNames() {
        return Arrays.asList(OUTPUT_FORMATS);
    }
}
