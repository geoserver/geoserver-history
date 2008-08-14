/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.georss;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.HashSet;

import javax.xml.transform.TransformerException;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;

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

    private final String advertisedFormatName;

	public AtomGeoRSSMapProducer(final String advertisedFormatName){
	    this.advertisedFormatName = advertisedFormatName;
	}

	public String getContentType() throws IllegalStateException {
		return MIME_TYPE;
	}

	public void setContentType(String mime) {
		throw new UnsupportedOperationException();
	}

	public void produceMap() throws WmsException {
	}

	public void writeTo(OutputStream out) throws ServiceException, IOException {
		AtomGeoRSSTransformer tx = new AtomGeoRSSTransformer();
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
		return advertisedFormatName;
	}

	public void setOutputFormat(String format) {
		throw new UnsupportedOperationException();
	}

}
