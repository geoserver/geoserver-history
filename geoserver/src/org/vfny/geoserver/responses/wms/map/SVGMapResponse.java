/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.data.FeatureResults;
import org.geotools.styling.Style;
import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.WmsException;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.requests.wms.GetMapRequest;


/**
 * Handles a GetMap request that spects a map in SVG format.
 *
 * @author Gabriel Roldán
 * @version $Id: SVGMapResponse.java,v 1.3.2.4 2004/01/05 22:14:41 dmzwiers Exp $
 */
public class SVGMapResponse extends GetMapDelegate {
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses.wms.map");

    /** DOCUMENT ME! */
    private static final String MIME_TYPE = "image/svg+xml";
    private SVGEncoder svgEncoder;
    private FeatureTypeInfo[] requestedLayers;
    private FeatureResults[] resultLayers;
    private Style[] styles;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return MIME_TYPE;
    }

    public List getSupportedFormats() {
        return Collections.singletonList(MIME_TYPE);
    }

    /**
     * evaluates if this Map producer can generate the map format specified by
     * <code>mapFormat</code>
     *
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format
     */
    public boolean canProduce(String mapFormat) {
        return mapFormat.startsWith("image/svg");
    }

    public void abort() {
        LOGGER.fine("aborting SVG map response");

        if (svgEncoder != null) {
            LOGGER.info("aborting SVG encoder");
            svgEncoder.abort();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param requestedLayers DOCUMENT ME!
     * @param resultLayers DOCUMENT ME!
     * @param styles DOCUMENT ME!
     *
     * @throws WmsException DOCUMENT ME!
     */
    protected void execute(FeatureTypeInfo[] requestedLayers,
        FeatureResults[] resultLayers, Style[] styles)
        throws WmsException {
        GetMapRequest request = getRequest();
        this.requestedLayers = requestedLayers;
        this.resultLayers = resultLayers;
        this.styles = styles;
        this.svgEncoder = new SVGEncoder();

        //fast an easy way of configuring the SVG coordinates traslation
        //I assume that feature results are almost accurate with the bbox requested
        svgEncoder.setReferenceSpace(getRequest().getBbox());
        svgEncoder.setWidth(String.valueOf(request.getWidth()));
        svgEncoder.setHeight(String.valueOf(request.getHeight()));
        svgEncoder.setWriteHeader(request.getWriteSvgHeader());
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
        svgEncoder.encode(requestedLayers, resultLayers, styles, out);
    }
}
