/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.map;

import org.geotools.data.*;

import org.vfny.geoserver.global.*;
import org.geotools.styling.*;
import org.vfny.geoserver.responses.wms.map.svg.*;
import org.vfny.geoserver.*;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.requests.Request;
import org.vfny.geoserver.requests.wms.GetMapRequest;
import org.vfny.geoserver.responses.Response;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;

/**
 * Handles a GetMap request that spects a map in SVG format.
 *
 * @author Gabriel Roldán
 * @version $Id: SVGMapResponse.java,v 1.9 2004/03/14 16:15:22 groldan Exp $
 */
public class SVGMapResponse extends GetMapDelegate {

    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.responses.wms.map");
    /** DOCUMENT ME!  */
    private static final String MIME_TYPE = "image/svg+xml";

    private EncodeSVG svgEncoder;

    public SVGMapResponse(){

    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getContentType(GeoServer gs) {
        return MIME_TYPE;
    }

    public String getContentEncoding(){
      return null;
    }

    public List getSupportedFormats()
    {
      return Collections.singletonList(MIME_TYPE);
    }

    /**
     * evaluates if this Map producer can generate the map format specified
     * by <code>mapFormat</code>
     *
     * @param mapFormat the mime type of the output map format requiered
     *
     * @return true if class can produce a map in the passed format
     */
    public boolean canProduce(String mapFormat)
    {
      return mapFormat.startsWith("image/svg");
    }


    public void abort()
    {
      LOGGER.fine("aborting SVG map response");
      if(svgEncoder != null)
      {
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

        EncoderConfig encoderData = new EncoderConfig(request,
                                                      requestedLayers,
                                                      resultLayers,
                                                      styles);
        this.svgEncoder = new EncodeSVG(encoderData);
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     *
     * @throws ServiceException DOCUMENT ME!
     * @throws WmsException DOCUMENT ME!
     */
    public void writeTo(OutputStream out) throws ServiceException, IOException {
        svgEncoder.encode(out);
    }
}
