/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wms.helpers;

import java.util.Iterator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;

import org.geotools.xml.transform.TransformerBase;
import org.geotools.xml.transform.Translator;
import org.vfny.geoserver.global.FeatureTypeInfo;
import org.vfny.geoserver.requests.wms.DescribeLayerRequest;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;


/**
 * <code>org.geotools.xml.transform.TransformerBase</code> specialized in
 * producing a WMS DescribeLayer response.
 *
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 */
public class DescribeLayerTransformer extends TransformerBase {
    /** The base url upon wich the DesribeLayer DTD should be referenced. */
    private String schemaBaseUrl;

    /**
     * Creates a new DescribeLayerTransformer object.
     *
     * @param schemaBaseUrl the url string wich holds the validation
     * schemas and DTD's on this server instance.
     */
    public DescribeLayerTransformer(String schemaBaseUrl) {
        super();

        if (schemaBaseUrl == null) {
            throw new NullPointerException();
        }

        this.schemaBaseUrl = schemaBaseUrl;
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
        String dtdUrl = this.schemaBaseUrl
            + "wms/1.1.1/WMS_DescribeLayerResponse.dtd";
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, dtdUrl);

        return transformer;
    }

    /**
     * Sends SAX events to produce a DescribeLayer response document.
     *
     * @author Gabriel Roldan, Axios Engineering
     * @version $Id$
     */
    private static class DescribeLayerTranslator extends TranslatorSupport {
    	
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

            AttributesImpl version = new AttributesImpl();
            version.addAttribute("", "version", "version", "", "1.0.0");

            start("WMS_DescribeLayerResponse", version);

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
            FeatureTypeInfo ft;

            String url = req.getBaseUrl() + "wfs/WfsDispatcher?";

            AttributesImpl layerAtts = new AttributesImpl();
            layerAtts.addAttribute("", "name", "name", "", "");
            layerAtts.addAttribute("", "wfs", "wfs", "", url);

            AttributesImpl queryAtts = new AttributesImpl();
            queryAtts.addAttribute("", "typeName", "typeName", "", "");

            for (Iterator it = req.getLayers().iterator(); it.hasNext();) {
                ft = (FeatureTypeInfo) it.next();
                layerAtts.setAttribute(0, "", "name", "name", "", ft.getName());
                start("LayerDescription", layerAtts);

                queryAtts.setAttribute(0, "", "typeName", "typeName", "",
                    ft.getName());
                element("Query", null, queryAtts);

                end("LayerDescription");
            }
        }
    }
}
