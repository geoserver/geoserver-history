/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms.svg;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.GetMapOutputFormat;
import org.geoserver.wms.map.AbstractGetMapProducer;
import org.vfny.geoserver.wms.WmsException;

/**
 * Handles a GetMap request that spects a map in SVG format.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
class SVGMapProducer extends AbstractGetMapProducer implements GetMapOutputFormat {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.vfny.geoserver.responses.wms.map");

    /** DOCUMENT ME! */
    private EncodeSVG svgEncoder;

    public SVGMapProducer(String mimeType, String[] outputFormats) {
        super(mimeType, outputFormats);
    }

    /**
     * aborts the encoding.
     */
    @Override
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
     * @param map
     *            DOCUMENT ME!
     * 
     * @throws WmsException
     *             DOCUMENT ME!
     */
    public void produceMap() throws WmsException {
        if (mapContext == null) {
            throw new WmsException("The map context is not set");
        }

        this.svgEncoder = new EncodeSVG(mapContext);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param out
     *            DOCUMENT ME!
     * 
     * @throws ServiceException
     *             DOCUMENT ME!
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        this.svgEncoder.encode(out);
    }
}
