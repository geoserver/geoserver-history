/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.responses.wms.WMSMapContext;
import org.vfny.geoserver.responses.wms.map.svg.EncodeSVG;
import org.vfny.geoserver.responses.wms.map.svg.EncoderConfig;


/**
 * Handles a GetMap request that spects a map in SVG format.
 *
 * @author Gabriel Rold?n
 * @version $Id: SVGMapResponse.java,v 1.11 2004/04/16 18:36:49 cholmesny Exp $
 */
public class SVGMapResponse implements GetMapProducer {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");

    /** DOCUMENT ME! */
    private static final String PRODUCE_TYPE = "image/svg";

    /** DOCUMENT ME! */
    private static final String MIME_TYPE = "image/svg+xml";

    /** DOCUMENT ME! */
    private EncodeSVG svgEncoder;

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     */
    public void abort(Service gs) {
        this.svgEncoder.abort();
    }

    /**
     * DOCUMENT ME!
     *
     * @param gs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return MIME_TYPE;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Set getSupportedFormats() {
        return Collections.singleton(MIME_TYPE);
    }

    /**
     * evaluates if this Map producer can generate the map format specified by
     * <code>mapFormat</code>
     * 
     * <p>
     * In this case, true if <code>mapFormat</code> starts with "image/svg", as
     * both <code>"image/svg"</code> and <code>"image/svg+xml"</code> are
     * commonly passed.
     * </p>
     *
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format.
     */
    public boolean canProduce(String mapFormat) {
        LOGGER.fine("checking if can producer " + mapFormat + ", returning"
            + mapFormat.startsWith(PRODUCE_TYPE));

        return mapFormat.startsWith(PRODUCE_TYPE);
    }

    /**
     * aborts the encoding.
     */
    public void abort() {
        LOGGER.fine("aborting SVG map response");

        if (this.svgEncoder != null) {
            LOGGER.info("aborting SVG encoder");
            this.svgEncoder.abort();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENT ME!
     * @param format DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    public void produceMap(WMSMapContext map)
        throws WmsException {
        EncoderConfig encoderData = new EncoderConfig(map);
        this.svgEncoder = new EncodeSVG(encoderData);
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        this.svgEncoder.encode(out);
    }
}
