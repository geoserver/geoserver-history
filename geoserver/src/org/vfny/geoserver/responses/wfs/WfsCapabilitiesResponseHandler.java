/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import com.vividsolutions.jts.geom.Envelope;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.responses.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.io.*;
import java.util.*;


/**
 * Handles a Wfs specific sections of the capabilities response.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: WfsCapabilitiesResponseHandler.java,v 1.1.2.2 2003/11/07 23:04:49 cholmesny Exp $
 */
public class WfsCapabilitiesResponseHandler extends CapabilitiesResponseHandler {
    /**
     * Creates a new WfsCapabilitiesResponseHandler object.
     *
     * @param handler DOCUMENT ME!
     */
    public WfsCapabilitiesResponseHandler(ContentHandler handler) {
        super(handler);
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void startDocument(ServiceConfig config)
        throws SAXException {
        startElement("WFS_Capabilities");
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void endDocument(ServiceConfig config) throws SAXException {
        endElement("WFS_Capabilities");
    }

    /**
     * DOCUMENT ME!
     *
     * @param serviceConfig DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleCapabilities(ServiceConfig serviceConfig)
        throws SAXException {
        WFSConfig config = (WFSConfig) serviceConfig;

        cReturn();

        startElement("Capability");
        indent();
        startElement("Request");

        handleCapability(config, "GetCapabilities");
        handleCapability(config, "DescribeFeatureType");
        handleCapability(config, "GetFeature");
        handleCapability(config, "Transaction");
        handleCapability(config, "LockFeature");
        handleCapability(config, "GetFeatureWithLock");

        endElement("Request");
        unIndent();
        endElement("Capability");

        handleFeatureTypes(config);
    }

    private void handleCapability(WFSConfig config, String capabilityName)
        throws SAXException {
        AttributesImpl attributes = new AttributesImpl();

        indent();
        startElement(capabilityName);
        indent();
        startElement("DCPType");
        startElement("HTTP");

        String url = config.getURL() + "/" + capabilityName + "?";
        attributes.addAttribute("", "onlineResource", "onlineResource", "", url);

        startElement("Get", attributes);
        endElement("Get");
        endElement("HTTP");
        endElement("DCPType");

        cReturn();

        startElement("DCPType");
        startElement("HTTP");
        startElement("Post", attributes);
        endElement("Post");
        endElement("HTTP");
        endElement("DCPType");
        unIndent();
        endElement(capabilityName);
        unIndent();
    }

    private void handleFeatureTypes(ServiceConfig serviceConfig)
        throws SAXException {
        WFSConfig config = (WFSConfig) serviceConfig;

        startElement("FeatureTypeList");

        indent();
        startElement("Operations");
        indent();
        startElement("Query");
        endElement("Query");
        unIndent();
        startElement("Lock");
        endElement("Lock");
        unIndent();
        endElement("Operations");

        Collection featureTypes = server.getCatalog().getFeatureTypes().values();
        FeatureTypeConfig ftype;

        for (Iterator it = featureTypes.iterator(); it.hasNext();) {
            ftype = (FeatureTypeConfig) it.next();

            if (ftype.isEnabled()) { //can't handle ones that aren't enabled.

                //and they shouldn't be handled, as they won't function.
                startElement("FeatureType");
                handleFeatureType(ftype);
                unIndent();
                endElement("FeatureType");
            }
        }

        endElement("FeatureTypeList");
    }
}
