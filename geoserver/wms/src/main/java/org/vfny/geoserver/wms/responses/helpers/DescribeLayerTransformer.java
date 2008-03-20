/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.helpers;

import org.geoserver.ows.util.RequestUtils;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.MapLayerInfo;
import org.vfny.geoserver.wms.requests.DescribeLayerRequest;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import java.util.Iterator;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;


/**
 * <code>org.geotools.xml.transform.TransformerBase</code> specialized in
 * producing a WMS DescribeLayer response.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class DescribeLayerTransformer extends TransformerBase {
    /** The base url upon URLs which point to 'me' should be based. */
    private String baseUrl;
    
    private GeoServer geoserver;

    private String version;

    /**
     * Creates a new DescribeLayerTransformer object.
     *
     * @param baseUrl the url string wich holds the validation
     * schemas and DTD's on this server instance.
     */
    public DescribeLayerTransformer(String baseUrl, GeoServer gs, String version) {
        super();

        if (baseUrl == null) {
            throw new NullPointerException();
        }

        this.baseUrl = baseUrl;
        this.geoserver = gs;
        this.version = version;
    }

    /**
     * Creates and returns a Translator specialized in producing
     * a DescribeLayer response document.
     *
     * @param handler the content handler to send sax events to.
     *
     * @return a new <code>DescribeLayerTranslator</code>
     */
    public Translator createTranslator(ContentHandler handler) {
        return new DescribeLayerTranslator(handler);
    }

    /**
     * Gets the <code>Transformer</code> created by the overriden method in
     * the superclass and adds it the DOCTYPE token pointing to the
     * DescribeLayer DTD on this server instance.
     *
     * <p>
     * The DTD is set at the fixed location given by the <code>schemaBaseUrl</code>
     * passed to the constructor <code>+ "wms/1.1.1/WMS_DescribeLayerResponse.dtd</code>.
     * </p>
     *
     * @return a Transformer propoerly configured to produce DescribeLayer responses.
     *
     * @throws TransformerException if it is thrown by <code>super.createTransformer()</code>
     */
    public Transformer createTransformer() throws TransformerException {
        Transformer transformer = super.createTransformer();
        String dtdUrl = RequestUtils.proxifiedBaseURL(baseUrl,geoserver.getProxyBaseUrl()) + "schemas/wms/1.1.1/WMS_DescribeLayerResponse.dtd";
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdUrl);

        return transformer;
    }

    /**
     * Sends SAX events to produce a DescribeLayer response document.
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private class DescribeLayerTranslator extends TranslatorSupport {
        /**
         * Creates a new DescribeLayerTranslator object.
         *
         * @param handler DOCUMENT ME!
         */
        public DescribeLayerTranslator(ContentHandler handler) {
            super(handler, null, null);
        }

        /**
         * Encode the object.
         *
         * @param o The Object to encode.
         *
         * @throws IllegalArgumentException if the Object is not encodeable.
         */
        public void encode(Object o) throws IllegalArgumentException {
            if (!(o instanceof DescribeLayerRequest)) {
                throw new IllegalArgumentException();
            }

            DescribeLayerRequest req = (DescribeLayerRequest) o;

            AttributesImpl versionAtt = new AttributesImpl();
            // TODO: grab the version from the request, should be equal to
            // the WMS one
            versionAtt.addAttribute("", "version", "version", "", version);

            start("WMS_DescribeLayerResponse", versionAtt);

            handleLayers(req);

            end("WMS_DescribeLayerResponse");
        }

        /**
         * As currently GeoServer does not have support for nested layers, this
         * method declares a <code>LayerDescription</code> element for each
         * featuretype requested.
         *
         * @param req
         */
        private void handleLayers(DescribeLayerRequest req) {
            MapLayerInfo layer;

            String url = RequestUtils.proxifiedBaseURL(req.getBaseUrl(),req.getServiceRef().getGeoServer().getProxyBaseUrl()) + "wfs/WfsDispatcher?";

            AttributesImpl layerAtts = new AttributesImpl();
            layerAtts.addAttribute("", "name", "name", "", "");
            layerAtts.addAttribute("", "wfs", "wfs", "", url);

            AttributesImpl queryAtts = new AttributesImpl();
            queryAtts.addAttribute("", "typeName", "typeName", "", "");

            for (Iterator it = req.getLayers().iterator(); it.hasNext();) {
                layer = (MapLayerInfo) it.next();
                layerAtts.setAttribute(0, "", "name", "name", "", layer.getName());
                start("LayerDescription", layerAtts);

                queryAtts.setAttribute(0, "", "typeName", "typeName", "", layer.getName());
                element("Query", null, queryAtts);

                end("LayerDescription");
            }
        }
    }
}
