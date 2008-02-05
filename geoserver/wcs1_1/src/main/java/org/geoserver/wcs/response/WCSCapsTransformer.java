/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs.response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.opengis.wcs.v1_1_1.GetCapabilitiesType;

import org.geoserver.ows.util.RequestUtils;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.util.logging.Logging;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.global.CoverageInfo;
import org.vfny.geoserver.global.CoverageInfoLabelComparator;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.MetaDataLink;
import org.vfny.geoserver.global.WCS;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;


/**
 * Based on the <code>org.geotools.xml.transform</code> framework, does the job
 * of encoding a WCS 1.1 Capabilities document.
 *
 * @author Alessio Fabiani (alessio.fabiani@gmail.com) 
 * @author Simone Giannecchini (simboss1@gmail.com)
 * @author Andrea Aime, TOPP
 */
public class WCSCapsTransformer extends TransformerBase {
    private static final Logger LOGGER = Logging.getLogger(WCSCapsTransformer.class.getPackage().getName());
    protected static final String WCS_URI = "http://www.opengis.net/wcs/1.1.1";

    protected static final String CUR_VERSION = "1.1.1";

    protected static final String XSI_PREFIX = "xsi";

    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";
    
    private WCS wcs;
    private Data catalog;

     /**
     * Creates a new WFSCapsTransformer object.
     */
    public WCSCapsTransformer(WCS wcs, Data catalog) {
        super();
        this.wcs = wcs;
        this.catalog = catalog;
        setNamespaceDeclarationEnabled(false);
    }

    public Translator createTranslator(ContentHandler handler) {
        return new WCS111CapsTranslator(handler);
    }

    private class WCS111CapsTranslator extends TranslatorSupport {
        /**
         * DOCUMENT ME!
         *
         * @uml.property name="request"
         * @uml.associationEnd multiplicity="(0 1)"
         */
        private GetCapabilitiesType request;
        private String proxifiedBaseUrl;

        /**
         * Creates a new WFSCapsTranslator object.
         *
         * @param handler
         *            DOCUMENT ME!
         */
        public WCS111CapsTranslator(ContentHandler handler) {
            super(handler, null, null);
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
            if (!(o instanceof GetCapabilitiesType)) {
                throw new IllegalArgumentException(new StringBuffer("Not a GetCapabilitiesType: ").append(
                        o).toString());
            }

            this.request = (GetCapabilitiesType) o;

            final AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "version", "version", "", CUR_VERSION);
            attributes.addAttribute("", "xmlns:wcs", "xmlns:wcs", "", WCS_URI);

            attributes.addAttribute("", "xmlns:xlink", "xmlns:xlink", "",
                "http://www.w3.org/1999/xlink");
            attributes.addAttribute("", "xmlns:ogc", "xmlns:ogc", "", "http://www.opengis.net/ogc");
            attributes.addAttribute("", "xmlns:ows", "xmlns:ows", "", "http://www.opengis.net/ows/1.1");
            attributes.addAttribute("", "xmlns:gml", "xmlns:gml", "", "http://www.opengis.net/gml");

            final String prefixDef = new StringBuffer("xmlns:").append(XSI_PREFIX).toString();
            attributes.addAttribute("", prefixDef, prefixDef, "", XSI_URI);

            final String locationAtt = new StringBuffer(XSI_PREFIX).append(":schemaLocation")
                                                                   .toString();

            proxifiedBaseUrl = RequestUtils.proxifiedBaseURL(request.getBaseUrl(), wcs.getGeoServer().getProxyBaseUrl());
            final String locationDef = WCS_URI + " "
                + proxifiedBaseUrl
                + "schemas/wcs/1.1.1/wcsGetCapabilities.xsd";
            attributes.addAttribute("", locationAtt, locationAtt, "", locationDef);
            start("wcs:Capabilities", attributes);

            handleServiceIdentification();
            handleServiceProvider();
            handleOperationsMetadata();
            handleContents();

            end("wcs:Capabilities");
        }

        /**
         * Handles the service identification of the capabilities document.
         *
         * @param config
         *            The OGC service to transform.
         *
         * @throws SAXException
         *             For any errors.
         */
        private void handleServiceIdentification() {
            start("ows:ServiceIdentification");
            element("ows:Title", wcs.getTitle());
            element("ows:Abstract", wcs.getAbstract());
            handleKeywords(wcs.getKeywords());
            element("ows:ServiceType", "WCS");
            element("ows:ServiceTypeVersion", "1.1.0");
            element("ows:ServiceTypeVersion", "1.1.1");

            String fees = wcs.getFees();
            if ((fees == null) || "".equals(fees)) {
                fees = "NONE";
            }
            element("ows:Fees", fees);

            String accessConstraints = wcs.getAccessConstraints();
            if ((accessConstraints == null) || "".equals(accessConstraints)) {
                accessConstraints = "NONE";
            }
            element("ows:AccessConstraints", accessConstraints);
            end("ows:ServiceIdentification");
        }
        
        /**
         * Handles the service provider of the capabilities document.
         *
         * @param config
         *            The OGC service to transform.
         *
         * @throws SAXException
         *             For any errors.
         */
        private void handleServiceProvider() {
            start("ows:ServiceProvider");
            element("ows:ProviderName", wcs.getGeoServer().getContactOrganization());
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute("", "xlink:href", "xlink:href", "", wcs.getGeoServer().getOnlineResource());
            element("ows:ProviderSite", null, attributes);
            
            handleContact();
            
            end("ows:ServiceProvider");
        }

        /**
         * Handles the OperationMetadata portion of the document, printing out the
         * operations and where to bind to them.
         *
         * @param config
         *            The global wms.
         *
         * @throws SAXException
         *             For any problems.
         */
        private void handleOperationsMetadata() {
            start("ows:OperationsMetadata");
            handleOperation("GetCapabilities", null);
            handleOperation("DescribeCoverage", null);
            handleOperation("GetCoverage", new HashMap<String, List<String>>() {{
                put("store", Collections.singletonList("False"));
            }});
            
            // specify that we do support xml post encoding, clause 8.3.2.2 of the WCS 1.1.1 spec
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(null, "name", "name", null, "PostEncoding");
            start("ows:Constraint", attributes);
            start("ows:AllowedValues");
            element("ows:Value", "XML");
            end("ows:AllowedValues");    
            end("ows:Constraint");
            
            end("ows:OperationsMetadata");
        }

        private void handleOperation(String capabilityName, Map<String, List<String>> parameters) {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(null, "name", "name", null, capabilityName);
            start("ows:Operation", attributes);
            
            final String url = proxifiedBaseUrl + "wcs?";

            start("ows:DCP");
            start("ows:HTTP");
            attributes = new AttributesImpl();
            attributes.addAttribute("", "xlink:href", "xlink:href", "", url);
            element("ows:Get", null, attributes);
            end("ows:HTTP");
            end("ows:DCP");

            attributes = new AttributesImpl();
            attributes.addAttribute("", "xlink:href", "xlink:href", "", url);
            start("ows:DCP");
            start("ows:HTTP");
            element("ows:Post", null, attributes);
            end("ows:HTTP");
            end("ows:DCP");
            
            if(parameters != null && !parameters.isEmpty()) {
                for (Map.Entry<String, List<String>> param : parameters.entrySet()) {
                    attributes = new AttributesImpl();
                    attributes.addAttribute("", "name", "name", "", param.getKey());
                    start("ows:Parameter", attributes);
                    start("ows:AllowedValues");
                    for (String value : param.getValue()) {
                        element("ows:Value", value);
                    }
                    end("ows:AllowedValues");
                    end("ows:Parameter");
                }
            }
            
            end("ows:Operation");
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
            start("ows:Keywords");

            if (kwords != null) {
                for (Iterator it = kwords.iterator(); it.hasNext();) {
                    element("ows:Keyword", it.next().toString());
                }
            }

            end("ows:Keywords");
        }

        /**
         * Handles contacts.
         *
         * @param wcs
         *            the service.
         */
        private void handleContact() {
            final GeoServer gs = wcs.getGeoServer();
            start("ows:ServiceContact");

            elementIfNotEmpty("ows:IndividualName", gs.getContactPerson());
            elementIfNotEmpty("ows:PositionName", gs.getContactPosition());
            
            start("ows:ContactInfo");
            start("ows:Phone");
            elementIfNotEmpty("ows:Voice", gs.getContactVoice());
            elementIfNotEmpty("ows:Facsimile", gs.getContactFacsimile());
            end("ows:Phone");
            start("ows:Address");
            elementIfNotEmpty("ows:DeliveryPoint", gs.getAddress());
            elementIfNotEmpty("ows:City", gs.getAddressCity());
            elementIfNotEmpty("ows:AdministrativeArea", gs.getAddressState());
            elementIfNotEmpty("ows:PostalCode", gs.getAddressPostalCode());
            elementIfNotEmpty("ows:Country", gs.getAddressCountry());
            elementIfNotEmpty("ows:ElectronicMailAddress", gs.getContactEmail());
            end("ows:Address");

            String or = gs.getOnlineResource();
            if ((or != null) && !"".equals(or.trim())) {
                AttributesImpl attributes = new AttributesImpl();
                attributes.addAttribute("", "xlink:href", "xlink:href", "", or);
                start("ows:OnlineResource", attributes);
                end("OnlineResource");
            }

            end("ows:ContactInfo");
            end("ows:ServiceContact");
        }

        private void handleEnvelope(GeneralEnvelope envelope) {
            start("ows:WGS84BoundingBox");
            element("ows:LowerCorner",
                new StringBuffer(Double.toString(envelope.getLowerCorner().getOrdinate(0))).append(
                    " ").append(envelope.getLowerCorner().getOrdinate(1)).toString());
            element("ows:UpperCorner",
                new StringBuffer(Double.toString(envelope.getUpperCorner().getOrdinate(0))).append(
                    " ").append(envelope.getUpperCorner().getOrdinate(1)).toString());
            end("ows:WGS84BoundingBox");
        }

        private void handleContents() {
            start("wcs:Contents");

            List coverages = new ArrayList(wcs.getData().getCoverageInfos().values());
            Collections.sort(coverages, new CoverageInfoLabelComparator());
            for (Iterator i = coverages.iterator(); i.hasNext();) {
                CoverageInfo cv = (CoverageInfo) i.next();
                if(cv.isEnabled())
                    handleCoverageSummary(cv);
            }
            
            end("wcs:Contents");
        }

        private void handleCoverageSummary(CoverageInfo cv) {
                start("wcs:CoverageSummary");
                elementIfNotEmpty("ows:Title", cv.getLabel());
                elementIfNotEmpty("ows:Abstract", cv.getDescription());
                handleKeywords(cv.getKeywords());
                handleMetadataLink(cv.getMetadataLink(), "simple");
                handleEnvelope(cv.getWGS84LonLatEnvelope());
                element("wcs:Identifier", cv.getName());

                end("wcs:CoverageSummary");
        }
        
        private void handleMetadataLink(MetaDataLink mdl, String linkType) {
            if (mdl != null) {
                AttributesImpl attributes = new AttributesImpl();

                if ((mdl.getAbout() != null) && (mdl.getAbout() != "")) {
                    attributes.addAttribute("", "about", "about", "", mdl.getAbout());
                }

                if ((mdl.getMetadataType() != null) && (mdl.getMetadataType() != "")) {
                    attributes.addAttribute("", "xlink:type", "xlink:type", "",
                        linkType);
                }

                if (attributes.getLength() > 0) {
                    element("ows:Metadata", null, attributes);
                }
            }
        }
        
        /**
         * Writes the element if and only if the content is not null and not empty
         * @param elementName
         * @param content
         */
        private void elementIfNotEmpty(String elementName, String content) {
            if(content != null && !"".equals(content.trim()))
                element(elementName, content);
        }
    }
}
