/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import org.geotools.data.*;
import org.geotools.svg.SVGEncoder;
import org.vfny.geoserver.*;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import org.vfny.geoserver.responses.Response;
import java.io.*;
import java.util.*;


/**
 * Handles a GetMap request that spects a map in SVG format.
 *
 * @author Gabriel Roldán
 * @version $Id: SVGMapResponse.java,v 1.1.2.2 2003/11/16 19:29:39 groldan Exp $
 */
public class SVGMapResponse extends GetMapDelegate {
    /** DOCUMENT ME!  */
    private static final String MIME_TYPE = "image/svg+xml";
    private SVGEncoder svgEncoder;
    private FeatureTypeConfig[] requestedLayers;
    private FeatureResults[] resultLayers;
    private List styles;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType() {
        return MIME_TYPE;
    }

    public void abort()
    {

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
    protected void execute(FeatureTypeConfig[] requestedLayers,
        FeatureResults[] resultLayers, List styles) throws WmsException {
        GetMapRequest request = getRequest();
        this.requestedLayers = requestedLayers;
        this.resultLayers = resultLayers;
        this.styles = styles;
        this.svgEncoder = new SVGEncoder();

        //fast an easy way of configuring the SVG coordinates traslation
        //I assume that feature results are almost accurate with the bbox requested
        //svgEncoder.setReferenceSpace(getRequest().getBbox());
        svgEncoder.setWidth(String.valueOf(request.getWidth()));
        svgEncoder.setHeight(String.valueOf(request.getHeight()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WmsException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException {
        FeatureResults layer;

        try {
            Writer writer = new OutputStreamWriter(out);
            svgEncoder.encode(requestedLayers, resultLayers, writer);
        } catch (IOException ex) {
            throw new WmsException(ex, "Error writing SVG",
                getClass().getName() + "::writeTo()");
        }
    }
}
