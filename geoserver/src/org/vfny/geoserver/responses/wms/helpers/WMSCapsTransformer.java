/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.helpers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.geotools.styling.Style;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.global.LegendURL;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.requests.CapabilitiesRequest;
import org.vfny.geoserver.responses.wms.DescribeLayerResponse;
import org.vfny.geoserver.responses.wms.GetFeatureInfoResponse;
import org.vfny.geoserver.responses.wms.GetMapResponse;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;

import com.vividsolutions.jts.geom.Envelope;


/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id
 */
public class WMSCapsTransformer extends TransformerBase {
    /** fixed MIME type for the returned capabilities document */
    public static final String WMS_CAPS_MIME = "application/vnd.ogc.wms_xml";

    /** DOCUMENT ME! */
    private String schemaBaseUrl;

    /**
     * Creates a new WMSCapsTransformer object.
     *
     * @param schemaBaseUrl needed to get the schema base URL
     *
     * @throws NullPointerException if <code>schemaBaseUrl</code> is null;
     */
    public WMSCapsTransformer(String schemaBaseUrl) {
        super();

        if (schemaBaseUrl == null) {
            throw new NullPointerException();
        }

        this.schemaBaseUrl = schemaBaseUrl;
        this.setNamespaceDeclarationEnabled(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param handler DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Translator createTranslator(ContentHandler handler) {
        return new CapabilitiesTranslator(handler);
    }

    /**
     * Gets the <code>Transformer</code> created by the overriden method in the
     * superclass and adds it the system DOCTYPE token pointing to the
     * Capabilities DTD on this server instance.
     * 
     * <p>
     * The DTD is set at the fixed location given by the
     * <code>schemaBaseUrl</code> passed to the constructor <code>+
     * "wms/1.1.1/WMS_MS_Capabilities.dtd</code>.
     * </p>
     *
     * @return a Transformer propoerly configured to produce DescribeLayer
     *         responses.
     *
     * @throws TransformerException if it is thrown by
     *         <code>super.createTransformer()</code>
     */
    public Transformer createTransformer() throws TransformerException {
        Transformer transformer = super.createTransformer();
        String dtdUrl = this.schemaBaseUrl
            + "wms/1.1.1/WMS_MS_Capabilities.dtd";
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdUrl);

        return transformer;
    }

    /**
     * DOCUMENT ME!
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id
     */
    private static class CapabilitiesTranslator extends TranslatorSupport {
        /** DOCUMENT ME! */
        private static final Logger LOGGER = Logger.getLogger(CapabilitiesTranslator.class.getPackage()
                                                                                          .getName());

        /** DOCUMENT ME! */
        private static final String EPSG = "EPSG:";

        /** DOCUMENT ME! */
        private static AttributesImpl wmsVersion = new AttributesImpl();

        /** DOCUMENT ME! */
        private static final String XLINK_NS = "http://www.w3.org/1999/xlink";

        static {
            wmsVersion.addAttribute("", "version", "version", "", "1.1.1");
        }

        /**
         * The request from wich all the information needed to produce the
         * capabilities document can be obtained
         */
        private CapabilitiesRequest request;

        /**
         * Creates a new CapabilitiesTranslator object.
         *
         * @param handler DOCUMENT ME!
         */
        public CapabilitiesTranslator(ContentHandler handler) {
            super(handler, null, null);
        }

        /**
         * DOCUMENT ME!
         *
         * @param o the <code>CapabilitiesRequest</code>
         *
         * @throws IllegalArgumentException DOCUMENT ME!
         */
        public void encode(Object o) throws IllegalArgumentException {
            if (!(o instanceof CapabilitiesRequest)) {
                throw new IllegalArgumentException();
            }

            this.request = (CapabilitiesRequest) o;
            LOGGER.fine("producing a capabilities document for " + request);
            start("WMT_MS_Capabilities", wmsVersion);
            handleService();
            handleCapability();
            end("WMT_MS_Capabilities");
        }

        /**
         * Encodes the service metadata section of a WMS capabilities document.
         */
        private void handleService() {
            WMS wms = request.getWMS();
            start("Service");

            element("Name", "OGC:WMS");
            element("Title", wms.getTitle());
            element("Abstract", wms.getAbstract());

            handleKeywordList(wms.getKeywords());

            AttributesImpl orAtts = new AttributesImpl();
            orAtts.addAttribute(XLINK_NS, "xlink:type", "xlink:type", "",
                "simple");
            orAtts.addAttribute("", "xlink:href", "xlink:href", "",
                wms.getOnlineResource().toExternalForm());
            element("OnlineResource", null, orAtts);

            element("Fees", wms.getFees());
            element("AccessConstraints", wms.getAccessConstraints());
            end("Service");
        }

        /**
         * DOCUMENT ME!
         *
         * @param keywords DOCUMENT ME!
         */
        private void handleKeywordList(List keywords) {
            start("KeywordList");

            for (Iterator it = keywords.iterator(); it.hasNext();) {
                element("Keyword", String.valueOf(it.next()));
            }

            end("KeywordList");
        }

        /**
         * Encodes the capabilities metadata section of a WMS capabilities
         * document
         */
        private void handleCapability() {
            start("Capability");
            handleRequest();
            handleException();
            handleSLD();
            handleLayers();
            end("Capability");
        }

        /**
         * DOCUMENT ME!
         */
        private void handleRequest() {
            WMS wms = request.getWMS();
            start("Request");

            start("GetCapabilities");
            element("Format", WMS_CAPS_MIME);

            //@HACK: pointer to the WMS dispatcher
            String serviceUrl = request.getBaseUrl() + "wms?";
            handleDcpType(serviceUrl, serviceUrl);
            end("GetCapabilities");

            start("GetMap");

            for (Iterator it = GetMapResponse.getMapFormats().iterator();
                    it.hasNext();) {
                element("Format", String.valueOf(it.next()));
            }

            handleDcpType(serviceUrl, null);
            end("GetMap");

            start("GetFeatureInfo");

            for (Iterator it = GetFeatureInfoResponse.getFormats().iterator();
                    it.hasNext();) {
                element("Format", String.valueOf(it.next()));
            }

            handleDcpType(serviceUrl, serviceUrl);
            end("GetFeatureInfo");

            start("DescribeLayer");
            element("Format", DescribeLayerResponse.DESCLAYER_MIME_TYPE);
            handleDcpType(serviceUrl, null);
            end("DescribeLayer");

            end("Request");
        }

        /**
         * Encodes a <code>DCPType</code> fragment for HTTP GET and POST
         * methods.
         *
         * @param getUrl the URL of the onlineresource for HTTP GET method
         *        requests
         * @param postUrl the URL of the onlineresource for HTTP POST method
         *        requests
         */
        private void handleDcpType(String getUrl, String postUrl) {
            AttributesImpl orAtts = new AttributesImpl();
            orAtts.addAttribute("", "xlink:type", "xlink:type", "", "simple");
            orAtts.addAttribute("", "xlink:href", "xlink:href", "", getUrl);
            start("DCPType");
        	start("HTTP");

        	if(getUrl != null){
            	start("Get");
            	element("OnlineResource", null, orAtts);
            	end("Get");
            }
        	
            if(postUrl != null){
	            orAtts.setAttribute(1, "", "xlink:href", "xlink:href", "", postUrl);
	            start("Post");
	            element("OnlineResource", null, orAtts);
	            end("Post");
            }

            end("HTTP");
            end("DCPType");
        }

        /**
         * DOCUMENT ME!
         */
        private void handleException() {
            start("Exception");

            Iterator it = Arrays.asList(request.getWMS().getExceptionFormats())
                                .iterator();

            while (it.hasNext()) {
                element("Format", String.valueOf(it.next()));
            }

            end("Exception");
        }

        /**
         * DOCUMENT ME!
         */
        private void handleSLD() {
            AttributesImpl sldAtts = new AttributesImpl();
            WMS config = request.getWMS();
            String supportsSLD = config.supportsSLD() ? "1" : "0";
            String supportsUserLayer = config.supportsUserLayer() ? "1" : "0";
            String supportsUserStyle = config.supportsUserStyle() ? "1" : "0";
            String supportsRemoteWFS = config.supportsRemoteWFS() ? "1" : "0";
            sldAtts.addAttribute("", "SupportSLD", "SupportSLD", "", supportsSLD);
            sldAtts.addAttribute("", "UserLayer", "UserLayer", "",
                supportsUserLayer);
            sldAtts.addAttribute("", "UserStyle", "UserStyle", "",
                supportsUserStyle);
            sldAtts.addAttribute("", "RemoteWFS", "RemoteWFS", "",
                supportsRemoteWFS);

            element("UserDefinedSymbolization", null, sldAtts);
        }

        /**
         * Handles the encoding of the layers elements.
         * 
         * <p>
         * This method does a search over the SRS of all the layers to see if
         * there are at least a common one, as needed by the spec:  "<i>The
         * root Layer element shall include a sequence of zero or more
         * &lt;SRS&gt; elements listing all SRSes that are common  to all
         * subsidiary layers. Use a single SRS element with empty content
         * (like so: "&lt;SRS&gt;&lt;/SRS&gt;") if there is no common
         * SRS."</i>
         * </p>
         * 
         * <p>
         * By the other hand, this search is also used to collecto the whole
         * latlon bbox, as stated by the spec: <i>"The bounding box metadata
         * in Capabilities XML specify the minimum enclosing rectangle for the
         * layer as a whole."</i>
         * </p>
         *
         * @task TODO: manage this differently when we have the layer list of
         *       the WMS service decoupled from the feature types configured
         *       for the server instance. (This involves nested layers,
         *       gridcoverages, etc)
         */
        private void handleLayers() {
            WMS wms = request.getWMS();
            start("Layer");

            Data catalog = wms.getData();
            Collection ftypes = catalog.getFeatureTypeInfos().values();
            FeatureTypeInfo layer;

            element("Title", wms.getTitle());
            element("Abstract", wms.getAbstract());

            handleRootSRSAndBbox(ftypes);

            //now encode each layer individually
            for (Iterator it = ftypes.iterator(); it.hasNext();) {
                layer = (FeatureTypeInfo) it.next();

                if (layer.isEnabled()) {
                    handleFeatureType(layer);
                }
            }

            end("Layer");
        }

        /**
         * Called from <code>handleLayers()</code>, does the first iteration
         * over the  available featuretypes to look for common SRS's and
         * summarize their LatLonBBox'es, to state at the root layer.
         * 
         * <p>
         * NOTE: by now we just have "layer.getSRS()", so the search is done
         * against this only SRS.
         * </p>
         *
         * @param ftypes DOCUMENT ME!
         *
         * @throws RuntimeException DOCUMENT ME!
         *
         * @task TODO: figure out how to incorporate multiple SRS using the
         *       reprojection facilities from gt2
         */
        private void handleRootSRSAndBbox(Collection ftypes) {
            FeatureTypeInfo layer;
            String commonSRS = "";
            boolean isCommonSRS = true;
            Envelope latlonBbox = new Envelope();
            Envelope layerBbox = null;
            LOGGER.finer("Collecting summarized latlonbbox and common SRS...");

            for (Iterator it = ftypes.iterator(); it.hasNext();) {
                layer = (FeatureTypeInfo) it.next();

                if (layer.isEnabled()) {
                    try {
                        layerBbox = layer.getLatLongBoundingBox();
                    } catch (IOException e) {
                        throw new RuntimeException(
                            "Can't obtain latLonBBox of " + layer.getName()
                            + ": " + e.getMessage(), e);
                    }

                    latlonBbox.expandToInclude(layerBbox);

                    String layerSRS = layer.getSRS();

                    if ("".equals(commonSRS)) {
                        commonSRS = layerSRS;
                    } else if (!commonSRS.equals(layerSRS)) {
                    	isCommonSRS = false;
                    }
                }
            }

            if (isCommonSRS) {
                commonSRS = EPSG + commonSRS;
                LOGGER.fine("Common SRS is " + commonSRS);
            } else {
            	commonSRS = "";
                LOGGER.fine(
                    "No common SRS, don't forget to incorporate reprojection support...");
            }

            element("SRS", commonSRS);

            LOGGER.fine("Summarized LatLonBBox is " + latlonBbox);
            handleLatLonBBox(latlonBbox);
        }

        /**
         * Calls super.handleFeatureType to add common FeatureType content such
         * as Name, Title and LatLonBoundingBox, and then writes WMS specific
         * layer properties as Styles, Scale Hint, etc.
         *
         * @param ftype The featureType to write out.
         *
         * @throws RuntimeException DOCUMENT ME!
         *
         * @task TODO: write wms specific elements.
         */
        protected void handleFeatureType(FeatureTypeInfo ftype) {
            //HACK: by now all our layers are queryable, since they reference
            //only featuretypes managed by this server
            AttributesImpl qatts = new AttributesImpl();
            qatts.addAttribute("", "queryable", "queryable", "", "1");
            start("Layer", qatts);
            element("Name", ftype.getName());
            element("Title", ftype.getTitle());
            element("Abstract", ftype.getAbstract());

            handleKeywordList(ftype.getKeywords());

            /**
             * @task REVISIT: should getSRS() return the full URL?
             */
            element("SRS", EPSG + ftype.getSRS());

            Envelope bbox = null;

            try {
                bbox = ftype.getLatLongBoundingBox();
            } catch (IOException ex) {
                throw new RuntimeException("Can't obtain latLongBBox of "
                    + ftype.getName() + ": " + ex.getMessage(), ex);
            }

            handleLatLonBBox(bbox);

            //add the layer style
            start("Style");

            Style ftStyle = ftype.getDefaultStyle();
            element("Name", ftStyle.getName());
            element("Title", ftStyle.getTitle());
            element("Abstract", ftStyle.getAbstract());
            handleLegendURL(ftype.getLegendURL());
            end("Style");

            end("Layer");
        }

        /**
         * Writes layer legend icon URL.
         *
         * @param legend The legendURL to write out.
         *
         * @task TODO: write wms specific elements.
         */
        protected void handleLegendURL(LegendURL legend) {
            if (legend != null) {
                AttributesImpl attrs = new AttributesImpl();
                attrs.addAttribute("", "width", "width", "",
                    String.valueOf(legend.getWidth()));
                attrs.addAttribute("", "height", "height", "",
                    String.valueOf(legend.getHeight()));

                start("LegendURL", attrs);

                element("Format", legend.getFormat());
                attrs.clear();
                attrs.addAttribute("", "xmlns:xlink", "xmlns:xlink", "",
                    "http://www.w3.org/1999/xlink");
                attrs.addAttribute("http://www.w3.org/1999/xlink", "type",
                    "xlink:type", "", "simple");
                attrs.addAttribute("http://www.w3.org/1999/xlink", "href",
                    "xlink:href", "", legend.getOnlineResource());
                element("OnlineResource", null, attrs);

                end("LegendURL");
            }
        }

        /**
         * Encodes a LatLonBoundingBox for the given Envelope.
         *
         * @param bbox
         */
        private void handleLatLonBBox(Envelope bbox) {
            String minx = String.valueOf(bbox.getMinX());
            String miny = String.valueOf(bbox.getMinY());
            String maxx = String.valueOf(bbox.getMaxX());
            String maxy = String.valueOf(bbox.getMaxY());

            AttributesImpl bboxAtts = new AttributesImpl();
            bboxAtts.addAttribute("", "minx", "minx", "", minx);
            bboxAtts.addAttribute("", "miny", "miny", "", miny);
            bboxAtts.addAttribute("", "maxx", "maxx", "", maxx);
            bboxAtts.addAttribute("", "maxy", "maxy", "", maxy);

            element("LatLonBoundingBox", null, bboxAtts);
        }
    }
}
