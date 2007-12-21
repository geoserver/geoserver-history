/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.htmlimagemap;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.wms.GetMapProducer;
import org.vfny.geoserver.wms.WmsException;
import org.vfny.geoserver.wms.responses.AbstractGetMapProducer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;


/**
 * Handles a GetMap request that produces a map in HTMLImageMap format.
 *
 * @author Mauro Bartolomeoli
 */
public class HTMLImageMapMapProducer extends AbstractGetMapProducer implements GetMapProducer {
    
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.responses.wms.map");

    /** HTMLImageMapEncoder: encodes features in HTMLImageMap format */
    private EncodeHTMLImageMap htmlImageMapEncoder;

    /**
     * Aborts map generation.
     *
     * @param gs requesting service
     */
    public void abort(Service gs) {
        this.htmlImageMapEncoder.abort();
    }

    /**
     * Gets ContentType for the produced map.
     *
     * @return text/html
     */
    public String getContentType() {
        return HTMLImageMapMapProducerFactory.MIME_TYPE;
    }

    /**
     * TODO: add support for different encodings
     *
     * @return DOCUMENT ME!
     */
    public String getContentEncoding() {
        return null;
    }

    /**
     * Aborts the encoding.
     */
    public void abort() {
        LOGGER.fine("aborting HTMLImageMap map response");

        if (this.htmlImageMapEncoder != null) {
            LOGGER.info("aborting HTMLImageMap encoder");
            this.htmlImageMapEncoder.abort();
        }
    }

    /**
     * Renders the map.
     *
     * @throws WmsException if an error occurs during rendering
     */
    public void produceMap() throws WmsException {
        if (mapContext == null) {
            throw new WmsException("The map context is not set");
        }

        this.htmlImageMapEncoder = new EncodeHTMLImageMap(mapContext);
    }

    /**
     * Writes the generated map to an OutputStream.
     *
     * @param out final output stream
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws IOException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        this.htmlImageMapEncoder.encode(out);
    }

    public String getContentDisposition() {
        // can be null
        return null;
    }
}
