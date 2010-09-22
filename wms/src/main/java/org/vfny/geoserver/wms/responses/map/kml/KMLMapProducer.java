/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.kml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.transform.TransformerException;

import org.geoserver.config.ServiceInfo;
import org.geoserver.platform.ServiceException;
import org.geoserver.wms.WMS;
import org.geoserver.wms.request.GetMapRequest;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.AbstractGetMapProducer;

/**
 * Handles a GetMap request that spects a map in KML format.
 * 
 * @author James Macgill
 */
public class KMLMapProducer extends AbstractGetMapProducer implements GetMapProducer {
	/** standard logger */
	protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses.wms.kml");

    /** 
     * Official KML mime type
     */
    public static final String MIME_TYPE = "application/vnd.google-earth.kml+xml";
    
    public static final List<String> OUTPUT_FORMATS = Arrays.asList(
                MIME_TYPE,
            "application/vnd.google-earth.kml",
            "kml",
            "application/vnd.google-earth.kml xml"
        );

    /** kml transformer which turns the map context into kml */
	protected KMLTransformer transformer;

	private List<String> aliases = new ArrayList<String>();

    private WMS wms;
	
	public KMLMapProducer(WMS wms) {
		super(MIME_TYPE, OUTPUT_FORMATS);
            this.wms = wms;
	}

	/**
	 * Request that encoding be halted if possible.
	 * 
	 * @param gs
	 *            The orriginating Service
	 */
	public void abort(ServiceInfo gs) {
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
		return super.getContentDisposition(".kml");
	}
}
