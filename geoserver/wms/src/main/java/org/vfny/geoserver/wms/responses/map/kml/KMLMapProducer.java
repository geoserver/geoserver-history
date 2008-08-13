/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.geoserver.platform.ServiceException;
import org.geotools.map.MapLayer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.RasterMapProducer;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.AbstractGetMapProducer;

/**
 * Handles a GetMap request that spects a map in KML format.
 * 
 * @author James Macgill
 */
class KMLMapProducer extends AbstractGetMapProducer implements GetMapProducer {
	/** standard logger */
	protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses.wms.kml");

    /** 
     * Official KML mime type
     */
    public static final String MIME_TYPE = "application/vnd.google-earth.kml+xml";

    /** kml transformer which turns the map contedxt into kml */
	protected KMLTransformer transformer;

	public KMLMapProducer() {
		super(MIME_TYPE, MIME_TYPE);
	}

	/**
	 * Request that encoding be halted if possible.
	 * 
	 * @param gs
	 *            The orriginating Service
	 */
	public void abort(Service gs) {
		if (transformer != null) {
			// transformer.abort();
		}
	}

	/**
	 * aborts the encoding.
	 */
	public void abort() {
		LOGGER.fine("aborting KML map response");

		// if (this.kmlEncoder != null) {
		// LOGGER.info("aborting KML encoder");
		// this.kmlEncoder.abort();
		// }
		if (transformer != null) {
			LOGGER.info("aborting KML encoder");

			// transformer.abort();
		}
	}

	/**
	 * Produce the actual map ready for outputing.
	 * 
	 * @param map
	 *            WMSMapContext describing what layers, styles, area of interest
	 *            etc are to be used when producing the map.
	 * 
	 * @throws WmsException
	 *             thrown if anything goes wrong during the production.
	 */
	public void produceMap() throws WmsException {
		transformer = new KMLTransformer();

		// TODO: use GeoServer.isVerbose() to determine if we should indent?
		transformer.setIndentation(3);
		GetMapRequest request = mapContext.getRequest();
		WMS wms = request.getWMS();
		Charset encoding = wms.getCharSet();
		transformer.setEncoding(encoding);
	}

	/**
	 * Pumps the map to the provided output stream. Note by this point that
	 * produceMap should already have been called so little work should be done
	 * within this method.
	 * 
	 * @param out
	 *            OutputStream to stream the map to.
	 * 
	 * @throws ServiceException
	 * @throws IOException
	 * 
	 * @TODO replace stream copy with nio code.
	 */
	public void writeTo(OutputStream out) throws ServiceException, IOException {
		try {
			transformer.transform(mapContext, out);
		} catch (TransformerException e) {
		    //LOGGER.severe(e.getMessage());
		    throw (IOException) new IOException().initCause(e);
		}
	}

	/**
	 * @return a sensible .kml file name attachment header
	 * @see GetMapProducer#getContentDisposition()
	 */
	public String getContentDisposition() {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mapContext.getLayerCount(); i++) {
			MapLayer layer = mapContext.getLayer(i);
			String title = layer.getFeatureSource().getSchema().getName().getLocalPart();
			if (title != null && !title.equals("")) {
				sb.append(title).append("_");
			}
		}
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
			return "attachment; filename=" + sb.toString() + ".kml";
		}
		return "attachment; filename=geoserver.kml";
	}
}
