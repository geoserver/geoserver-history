/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import net.opengis.wcs10.CapabilitiesSectionType;
import net.opengis.wcs10.GetCapabilitiesType;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.ows.util.ResponseUtils;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.logging.Logging;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.CoverageInfoLabelComparator;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.MetaDataLink;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Based on the <code>org.geotools.xml.transform</code> framework, does the
 * job of encoding a WCS 1.0.0 Capabilities document.
 * 
 * @author Alessio Fabiani (alessio.fabiani@gmail.com)
 * @author Simone Giannecchini (simboss1@gmail.com)
 * @author Andrea Aime, TOPP
 */
public class Wcs10CapsTransformer extends TransformerBase {
    private static final Logger LOGGER = Logging.getLogger(Wcs10CapsTransformer.class.getPackage().getName());

    protected static final String WCS_URI = "http://www.opengis.net/wcs";

    protected static final String CUR_VERSION = "1.0.0";

    protected static final String XSI_PREFIX = "xsi";

    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    private WCS wcs;

    private Data catalog;
    
    /**
     * Creates a new WFSCapsTransformer object.
     */
    public Wcs10CapsTransformer(WCS wcs, Data catalog) {
        super();
        this.wcs = wcs;
        this.catalog = catalog;
        setNamespaceDeclarationEnabled(false);
    }

    public Translator createTranslator(ContentHandler handler) {
        return new WCS100CapsTranslator(handler);
    }

    private class WCS100CapsTranslator extends TranslatorSupport {
        /**
         * DOCUMENT ME!
         */
        private GetCapabilitiesType request;

        private String proxifiedBaseUrl;

        /**
         * Creates a new WCS100CapsTranslator object.
         * 
         * @param handler
         *                DOCUMENT ME!
         */
        public WCS100CapsTranslator(ContentHandler handler) {
            super(handler, null, null);
        }

        /**
         * Encode the object.
         * 
         * @param o
         *                The Object to encode.
         * 
         * @throws IllegalArgumentException
         *                 if the Object is not encodeable.
         */
        public void encode(Object o) throws IllegalArgumentException {
            if (!(o instanceof GetCapabilitiesType)) {
                throw new IllegalArgumentException(new StringBuffer(
                        "Not a GetCapabilitiesType: ").append(o).toString());
            }

            this.request = (GetCapabilitiesType) o;

         // check the update sequence
            final int updateSequence = wcs.getGeoServer().getUpdateSequence();
            int requestedUpdateSequence = -1;
            if (request.getUpdateSequence() != null) {
                try {
                    requestedUpdateSequence = Integer.parseInt(request.getUpdateSequence());
                } catch (NumberFormatException e) {
                    if (request.getUpdateSequence().length() == 0)
                        requestedUpdateSequence = 0;
                    else
                        throw new WcsException("Invalid update sequence number format, "
                            + "should be an integer", WcsExceptionCode.InvalidUpdateSequence,
                            "updateSequence");
                }
                if (requestedUpdateSequence > updateSequence) {
                    throw new WcsException("Invalid update sequence value, it's higher "
                            + "than the current value, " + updateSequence,
                            WcsExceptionCode.InvalidUpdateSequence, "updateSequence");
                }
                
                if (requestedUpdateSequence == updateSequence) {
                    throw new WcsException("WCS capabilities document is current (updateSequence = " + updateSequence + ")", WcsExceptionCode.CurrentUpdateSequence, "");
                }
            }

            final AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "version", "version", "", CUR_VERSION);
            attributes.addAttribute("", "xmlns:wcs", "xmlns:wcs", "", WCS_URI);

            attributes.addAttribute("", "xmlns:xlink", "xmlns:xlink", "",
                    "http://www.w3.org/1999/xlink");
            attributes.addAttribute("", "xmlns:ogc", "xmlns:ogc", "",
                    "http://www.opengis.net/ogc");
            attributes.addAttribute("", "xmlns:ows", "xmlns:ows", "",
                    "http://www.opengis.net/ows/1.1");
            attributes.addAttribute("", "xmlns:gml", "xmlns:gml", "",
                    "http://www.opengis.net/gml");

            final String prefixDef = new StringBuffer("xmlns:").append(
                    XSI_PREFIX).toString();
            attributes.addAttribute("", prefixDef, prefixDef, "", XSI_URI);

            final String locationAtt = new StringBuffer(XSI_PREFIX).append(
                    ":schemaLocation").toString();

            proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wcs
                    .getGeoServer().getProxyBaseUrl());
            final String locationDef = WCS_URI + " " + proxifiedBaseUrl
                    + "schemas/wcs/1.0.0/wcsCapabilities.xsd";
            attributes.addAttribute("", locationAtt, locationAtt, "",
                    locationDef);
            attributes.addAttribute("", "updateSequence", "updateSequence", "",
                    String.valueOf(updateSequence));
            start("wcs:WCS_Capabilities", attributes);

            // handle the sections directive
            boolean allSections;
            CapabilitiesSectionType section;
            if (request.getSection() == null) {
                allSections = true;
                section = CapabilitiesSectionType.get("/");
            } else {
                section = request.getSection();
                allSections = (section.get("/").equals(section));
            }
            final Set<String> knownSections = new HashSet<String>(Arrays
                    .asList("/", "/WCS_Capabilities/Service",
                            "/WCS_Capabilities/Capability",
                            "/WCS_Capabilities/ContentMetadata"));

            if (!knownSections.contains(section.getLiteral()))
                throw new WcsException("Unknown section " + section, WcsExceptionCode.InvalidParameterValue, "Sections");

            // encode the actual capabilities contents taking into consideration
            // the sections
            if (requestedUpdateSequence < updateSequence) {
                if (allSections
                        || section.equals(CapabilitiesSectionType.WCS_CAPABILITIES_SERVICE_LITERAL)) {
                    handleService();
                }

                if (allSections
                        || section.equals(CapabilitiesSectionType.WCS_CAPABILITIES_CAPABILITY_LITERAL))
                    handleCapabilities();

                if (allSections
                        || section.equals(CapabilitiesSectionType.WCS_CAPABILITIES_CONTENT_METADATA_LITERAL))
                    handleContentMetadata();
            }

            end("wcs:WCS_Capabilities");
        }

        /**
         * Handles the service section of the capabilities document.
         * 
         * @param config
         *                The OGC service to transform.
         * 
         * @throws SAXException
         *                 For any errors.
         */
        private void handleService() {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "version", "version", "", CUR_VERSION);
            start("wcs:Service", attributes);
            handleMetadataLink(wcs.getMetadataLink());
            element("wcs:description", wcs.getAbstract());
            element("wcs:name", wcs.getName());
            element("wcs:label", wcs.getTitle());
            handleKeywords(wcs.getKeywords());
            handleContact(wcs);

            String fees = wcs.getFees();

            if ((fees == null) || "".equals(fees)) {
                fees = "NONE";
            }

            element("wcs:fees", fees);

            String accessConstraints = wcs.getAccessConstraints();

            if ((accessConstraints == null) || "".equals(accessConstraints)) {
                accessConstraints = "NONE";
            }

            element("wcs:accessConstraints", accessConstraints);
            end("wcs:Service");
        }

        /**
         * DOCUMENT ME!
         * 
         * @param metadataLink
         *                DOCUMENT ME!
         * 
         * @throws SAXException
         *                 DOCUMENT ME!
         */
        private void handleMetadataLink(MetaDataLink mdl) {
            if (mdl != null) {
                AttributesImpl attributes = new AttributesImpl();

                if ((mdl.getAbout() != null) && (mdl.getAbout() != "")) {
                    attributes.addAttribute("", "about", "about", "", mdl
                            .getAbout());
                }

                // if( mdl.getType() != null && mdl.getType() != "" ) {
                // attributes.addAttribute("", "type", "type", "",
                // mdl.getType());
                // }
                if ((mdl.getMetadataType() != null)
                        && (mdl.getMetadataType() != "")) {
                    attributes.addAttribute("", "metadataType", "metadataType",
                            "", mdl.getMetadataType());
                }

                if (attributes.getLength() > 0) {
                    start("wcs:metadataLink", attributes);
                    // characters(mdl.getContent());
                    end("wcs:metadataLink");
                }
            }
        }

        /**
         * DOCUMENT ME!
         * 
         * @param kwords
         *                DOCUMENT ME!
         * 
         * @throws SAXException
         *                 DOCUMENT ME!
         */
        private void handleKeywords(List kwords) {
            start("wcs:keywords");

            if (kwords != null) {
                for (Iterator it = kwords.iterator(); it.hasNext();) {
                    element("wcs:keyword", it.next().toString());
                }
            }

            end("wcs:keywords");
        }

        /**
         * Handles contacts.
         * 
         * @param config
         *                the service.
         */
        private void handleContact(Service config) {
            String tmp = "";

            if (((config.getGeoServer().getContactPerson() != null) && (config
                    .getGeoServer().getContactPerson() != ""))
                    || ((config.getGeoServer().getContactOrganization() != null) && (config
                            .getGeoServer().getContactOrganization() != ""))) {
                start("wcs:responsibleParty");

                tmp = config.getGeoServer().getContactPerson();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:individualName", tmp);

                    tmp = config.getGeoServer().getContactOrganization();

                    if ((tmp != null) && (tmp != "")) {
                        element("wcs:organisationName", tmp);
                    }
                } else {
                    tmp = config.getGeoServer().getContactOrganization();

                    if ((tmp != null) && (tmp != "")) {
                        element("wcs:organisationName", tmp);
                    }
                }

                tmp = config.getGeoServer().getContactPosition();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:positionName", tmp);
                }

                start("wcs:contactInfo");

                start("wcs:phone");
                tmp = config.getGeoServer().getContactVoice();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:voice", tmp);
                }

                tmp = config.getGeoServer().getContactFacsimile();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:facsimile", tmp);
                }

                end("wcs:phone");

                start("wcs:address");
                tmp = config.getGeoServer().getAddressType();

                if ((tmp != null) && (tmp != "")) {
                    String addr = "";
                    addr = config.getGeoServer().getAddress();

                    if ((addr != null) && (addr != "")) {
                        element("wcs:deliveryPoint", tmp + " " + addr);
                    }
                } else {
                    tmp = config.getGeoServer().getAddress();

                    if ((tmp != null) && (tmp != "")) {
                        element("wcs:deliveryPoint", tmp);
                    }
                }

                tmp = config.getGeoServer().getAddressCity();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:city", tmp);
                }

                tmp = config.getGeoServer().getAddressState();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:administrativeArea", tmp);
                }

                tmp = config.getGeoServer().getAddressPostalCode();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:postalCode", tmp);
                }

                tmp = config.getGeoServer().getAddressCountry();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:country", tmp);
                }

                tmp = config.getGeoServer().getContactEmail();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:electronicMailAddress", tmp);
                }

                end("wcs:address");

                tmp = config.getGeoServer().getOnlineResource();

                if ((tmp != null) && (tmp != "")) {
                    AttributesImpl attributes = new AttributesImpl();
                    attributes.addAttribute("", "xlink:href", "xlink:href", "",
                            tmp);
                    start("wcs:onlineResource", attributes);
                    end("wcs:onlineResource");
                }

                end("wcs:contactInfo");

                end("wcs:responsibleParty");
            }
        }

        /**
         * DOCUMENT ME!
         * 
         * @param serviceConfig
         *                DOCUMENT ME!
         * 
         * @throws SAXException
         *                 DOCUMENT ME!
         */
        private void handleCapabilities() {
            start("wcs:Capability");
            handleRequest(wcs);
            handleExceptions(wcs);
            handleVendorSpecifics(wcs);
            end("wcs:Capability");

        }

        /**
         * Handles the request portion of the document, printing out the
         * capabilities and where to bind to them.
         * 
         * @param config
         *                The global wms.
         * 
         * @throws SAXException
         *                 For any problems.
         */
        private void handleRequest(WCS config) {
            start("wcs:Request");
            handleCapability(config, "wcs:GetCapabilities");
            handleCapability(config, "wcs:DescribeCoverage");
            handleCapability(config, "wcs:GetCoverage");
            end("wcs:Request");
        }

        private void handleCapability(WCS config, String capabilityName) {
            AttributesImpl attributes = new AttributesImpl();
            start(capabilityName);

            start("wcs:DCPType");
            start("wcs:HTTP");

            String baseURL = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wcs.getGeoServer().getProxyBaseUrl());
            baseURL = ResponseUtils.appendPath(baseURL, "wcs");

            // ensure ends in "?" or "&"
            baseURL = ResponseUtils.appendQueryString(baseURL, "");

            attributes
                    .addAttribute("", "xlink:href", "xlink:href", "", baseURL);

            start("wcs:Get");
            start("wcs:OnlineResource", attributes);
            end("wcs:OnlineResource");
            end("wcs:Get");
            end("wcs:HTTP");
            end("wcs:DCPType");

            attributes = new AttributesImpl();
            attributes
                    .addAttribute("", "xlink:href", "xlink:href", "", baseURL);

            start("wcs:DCPType");
            start("wcs:HTTP");
            start("wcs:Post");
            start("wcs:OnlineResource", attributes);
            end("wcs:OnlineResource");
            end("wcs:Post");
            end("wcs:HTTP");
            end("wcs:DCPType");
            end(capabilityName);
        }

        /**
         * Handles the printing of the exceptions information, prints the
         * formats that GeoServer can return exceptions in.
         * 
         * @param config
         *                The wms service global config.
         * 
         * @throws SAXException
         *                 For any problems.
         */
        private void handleExceptions(WCS config) {
            start("wcs:Exception");

            final String[] formats = config.getExceptionFormats();
            final int length = formats.length;

            for (int i = 0; i < length; i++) {
                element("wcs:Format", formats[i]);
            }

            end("wcs:Exception");
        }

        /**
         * Handles the vendor specific capabilities. Right now there are none,
         * so we do nothing.
         * 
         * @param config
         *                The global config that may contain vendor specifics.
         * 
         * @throws SAXException
         *                 For any problems.
         */
        private void handleVendorSpecifics(WCS config) {
        }

        private void handleEnvelope(GeneralEnvelope envelope) {
            AttributesImpl attributes = new AttributesImpl();

            attributes.addAttribute("", "srsName", "srsName", "", /* "WGS84(DD)" */ "urn:ogc:def:crs:OGC:1.3:CRS84");
            start("wcs:lonLatEnvelope", attributes);
            element("gml:pos", new StringBuffer(Double.toString(envelope
                    .getLowerCorner().getOrdinate(0))).append(" ").append(
                    envelope.getLowerCorner().getOrdinate(1)).toString());
            element("gml:pos", new StringBuffer(Double.toString(envelope
                    .getUpperCorner().getOrdinate(0))).append(" ").append(
                    envelope.getUpperCorner().getOrdinate(1)).toString());
            end("wcs:lonLatEnvelope");
        }

        private void handleMetadataLink(MetaDataLink mdl, String linkType) {
            if (mdl != null) {
                AttributesImpl attributes = new AttributesImpl();

                if ((mdl.getAbout() != null) && (mdl.getAbout() != "")) {
                    attributes.addAttribute("", "about", "about", "", mdl.getAbout());
                }

                if ((mdl.getMetadataType() != null)
                        && (mdl.getMetadataType() != "")) {
                    attributes.addAttribute("", "xlink:type", "xlink:type", "",
                            linkType);
                }

                if (attributes.getLength() > 0) {
                    element("ows:Metadata", null, attributes);
                }
            }
        }

        private void handleContentMetadata() {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "version", "version", "", CUR_VERSION);

            start("wcs:ContentMetadata", attributes);

            List coverages = new ArrayList(wcs.getData().getCoverageInfos()
                    .values());
            Collections.sort(coverages, new CoverageInfoLabelComparator());
            for (Iterator i = coverages.iterator(); i.hasNext();) {
                handleCoverageOfferingBrief(wcs, (CoverageInfo) i.next());
            }

            end("wcs:ContentMetadata");
        }

        private void handleCoverageOfferingBrief(WCS config, CoverageInfo cv) {
            if (cv.isEnabled()) {
                start("wcs:CoverageOfferingBrief");

                String tmp;

                handleMetadataLink(cv.getMetadataLink());
                tmp = cv.getDescription();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:description", tmp);
                }

                tmp = cv.getName();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:name", tmp);
                }

                tmp = cv.getLabel();

                if ((tmp != null) && (tmp != "")) {
                    element("wcs:label", tmp);
                }

                handleEnvelope(cv.getWGS84LonLatEnvelope());
                handleKeywords(cv.getKeywords());

                end("wcs:CoverageOfferingBrief");
            }
        }

        /**
         * Writes the element if and only if the content is not null and not
         * empty
         * 
         * @param elementName
         * @param content
         */
        private void elementIfNotEmpty(String elementName, String content) {
            if (content != null && !"".equals(content.trim()))
                element(elementName, content);
        }
    }
}
