/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses;

import java.io.IOException;

import org.vfny.geoserver.global.*;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import java.util.*;
import com.vividsolutions.jts.geom.Envelope;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: CapabilitiesResponseHandler.java,v 1.3.2.5 2004/01/03 00:20:15 dmzwiers Exp $
 */
public abstract class CapabilitiesResponseHandler extends XmlResponseHandler {
    private static final String EPSG = "EPSG:";

    /** DOCUMENT ME! */
    protected static final GlobalServer server = GlobalServer.getInstance();
    protected static final GlobalCatalog catalog = server.getCatalog();

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
    public void handleDocument(GlobalService config) throws SAXException {
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
    protected void handleService(GlobalService config)
        throws SAXException {
        startElement("ServiceConfig");
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
        handleOnlineResouce(config);

        String fees = config.getFees();

        if ((fees == null) || "".equals(fees)) {
            fees = "NONE";
        }

        handleSingleElem("Fees", fees);
        cReturn();

        String accessConstraints = config.getAccessConstraints();

        if ((accessConstraints == null) || "".equals(accessConstraints)) {
            accessConstraints = "NONE";
        }

        handleSingleElem("AccessConstraints", accessConstraints);
        indent();

        unIndent();
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleOnlineResouce(GlobalService config)
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
    protected abstract void startDocument(GlobalService config)
        throws SAXException;

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void endService(GlobalService config) throws SAXException {
        endElement("ServiceConfig");
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected abstract void handleCapabilities(GlobalService config)
        throws SAXException;

    /**
     * Default handle of a GlobalFeatureType content that writes the latLongBBox as
     * well as the GlobalBasic's parameters
     *
     * @param ftype DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     * @throws IllegalArgumentException if a non-enabled ftype is passed in.
     */
    protected void handleFeatureType(GlobalFeatureType ftype)
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
	 *       response. It should be Keywords instead of Keyword.  For GlobalWMS I
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
