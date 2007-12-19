/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.responses;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.joda.time.Interval;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.CoverageInfoLabelComparator;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.MetaDataLink;
import org.vfny.geoserver.global.Service;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.wcs.requests.WCSRequest;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 */
public class WCSCapsTransformer extends TransformerBase {
    /** DOCUMENT ME! */
    private static final Logger LOGGER = Logger.getLogger(WCSCapsTransformer.class.getPackage()
                                                                                  .getName());
    protected static final String WCS_URI = "http://www.opengis.net/wcs";

    /** DOCUMENT ME! */
    protected static final String CUR_VERSION = "1.0.0";

    /** DOCUMENT ME! */
    protected static final String XSI_PREFIX = "xsi";

    /** DOCUMENT ME! */
    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

    /** DOCUMENT ME! */
    private String baseUrl;
    private ApplicationContext applicationContext;

    /**
     * DOCUMENT ME!
     *
     * @uml.property name="request"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    protected WCSRequest request;

    /**
     * Creates a new WFSCapsTransformer object.
     */
    public WCSCapsTransformer(String baseUrl, ApplicationContext applicationContext) {
        super();

        if (baseUrl == null) {
            throw new NullPointerException();
        }

        this.baseUrl = baseUrl;
        this.setNamespaceDeclarationEnabled(false);
        this.applicationContext = applicationContext;
    }

    /**
     * DOCUMENT ME!
     *
     * @param handler
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Translator createTranslator(ContentHandler handler) {
        return new WCSCapsTranslator(handler, applicationContext);
    }

    /**
     * DOCUMENT ME!
     *
     * @return a Transformer propoerly configured to produce DescribeLayer
     *         responses.
     *
     * @throws TransformerException
     *             if it is thrown by <code>super.createTransformer()</code>
     */
    public Transformer createTransformer() throws TransformerException {
        Transformer transformer = super.createTransformer();
        GeoServer gs = (GeoServer) GeoServerExtensions.extensions(GeoServer.class,
                applicationContext).get(0);
        String dtdUrl = RequestUtils.proxifiedBaseURL(this.baseUrl, gs.getProxyBaseUrl())
            + "schemas/wcs/1.0.0/wcsCapabilities.xsd"; // DJB: fixed
                                                       // this to
                                                       // point to correct
                                                       // location

        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdUrl);

        return transformer;
    }

    /**
     * DOCUMENT ME!
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id
     */
    private static class WCSCapsTranslator extends TranslatorSupport {
        /**
         * DOCUMENT ME!
         *
         * @uml.property name="request"
         * @uml.associationEnd multiplicity="(0 1)"
         */
        private CapabilitiesRequest request;
        private ApplicationContext applicationContext;

        /**
         * Creates a new WFSCapsTranslator object.
         *
         * @param handler
         *            DOCUMENT ME!
         */
        public WCSCapsTranslator(ContentHandler handler, ApplicationContext applicationContext) {
            super(handler, null, null);
            this.applicationContext = applicationContext;
        }

        /**
         * Encode the object.
         *
         * @param o
         *            The Object to encode.
         *
         * @throws IllegalArgumentException
         *             if the Object is not encodeable.
         */
        public void encode(Object o) throws IllegalArgumentException {
            if (!(o instanceof CapabilitiesRequest)) {
                throw new IllegalArgumentException(new StringBuffer("Not a CapabilitiesRequest: ").append(
                        o).toString());
            }

            this.request = (CapabilitiesRequest) o;

            final WCS wcs = (WCS) request.getServiceRef().getServiceRef();

            final AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "version", "version", "", CUR_VERSION);
            attributes.addAttribute("", "xmlns", "xmlns", "", WCS_URI);

            attributes.addAttribute("", "xmlns:xlink", "xmlns:xlink", "",
                "http://www.w3.org/1999/xlink");
            attributes.addAttribute("", "xmlns:ogc", "xmlns:ogc", "", "http://www.opengis.net/ogc");
            attributes.addAttribute("", "xmlns:gml", "xmlns:gml", "", "http://www.opengis.net/gml");

            final String prefixDef = new StringBuffer("xmlns:").append(XSI_PREFIX).toString();
            attributes.addAttribute("", prefixDef, prefixDef, "", XSI_URI);

            final String locationAtt = new StringBuffer(XSI_PREFIX).append(":schemaLocation")
                                                                   .toString();

            final String locationDef = WCS_URI + " "
                + RequestUtils.proxifiedBaseURL(request.getBaseUrl(),
                    wcs.getGeoServer().getProxyBaseUrl()) + "wcs/1.0.0/wcsCapabilities.xsd";

            attributes.addAttribute("", locationAtt, locationAtt, "", locationDef);
            start("WCS_Capabilities", attributes);

            handleService();
            handleCapabilities();

            end("WCS_Capabilities");
        }

        /**
         * Handles the service section of the capabilities document.
         *
         * @param config
         *            The OGC service to transform.
         *
         * @throws SAXException
         *             For any errors.
         */
        private void handleService() {
            final WCS wcs = (WCS) request.getServiceRef().getServiceRef();
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "version", "version", "", CUR_VERSION);
            start("Service", attributes);
            handleMetadataLink(wcs.getMetadataLink());
            element("description", wcs.getAbstract());
            element("name", wcs.getName());
            element("label", wcs.getTitle());
            handleKeywords(wcs.getKeywords());
            handleContact(wcs);

            String fees = wcs.getFees();

            if ((fees == null) || "".equals(fees)) {
                fees = "NONE";
            }

            element("fees", fees);

            String accessConstraints = wcs.getAccessConstraints();

            if ((accessConstraints == null) || "".equals(accessConstraints)) {
                accessConstraints = "NONE";
            }

            element("accessConstraints", accessConstraints);
            end("Service");
        }

        /**
         * DOCUMENT ME!
         *
         * @param serviceConfig
         *            DOCUMENT ME!
         *
         * @throws SAXException
         *             DOCUMENT ME!
         */
        private void handleCapabilities() {
            final WCS wcs = (WCS) request.getServiceRef().getServiceRef();
            start("Capability");
            handleRequest(wcs);
            handleExceptions(wcs);
            handleVendorSpecifics(wcs);
            end("Capability");

            handleContentMetadata(wcs);
        }

        /**
         * Handles the request portion of the document, printing out the
         * capabilities and where to bind to them.
         *
         * @param config
         *            The global wms.
         *
         * @throws SAXException
         *             For any problems.
         */
        private void handleRequest(WCS config) {
            start("Request");
            handleCapability(config, "GetCapabilities");
            handleCapability(config, "DescribeCoverage");
            handleCapability(config, "GetCoverage");
            end("Request");
        }

        private void handleCapability(WCS config, String capabilityName) {
            AttributesImpl attributes = new AttributesImpl();
            start(capabilityName);

            start("DCPType");
            start("HTTP");

            String url = "";
            String baseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(),
                    request.getServiceRef().getGeoServer().getProxyBaseUrl()) + "wcs?SERVICE=WCS&";

            if (request.isDispatchedRequest()) {
                url = new StringBuffer(baseUrl).append("?").toString();
            } else {
                url = new StringBuffer(baseUrl).append("/").append(capabilityName).append("?")
                                               .toString();
            }

            attributes.addAttribute("", "xlink:href", "xlink:href", "", url);

            start("Get");
            start("OnlineResource", attributes);
            end("OnlineResource");
            end("Get");
            end("HTTP");
            end("DCPType");

            attributes = new AttributesImpl();

            if (request.isDispatchedRequest()) {
                url = baseUrl;
            } else {
                url = new StringBuffer(baseUrl).append("/").append(capabilityName).toString();
            }

            attributes.addAttribute("", "xlink:href", "xlink:href", "", url);

            start("DCPType");
            start("HTTP");
            start("Post");
            start("OnlineResource", attributes);
            end("OnlineResource");
            end("Post");
            end("HTTP");
            end("DCPType");
            end(capabilityName);
        }

        /**
         * DOCUMENT ME!
         *
         * @param kwords
         *            DOCUMENT ME!
         *
         * @throws SAXException
         *             DOCUMENT ME!
         */
        private void handleKeywords(List kwords) {
            start("keywords");

            if (kwords != null) {
                for (Iterator it = kwords.iterator(); it.hasNext();) {
                    element("keyword", it.next().toString());
                }
            }

            end("keywords");
        }

        /**
         * Handles contacts.
         *
         * @param config
         *            the service.
         */
        private void handleContact(Service config) {
            String tmp = "";

            if (((config.getGeoServer().getContactPerson() != null)
                    && (config.getGeoServer().getContactPerson() != ""))
                    || ((config.getGeoServer().getContactOrganization() != null)
                    && (config.getGeoServer().getContactOrganization() != ""))) {
                start("responsibleParty");

                tmp = config.getGeoServer().getContactPerson();

                if ((tmp != null) && (tmp != "")) {
                    element("individualName", tmp);

                    tmp = config.getGeoServer().getContactOrganization();

                    if ((tmp != null) && (tmp != "")) {
                        element("organisationName", tmp);
                    }
                } else {
                    tmp = config.getGeoServer().getContactOrganization();

                    if ((tmp != null) && (tmp != "")) {
                        element("organisationName", tmp);
                    }
                }

                tmp = config.getGeoServer().getContactPosition();

                if ((tmp != null) && (tmp != "")) {
                    element("positionName", tmp);
                }

                start("contactInfo");

                start("phone");
                tmp = config.getGeoServer().getContactVoice();

                if ((tmp != null) && (tmp != "")) {
                    element("voice", tmp);
                }

                tmp = config.getGeoServer().getContactFacsimile();

                if ((tmp != null) && (tmp != "")) {
                    element("facsimile", tmp);
                }

                end("phone");

                start("address");
                tmp = config.getGeoServer().getAddressType();

                if ((tmp != null) && (tmp != "")) {
                    String addr = "";
                    addr = config.getGeoServer().getAddress();

                    if ((addr != null) && (addr != "")) {
                        element("deliveryPoint", tmp + " " + addr);
                    }
                } else {
                    tmp = config.getGeoServer().getAddress();

                    if ((tmp != null) && (tmp != "")) {
                        element("deliveryPoint", tmp);
                    }
                }

                tmp = config.getGeoServer().getAddressCity();

                if ((tmp != null) && (tmp != "")) {
                    element("city", tmp);
                }

                tmp = config.getGeoServer().getAddressState();

                if ((tmp != null) && (tmp != "")) {
                    element("administrativeArea", tmp);
                }

                tmp = config.getGeoServer().getAddressPostalCode();

                if ((tmp != null) && (tmp != "")) {
                    element("postalCode", tmp);
                }

                tmp = config.getGeoServer().getAddressCountry();

                if ((tmp != null) && (tmp != "")) {
                    element("country", tmp);
                }

                tmp = config.getGeoServer().getContactEmail();

                if ((tmp != null) && (tmp != "")) {
                    element("electronicMailAddress", tmp);
                }

                end("address");

                tmp = config.getGeoServer().getOnlineResource();

                if ((tmp != null) && (tmp != "")) {
                    AttributesImpl attributes = new AttributesImpl();
                    attributes.addAttribute("", "xlink:href", "xlink:href", "", tmp);
                    start("onlineResource", attributes);
                    end("onlineResource");
                }

                end("contactInfo");

                end("responsibleParty");
            }
        }

        /**
         * Handles the printing of the exceptions information, prints the
         * formats that GeoServer can return exceptions in.
         *
         * @param config
         *            The wms service global config.
         *
         * @throws SAXException
         *             For any problems.
         */
        private void handleExceptions(WCS config) {
            start("Exception");

            final String[] formats = config.getExceptionFormats();
            final int length = formats.length;

            for (int i = 0; i < length; i++) {
                element("Format", formats[i]);
            }

            end("Exception");
        }

        /**
         * Handles the vendor specific capabilities. Right now there are none,
         * so we do nothing.
         *
         * @param config
         *            The global config that may contain vendor specifics.
         *
         * @throws SAXException
         *             For any problems.
         */
        private void handleVendorSpecifics(WCS config) {
        }

        /**
         *
         * @param envelope
         * @deprecated use {@link #handleEnvelope(GeneralEnvelope, Map, Map)}
         *             instead
         */
        private void handleEnvelope(GeneralEnvelope envelope) {
            handleEnvelope(envelope, null, null);
        }

        private void handleEnvelope(GeneralEnvelope lonLatEnvelope, Map verticalExtent,
            Map temporalExtent) {
            AttributesImpl attributes = new AttributesImpl();

            attributes.addAttribute("", "srsName", "srsName", "", /* "urn:ogc:def:crs:OGC:1.3:CRS84" */
                "WGS84(DD)");
            start("lonLatEnvelope", attributes);

            if ((verticalExtent != null) && !verticalExtent.isEmpty()) {
                final List values = (LinkedList) verticalExtent.get("values");
                double firstPosition = 0.0;
                double lastPosition = 0.0;

                try {
                    firstPosition = Double.parseDouble((String) ((LinkedList) values).getFirst());
                } catch (NumberFormatException e) {
                    // TODO: table of possible values
                }

                try {
                    lastPosition = Double.parseDouble((String) ((LinkedList) values).getLast());
                } catch (NumberFormatException e) {
                    // TODO: table of possible values
                }

                element("gml:pos",
                    new StringBuffer(Double.toString(lonLatEnvelope.getLowerCorner().getOrdinate(0))).append(
                        " ").append(lonLatEnvelope.getLowerCorner().getOrdinate(1)).append(" ")
                                                                                                     .append(firstPosition)
                                                                                                     .toString());
                element("gml:pos",
                    new StringBuffer(Double.toString(lonLatEnvelope.getUpperCorner().getOrdinate(0))).append(
                        " ").append(lonLatEnvelope.getUpperCorner().getOrdinate(1)).append(" ")
                                                                                                     .append(lastPosition)
                                                                                                     .toString());
            } else {
                element("gml:pos",
                    new StringBuffer(Double.toString(lonLatEnvelope.getLowerCorner().getOrdinate(0))).append(
                        " ").append(lonLatEnvelope.getLowerCorner().getOrdinate(1)).toString());
                element("gml:pos",
                    new StringBuffer(Double.toString(lonLatEnvelope.getUpperCorner().getOrdinate(0))).append(
                        " ").append(lonLatEnvelope.getUpperCorner().getOrdinate(1)).toString());
            }

            if ((temporalExtent != null) && !temporalExtent.isEmpty()) {
                if (temporalExtent.containsKey("timePeriod")) {
                    final Interval period = (Interval) temporalExtent.get("timePeriod");
                    element("gml:timePosition", period.getStart().toString());
                    element("gml:timePosition", period.getEnd().toString());
                } else if (temporalExtent.containsKey("timePositions")) {
                    final List positions = (LinkedList) temporalExtent.get("timePositions");
                    element("gml:timePosition", ((LinkedList) positions).getFirst().toString());
                    element("gml:timePosition", ((LinkedList) positions).getLast().toString());
                }
            }

            end("lonLatEnvelope");
        }

        /**
         * DOCUMENT ME!
         *
         * @param metadataLink
         *            DOCUMENT ME!
         *
         * @throws SAXException
         *             DOCUMENT ME!
         */
        private void handleMetadataLink(MetaDataLink mdl) {
            if (mdl != null) {
                AttributesImpl attributes = new AttributesImpl();

                if ((mdl.getAbout() != null) && (mdl.getAbout() != "")) {
                    attributes.addAttribute("", "about", "about", "", mdl.getAbout());
                }

                // if( mdl.getType() != null && mdl.getType() != "" ) {
                // attributes.addAttribute("", "type", "type", "",
                // mdl.getType());
                // }
                if ((mdl.getMetadataType() != null) && (mdl.getMetadataType() != "")) {
                    attributes.addAttribute("", "metadataType", "metadataType", "",
                        mdl.getMetadataType());
                }

                if (attributes.getLength() > 0) {
                    start("metadataLink", attributes);
                    // characters(mdl.getContent());
                    end("metadataLink");
                }
            }
        }

        private void handleContentMetadata(WCS config) {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "version", "version", "", CUR_VERSION);

            start("ContentMetadata", attributes);

            List coverages = new ArrayList(config.getData().getCoverageInfos().values());
            Collections.sort(coverages, new CoverageInfoLabelComparator());

            for (Iterator i = coverages.iterator(); i.hasNext();) {
                handleCoverageOfferingBrief(config, (CoverageInfo) i.next());
            }

            end("ContentMetadata");
        }

        private void handleCoverageOfferingBrief(WCS config, CoverageInfo cv) {
            if (cv.isEnabled()) {
                start("CoverageOfferingBrief");

                String tmp;

                handleMetadataLink(cv.getMetadataLink());
                tmp = cv.getDescription();

                if ((tmp != null) && (tmp != "")) {
                    element("description", tmp);
                }

                tmp = cv.getName();

                if ((tmp != null) && (tmp != "")) {
                    element("name", tmp);
                }

                tmp = cv.getLabel();

                if ((tmp != null) && (tmp != "")) {
                    element("label", tmp);
                }

                handleEnvelope(cv.getWGS84LonLatEnvelope(), cv.getVerticalExtent(),
                    cv.getTemporalExtent());
                handleKeywords(cv.getKeywords());

                end("CoverageOfferingBrief");
            }
        }
    }
}
