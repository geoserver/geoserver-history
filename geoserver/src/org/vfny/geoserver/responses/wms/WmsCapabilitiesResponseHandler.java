/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import org.vfny.geoserver.*;
import org.vfny.geoserver.config.*;
import org.vfny.geoserver.responses.*;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.*;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: WmsCapabilitiesResponseHandler.java,v 1.2 2003/12/16 18:46:10 cholmesny Exp $
 */
public class WmsCapabilitiesResponseHandler extends CapabilitiesResponseHandler {
    private static final String CAP_VERSION = ServerConfig.getInstance()
                                                          .getWMSConfig()
                                                          .getVersion();

    /**
     * Creates a new WmsCapabilitiesResponseHandler object.
     *
     * @param handler DOCUMENT ME!
     */
    public WmsCapabilitiesResponseHandler(ContentHandler handler) {
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
        WMSConfig wmsConfig = (WMSConfig) config;

        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("", "version", "version", "", CAP_VERSION);
        atts.addAttribute("", "", "updateSequence", "updateSequence",
            wmsConfig.getUpdateTime());
        startElement("WMT_MS_Capabilities", atts);
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void endDocument(ServiceConfig config) throws SAXException {
        endElement("WMT_MS_Capabilities");
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void startService(ServiceConfig config) throws SAXException {
        startElement("Service");
    }

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
    public void handleService(ServiceConfig config) throws SAXException {
        super.handleService(config);
        indent();
        handleContactInformation(config);
        unIndent();
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
        WMSConfig config = (WMSConfig) serviceConfig;

        cReturn();

        startElement("Capability");
        indent();
        handleRequest(config);
        handleExceptions(config);
        handleVendorSpecifics(config);
        handleSLD(config);
        handleLayers(config);
        unIndent();
        endElement("Capability");

        handleLayers(config);
    }

    protected void handleLayers(WMSConfig config) throws SAXException {
        CatalogConfig catalog = server.getCatalog();
        Collection ftypes = catalog.getFeatureTypes().values();
        FeatureTypeConfig layer;

        for (Iterator it = ftypes.iterator(); it.hasNext();) {
            layer = (FeatureTypeConfig) it.next();
            if(layer.isEnabled())
            {
              startElement("Layer");
              indent();
              handleFeatureType(layer);
              unIndent();
              endElement("Layer");
            }
        }
    }

    /**
     * calls super.handleFeatureType to add common FeatureType content such as
     * Name, Title and LatLonBoundingBox, and then writes WMS specific layer
     * properties as Styles, Scale Hint, etc.
     *
     * @param ftype DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleFeatureType(FeatureTypeConfig ftype)
        throws SAXException {
        super.handleFeatureType(ftype);
    }

    protected void handleSLD(WMSConfig config) throws SAXException {
        AttributesImpl sldAtts = new AttributesImpl();
        String supportsSLD = config.supportsSLD() ? "1" : "0";
        String supportsUserLayer = config.supportsUserLayer() ? "1" : "0";
        String supportsUserStyle = config.supportsUserStyle() ? "1" : "0";
        String supportsRemoteWFS = config.supportsRemoteWFS() ? "1" : "0";

        sldAtts.addAttribute("", "SupportsSLD", "SupportsSLD", "", supportsSLD);
        sldAtts.addAttribute("", "UserLayer", "UserLayer", "", supportsUserLayer);
        sldAtts.addAttribute("", "UserStyle", "UserStyle", "", supportsUserStyle);
        sldAtts.addAttribute("", "RemoteWFS", "RemoteWFS", "", supportsRemoteWFS);

        startElement("UserDefinedSymbolization", sldAtts);
        endElement("UserDefinedSymbolization");
    }

    protected void handleVendorSpecifics(WMSConfig config)
        throws SAXException {
        startElement("VendorSpecificCapabilities");
        endElement("VendorSpecificCapabilities");
    }

    protected void handleRequest(WMSConfig config) throws SAXException {
        startElement("Request");

        /*
           handleCapability(config, "GetCapabilities");
           handleCapability(config, "DescribeFeatureType");
           handleCapability(config, "GetFeature");
           handleCapability(config, "Transaction");
           handleCapability(config, "LockFeature");
           handleCapability(config, "GetFeatureWithLock");
         */
        endElement("Request");
    }

    protected void handleExceptions(WMSConfig config) throws SAXException {
        startElement("Exception");
        indent();

        String[] formats = config.getExceptionFormats();

        for (int i = 0; i < formats.length; i++) {
            handleSingleElem("Format", formats[i]);
            cReturn();
        }

        unIndent();
        endElement("Exception");
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleContactInformation(ServiceConfig config)
        throws SAXException {
        ContactConfig contact = server.getGlobalConfig().getContactInformation();
        startElement("ContactInformation");
        indent();
        startElement("ContactPersonPrimary");
        indent();
        handleSingleElem("ContactPerson", contact.getContactPerson());
        handleSingleElem("ContactOrganization", contact.getContactOrganization());
        unIndent();
        endElement("ContactPersonPrimary");

        startElement("ContactAddress");
        indent();
        handleSingleElem("AddressType", contact.getAddressType());
        handleSingleElem("Address", contact.getAddress());
        handleSingleElem("City", contact.getAddressCity());
        handleSingleElem("StateOrProvince", contact.getAddressState());
        handleSingleElem("PostCode", contact.getAddressPostalCode());
        handleSingleElem("Country", contact.getAddressCountry());
        unIndent();
        endElement("ContactAddress");

        handleSingleElem("ContactVoiceTelephone", contact.getContactVoice());
        handleSingleElem("ContactFacsimileTelephone",
            contact.getContactFacsimile());
        handleSingleElem("ContactElectronicMailAddress",
            contact.getContactEmail());

        unIndent();
        endElement("ContactInformation");
    }

    /**
     * Overrides BasicConfig.handleKeywords to write the keywords list in WMS
     * style
     *
     * @param kwords DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleKeywords(List kwords) throws SAXException {
        startElement("KeywordList");

        if (kwords != null) {
            indent();

            for (Iterator it = kwords.iterator(); it.hasNext();) {
                startElement("Keyword");
                characters(String.valueOf(it.next()));
                endElement("Keyword");
            }

            unIndent();
        }

        endElement("KeywordList");
    }

    /**
     * Overrides CapabilitiesResponseHandler.handlerOnlineResource to write WMS
     * style service online resource
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleOnlineResouce(ServiceConfig config)
        throws SAXException {
        String url = config.getOnlineResource();
        AttributesImpl olAtts = new AttributesImpl();
        String xlinkNs = "http://www.w3c.org/1999/xlink";

        olAtts.addAttribute("", "xlink", "xmlns:xlink", "", xlinkNs);

        olAtts.addAttribute(xlinkNs, "type", "xlink:type", "", "simple");

        olAtts.addAttribute(xlinkNs, "href", "xlink:href", "", url);

        startElement("OnlineResource", olAtts);
        endElement("OnlineResource");
    }
}
