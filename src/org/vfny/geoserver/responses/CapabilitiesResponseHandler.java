/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import com.vividsolutions.jts.geom.Envelope;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


/**
 * Base class for the handling of the WFS and WMS capabilities responses.
 * Attempts to put the functionality that both use here, but in practice it
 * ends up being closer to WFS, with WMS over-writing.
 *
 * @author Gabriel Roldán
 * @version $Id: CapabilitiesResponseHandler.java,v 1.15 2004/09/05 17:17:58 cholmesny Exp $
 */
public abstract class CapabilitiesResponseHandler extends XmlResponseHandler {
    private static final String EPSG = "EPSG:";

    /**
     * Creates a new CapabilitiesResponseHandler object.
     *
     * @param contentHandler The content handler to recieve SAX calls.
     */
    public CapabilitiesResponseHandler(ContentHandler contentHandler) {
        super(contentHandler);
    }

    /**
     * Turns the Service passed in into a series of SAX production calls for
     * the content handler passed in.
     *
     * @param config The service to transform.
     *
     * @throws SAXException For any errors.
     */
    public void handleDocument(Service config) throws SAXException {
        startDocument(config);
        indent();
        handleService(config);

        //endService(config);
        handleCapabilities(config);
    }

    /**
     * Handles the service section of the capabilities document.
     *
     * @param config The OGC service to transform.
     *
     * @throws SAXException For any errors.
     */
    protected void handleService(Service config) throws SAXException {
        startService(config);
        indent();
        handleSingleElem("Name", config.getName());
        cReturn();
        handleSingleElem("Title", config.getTitle());
        cReturn();
        handleSingleElem("Abstract", config.getAbstract());
        cReturn();
        handleKeywords(config.getKeywords());
        cReturn();
        unIndent();
        handleOnlineResource(config);

        String fees = config.getFees();

        if ((fees == null) || "".equals(fees)) {
            fees = "NONE";
        }

        handleContact(config);

        handleSingleElem("Fees", fees);
        cReturn();

        String accessConstraints = config.getAccessConstraints();

        if ((accessConstraints == null) || "".equals(accessConstraints)) {
            accessConstraints = "NONE";
        }

        handleSingleElem("AccessConstraints", accessConstraints);

        endService(config);
    }

    protected void startService(Service config) throws SAXException {
        startElement("Service");
    }

    protected abstract void handleContact(Service config)
        throws SAXException;

    /**
     * Handles an online resource section.
     *
     * @param config The service to get the online resource from.
     *
     * @throws SAXException For any errors.
     */
    protected void handleOnlineResource(Service config)
        throws SAXException {
        Object o = config.getOnlineResource();

        if (o != null) {
            handleSingleElem("OnlineResource", o.toString());
        }
    }

    /**
     * 
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected abstract void startDocument(Service config)
        throws SAXException;

    /**
     * Ends the service section.
     *
     * @param config The service for configuration information.
     *
     * @throws SAXException for any errors.
     */
    protected void endService(Service config) throws SAXException {
        endElement("Service");
    }

    /**
     * Handles the capabilities section of the capabilities document.
     *
     * @param config The service for configuration information.
     *
     * @throws SAXException for any errors.
     */
    protected abstract void handleCapabilities(Service config)
        throws SAXException;

    /**
     * Default handle of a FeatureTypeInfo content that writes the latLongBBox
     * as well as the GlobalBasic's parameters
     *
     * @param ftype The FeatureType configuration to report capabilities on.
     *
     * @throws SAXException For any errors.
     * @throws IllegalArgumentException if a non-enabled ftype is passed in.
     */
    protected void handleFeatureType(FeatureTypeInfo ftype)
        throws SAXException {
        if (!ftype.isEnabled()) {
            throw new IllegalArgumentException("FeatureTypeConfig " + ftype
                + " is not " + "enabled, check config.");
        }

        Envelope bbox = null;

        try {
            bbox = ftype.getLatLongBoundingBox();
        } catch (IOException ex) {
            throw new SAXException("Can't obtain latLongBBox of "
                + ftype.getName() + ": " + ex.getMessage(), ex);
        }

        indent();
        handleSingleElem("Name", ftype.getName());
        cReturn();
        handleSingleElem("Title", ftype.getTitle());
        cReturn();
        handleSingleElem("Abstract", ftype.getAbstract());
        cReturn();
        handleKeywords(ftype.getKeywords());
        cReturn();
        unIndent();

        //indent();
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

        handleLatLongBbox(bboxAtts);
    }

    protected void handleLatLongBbox(AttributesImpl bboxAtts)
        throws SAXException {
        startElement("LatLongBoundingBox", bboxAtts);
        endElement("LatLongBoundingBox");
    }

    /**
     * Handles a keyword list.
     *
     * @param kwords The list of key words.
     *
     * @throws SAXException DOCUMENT ME!
     *
     * @task REVISIT: I don't think this is currently right for wms or wfs
     *       service elements.  I'm just subclassing for WfsCapabilities
     *       response. It should be Keywords instead of Keyword.  For WMS I
     *       think it should be KeywordList or something to that effect, with
     *       individual keywords delimited by keyword elements.  So I'm not
     *       sure what should go here by default, perhaps should just remain
     *       abstract.
     */
    protected void handleKeywords(List kwords) throws SAXException {
        startElement("Keywords");

        if (kwords != null) {
            for (Iterator it = kwords.iterator(); it.hasNext();) {
                characters(it.next().toString());

                if (it.hasNext()) {
                    characters(", ");
                }
            }
        }

        endElement("Keywords");
    }
}
