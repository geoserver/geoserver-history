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
 * Handler methods to print out a valid wms capabilities document from the WMS
 * config.  Will be called by a transformer in Capabilities response, and
 * should turn itself into xml events that will then be consumed by a writer
 * and written to the output stream (ie sent to the client).
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: WmsCapabilitiesResponseHandler.java,v 1.4 2004/01/02 23:29:37 cholmesny Exp $
 */
public class WmsCapabilitiesResponseHandler extends CapabilitiesResponseHandler {
    private static final String CAP_VERSION = ServerConfig.getInstance()
                                                          .getWMSConfig()
                                                          .getVersion();
    protected String BBOX_ELEM_NAME = "LatLonBoundingBox";

    /**
     * Creates a new WmsCapabilitiesResponseHandler object.
     *
     * @param handler DOCUMENT ME!
     */
    public WmsCapabilitiesResponseHandler(ContentHandler handler) {
        super(handler);
    }

    /**
     * Prints the dtd declaration and root WMT_MS_Capabilities declaration.
     *
     * @param config Must be an instance of WMSConfig.  If not a class cast
     *        exception will be thrown.
     *
     * @throws SAXException For any problems creating the SAX.
     *
     * @task TODO: replace the digitalearth.gov dtd with our own locally
     *       referenced one.  To do this we will need to modify the build.xml
     *       file to copy it to the war.
     */
    protected void startDocument(ServiceConfig config)
        throws SAXException {
        WMSConfig wmsConfig = (WMSConfig) config;

        AttributesImpl atts = new AttributesImpl();

        startElement("!DOCTYPE WMT_MS_Capabilities SYSTEM \"http://www"
            + ".digitalearth.gov/wmt/xml/capabilities_1_1_1.dtd\"");

        atts.addAttribute("", "version", "version", "", CAP_VERSION);
        atts.addAttribute("", "", "updateSequence", "updateSequence",
            wmsConfig.getUpdateTime());
        startElement("WMT_MS_Capabilities", atts);
    }

    /**
     * Closes the WMT_MS_Capabilities element.
     *
     * @param config The wms config used to start the document.
     *
     * @throws SAXException For any problems.
     */
    public void endDocument(ServiceConfig config) throws SAXException {
        unIndent();
        endElement("WMT_MS_Capabilities");
    }

    /**
     * Starts the Service element.
     *
     * @param config The Wms config to turn into a capabilities document.
     *
     * @throws SAXException If anything goes wrong.
     */
    protected void startService(ServiceConfig config) throws SAXException {
        startElement("Service");
    }

    /**
     * Calls endElement for Service
     *
     * @param config The Wms config to turn into a capabilities document.
     *
     * @throws SAXException For any problems.
     */
    protected void endService(ServiceConfig config) throws SAXException {
        endElement("Service");
    }

    /**
     * Handles the service section of the document.  Just calls super which
     * calls the appropriate sub methods.
     *
     * @param config The Wms config to turn into a capabilities document.
     *
     * @throws SAXException For any problems.
     */
    public void handleService(ServiceConfig config) throws SAXException {
        super.handleService(config);
    }

    /**
     * Handles the capabilities section - request, exceptions, vendor, sld and
     * layers - of the caps document.
     *
     * @param serviceConfig The Wms config to turn into a capabilities
     *        document.
     *
     * @throws SAXException If anything goes wrong.
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
    }

    /**
     * Handles the layers portion of the document.  Prints a root layer that
     * all others descend from, as it appears that only one layer element is
     * allowed.  For now just calls handle config, which prints out the
     * server's title, abstract and keywords, which are all valid elements.
     *
     * @param config The Wms config to handle the layers of.
     *
     * @throws SAXException For any problems.
     *
     * @task REVISIT: print out a more wms appropriate root list.  Perhaps
     *       consider putting 4326 (latlon) for the SRS.  I'm not sure if
     *       child layers override or inherit.
     */
    protected void handleLayers(WMSConfig config) throws SAXException {
        CatalogConfig catalog = server.getCatalog();
        Collection ftypes = catalog.getFeatureTypes().values();
        FeatureTypeConfig layer;
        cReturn();
        startElement("Layer");
        handleConfig(config);

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

        endElement("Layer");
    }

    /**
     * Calls super.handleFeatureType to add common FeatureType content such as
     * Name, Title and LatLonBoundingBox, and then writes WMS specific layer
     * properties as Styles, Scale Hint, etc.
     *
     * @param ftype The featureType to write out.
     *
     * @throws SAXException For any problems.
     *
     * @task TODO: write wms specific elements.
     */
    protected void handleFeatureType(FeatureTypeConfig ftype)
        throws SAXException {
        super.handleFeatureType(ftype);
    }

    /**
     * Handles the sld styling support provided by the server.
     *
     * @param config The WMS config to get sld info from.
     *
     * @throws SAXException For any problems.
     */
    protected void handleSLD(WMSConfig config) throws SAXException {
        AttributesImpl sldAtts = new AttributesImpl();
        String supportsSLD = config.supportsSLD() ? "1" : "0";
        String supportsUserLayer = config.supportsUserLayer() ? "1" : "0";
        String supportsUserStyle = config.supportsUserStyle() ? "1" : "0";
        String supportsRemoteWFS = config.supportsRemoteWFS() ? "1" : "0";

        sldAtts.addAttribute("", "SupportSLD", "SupportSLD", "", supportsSLD);
        sldAtts.addAttribute("", "UserLayer", "UserLayer", "", supportsUserLayer);
        sldAtts.addAttribute("", "UserStyle", "UserStyle", "", supportsUserStyle);
        sldAtts.addAttribute("", "RemoteWFS", "RemoteWFS", "", supportsRemoteWFS);
        cReturn();
        startElement("UserDefinedSymbolization", sldAtts);
        endElement("UserDefinedSymbolization");
    }

    /**
     * Handles the vendoer specific capabilities.  Right now there are none, so
     * we do nothing.
     *
     * @param config The global config that may contain vendor specifics.
     *
     * @throws SAXException For any problems.
     */
    protected void handleVendorSpecifics(WMSConfig config)
        throws SAXException {
        //startElement("VendorSpecificCapabilities");
        //endElement("VendorSpecificCapabilities");
    }

    /**
     * Handles the request portion of the document, printing out the
     * capabilities and where to bind to them.
     *
     * @param config The global wms.
     *
     * @throws SAXException For any problems.
     */
    protected void handleRequest(WMSConfig config) throws SAXException {
        startElement("Request");
        indent();
        handleCapability(config, "GetCapabilities");
        cReturn();
        handleCapability(config, "GetMap");
        unIndent();
        endElement("Request");
    }

    /**
     * Handles an individual capability, printing where it can be found and the
     * formats it supports.
     *
     * @param config The wms service config.
     * @param capabilityName The name of the capability to print out.
     *
     * @throws SAXException For any problems.
     */
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

    /**
     * Handles the printing of the exceptions information, prints the formats
     * that GeoServer can return exceptions in.
     *
     * @param config The wms service global config.
     *
     * @throws SAXException For any problems.
     */
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
     * Handles the contact information portion of the service section.
     *
     * @param config The global service information.
     *
     * @throws SAXException For any problems.
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
     * @param kwords A list of keywords to print out.  If null or of size 0
     *        then nothing will be printed, since the keyword(s) element is
     *        not mandatory.
     *
     * @throws SAXException For any problems.
     */
    protected void handleKeywords(List kwords) throws SAXException {
        if ((kwords != null) && (kwords.size() > 0)) {
            startElement("KeywordList");
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

            endElement("KeywordList");
        }
    }

    /**
     * Overrides CapabilitiesResponseHandler.handlerOnlineResource to write WMS
     * style service online resource
     *
     * @param config The WMS config to write out an online resource from.
     *
     * @throws SAXException For any problems.
     */
    protected void handleOnlineResource(ServiceConfig config)
        throws SAXException {
        String url = config.getOnlineResource();
        handleOnlineResource(url);
    }

    /**
     * Convenience method to print the appropriate xlink attributes for an
     * online resource.
     *
     * @param url The url to be linked to.
     *
     * @throws SAXException For any problems.
     */
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

    /**
     * Gets the name of the WMS bbox element name.
     *
     * @return The string 'LatLonBoundingBox', as that's what wms uses.
     */
    protected String getBboxElementName() {
        return "LatLonBoundingBox";
    }
}
