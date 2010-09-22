/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.map.htmlimagemap;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Logger;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.map.AbstractGetMapProducer;
import org.vfny.geoserver.wms.WmsException;


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
     * The ImageMap is served as text/html: it is an HTML fragment, after all.   
     */
    static final String MIME_TYPE = "text/html";
    
    static final Set<String> SUPPORTED_FORMATS = Collections.singleton(MIME_TYPE);

    public HTMLImageMapMapProducer() {
		super(MIME_TYPE,SUPPORTED_FORMATS.toArray(new String[]{}));
		// TODO Auto-generated constructor stub
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

}
