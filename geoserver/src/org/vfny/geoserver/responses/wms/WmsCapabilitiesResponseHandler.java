/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.vfny.geoserver.global.CatalogConfig;
import org.vfny.geoserver.global.ContactConfig;
import org.vfny.geoserver.global.FeatureTypeConfig;
import org.vfny.geoserver.global.ServerConfig;
import org.vfny.geoserver.global.ServiceConfig;
import org.vfny.geoserver.global.WMSConfig;
import org.vfny.geoserver.responses.CapabilitiesResponseHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version $Id: WmsCapabilitiesResponseHandler.java,v 1.3.2.2 2003/12/30 23:08:27 dmzwiers Exp $
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

        //handleSingleElem("!DOCTYPE WMT_MS_Capabilities SYSTEM \"http://www.digitalearth.gov/wmt/xml/capabilities_1_1_1.dtd\"", null);
        //atts.addAttribute("WMT_MS_Capabilities
        //atts = new AttributesImpl();
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
        unIndent();
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

        //        handleLayers(config);
    }

    protected void handleLayers(WMSConfig config) throws SAXException {
        CatalogConfig catalog = server.getCatalog();
        Collection ftypes = catalog.getFeatureTypes().values();
        FeatureTypeConfig layer;

        for (Iterator it = ftypes.iterator(); it.hasNext();) {
            layer = (FeatureTypeConfig) it.next();

            if (layer.isEnabled()) {
                cReturn();
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
        cReturn();
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
        indent();
        handleCapability(config, "GetCapabilities");
        cReturn();
        handleCapability(config, "GetMap");
        unIndent();

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

    protected void handleCapability(WMSConfig config, String capabilityName)
        throws SAXException {
        boolean isPost = false;
        startElement(capabilityName);
        indent();

        if (capabilityName.equals("GetCapabilities")) {
            //HACK - hardcode.  Also, do we actually return this mime-type?
            handleSingleElem("Format", "application/vnd.ogc.wms_xml");
            cReturn();
            isPost = true;
        }

        if (capabilityName.startsWith("GetMap")) {
            Iterator formats = GetMapResponse.getMapFormats().iterator();

            while (formats.hasNext()) {
                handleSingleElem("Format", (String) formats.next());
                cReturn();
            }
        }

        startElement("DCPType");
        indent();
        startElement("HTTP");
        indent();
        startElement("Get");

        String baseUrl = config.getURL();
        String url = baseUrl + "?";
        handleOnlineResource(url);
        endElement("Get");

        if (isPost) {
            startElement("Post");

            String postUrl = baseUrl;
            handleOnlineResource(postUrl);
            endElement("Post");
        }

        unIndent();
        endElement("HTTP");
        unIndent();
        endElement("DCPType");
        unIndent();
        endElement(capabilityName);
    }

    protected void handleOnlineResource(String url) throws SAXException {
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute("", "xmlns:xlink", "xmlns:xlink", "",
            "http://www.w3.org/1999/xlink");
        attributes.addAttribute("", "xlink:type", "xlink:type", "", "simple");
        attributes.addAttribute("", "xlink:href", "xlink:href", "", url);

        indent();
        startElement("OnlineResource", attributes);
        endElement("OnlineResource");
        unIndent();
    }

    protected void handleExceptions(WMSConfig config) throws SAXException {
        cReturn();
        startElement("Exception");
        indent();

        String[] formats = config.getExceptionFormats();

        for (int i = 0; i < formats.length; i++) {
            handleSingleElem("Format", formats[i]);

            if (i < (formats.length - 1)) {
                cReturn();
            }
        }

        unIndent();
        endElement("Exception");
        cReturn();
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

                if (it.hasNext()) {
                    cReturn();
                }
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
