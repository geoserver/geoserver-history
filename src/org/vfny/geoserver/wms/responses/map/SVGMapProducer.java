/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WMSMapContext;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.map.svg.EncodeSVG;


/**
 * Handles a GetMap request that spects a map in SVG format.
 *
 * @author Gabriel Rold?n
 * @version $Id: SVGMapResponse.java,v 1.11 2004/04/16 18:36:49 cholmesny Exp $
 */
class SVGMapProducer implements GetMapProducer {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");

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
        return SvgMapProducerFactory.MIME_TYPE;
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
        this.svgEncoder = new EncodeSVG(map);
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
