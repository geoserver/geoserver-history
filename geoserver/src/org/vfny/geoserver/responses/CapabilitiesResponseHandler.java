/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import com.vividsolutions.jts.geom.Envelope;
import org.vfny.geoserver.config.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public abstract class CapabilitiesResponseHandler extends ConfigResponseHandler {
    private static final String EPSG = "EPSG:";

    /** DOCUMENT ME! */
    protected static final ServerConfig server = ServerConfig.getInstance();

    /**
     * Creates a new CapabilitiesResponseHandler object.
     *
     * @param contentHandler DOCUMENT ME!
     */
    public CapabilitiesResponseHandler(ContentHandler contentHandler) {
        super(contentHandler);
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void handleDocument(ServiceConfig config) throws SAXException {
        startDocument(config);
        indent();
        handleService(config);
        endService(config);
        handleCapabilities(config);
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleService(ServiceConfig config)
        throws SAXException {
        startElement("Service");
        handleConfig((BasicConfig) config);
        indent();
        handleOnlineResouce(config);
        unIndent();
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleOnlineResouce(ServiceConfig config)
        throws SAXException {
        handleSingleElem("OnlineResource", config.getOnlineResource());
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected abstract void startDocument(ServiceConfig config)
        throws SAXException;

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void endService(ServiceConfig config) throws SAXException {
        endElement("Service");
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected abstract void handleCapabilities(ServiceConfig config)
        throws SAXException;

    /**
     * Default handle of a FeatureType content that writes the latLongBBox as
     * well as the BasicConfig's parameters
     *
     * @param ftype DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleFeatureType(FeatureTypeConfig ftype)
        throws SAXException {
        Envelope bbox = null;

        try {
            bbox = ftype.getLatLongBoundingBox();
        } catch (IOException ex) {
            throw new SAXException("Can't obtain latLongBBox of "
                + ftype.getName() + ": " + ex.getMessage(), ex);
        }

        handleConfig((BasicConfig) ftype);
        indent();

        /**
         * @task REVISIT: should getSRS() return the full URL?
         */
        handleSingleElem("SRS", EPSG + ftype.getSRS());
        cReturn();

        String minx = String.valueOf(bbox.getMinX());
        String miny = String.valueOf(bbox.getMinY());
        String maxx = String.valueOf(bbox.getMaxX());
        String maxy = String.valueOf(bbox.getMaxY());

        AttributesImpl bboxAtts = new AttributesImpl();
        bboxAtts.addAttribute("", "minx", "minx", "", minx);
        bboxAtts.addAttribute("", "miny", "miny", "", miny);
        bboxAtts.addAttribute("", "maxx", "maxx", "", maxx);
        bboxAtts.addAttribute("", "maxy", "maxy", "", maxy);

        startElement("LatLongBoundingBox", bboxAtts);
        endElement("LatLongBoundingBox");
    }
}
