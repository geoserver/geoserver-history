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
 * @version $Id: WfsCapabilitiesResponseHandler.java,v 1.1.2.6 2003/11/22 02:01:57 cholmesny Exp $
 */
public class WfsCapabilitiesResponseHandler extends CapabilitiesResponseHandler {
    protected static final String WFS_URI = "http://www.opengis.net/wfs";
    protected static final String CUR_VERSION = "1.0.0";
    protected static final String XSI_PREFIX = "xsi";
    protected static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

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
        AttributesImpl attributes = new AttributesImpl();
        attributes.addAttribute("", "version", "version", "", CUR_VERSION);
        attributes.addAttribute("", "xmlns", "xmlns", "", WFS_URI);

        NameSpace[] namespaces = catalog.getNameSpaces();

        for (int i = 0; i < namespaces.length; i++) {
            String prefixDef = "xmlns:" + namespaces[i].getPrefix();
            String uri = namespaces[i].getUri();
            attributes.addAttribute("", prefixDef, prefixDef, "", uri);
        }

        attributes.addAttribute("", "xmlns:ogc", "xmlns:ogc", "",
            "http://www.opengis.net/ogc");

        String prefixDef = "xmlns:" + XSI_PREFIX;
        attributes.addAttribute("", prefixDef, prefixDef, "", XSI_URI);

        String locationAtt = XSI_PREFIX + ":schemaLocation";
        String locationDef = WFS_URI + " "
            + ServerConfig.getInstance().getWFSConfig().getWfsCapLocation();
        attributes.addAttribute("", locationAtt, locationAtt, "", locationDef);
        startElement("WFS_Capabilities", attributes);
    }

    /**
     * DOCUMENT ME!
     *
     * @param config DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    public void endDocument(ServiceConfig config) throws SAXException {
        handleFilters();
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

        if (capabilityName.equals("DescribeFeatureType")) {
            String schemaLanguage = "SchemaDescriptionLanguage";
            startElement(schemaLanguage);
            handleSingleElem("XMLSCHEMA", "");
            endElement(schemaLanguage);
            cReturn();
        }

        if (capabilityName.startsWith("GetFeature")) {
            String resultFormat = "ResultFormat";
            startElement(resultFormat);
            handleSingleElem("GML2", "");
            endElement(resultFormat);
            cReturn();
        }

        startElement("DCPType");
        startElement("HTTP");

        String baseUrl = config.getURL();
        String url = baseUrl + capabilityName + "?";
        attributes.addAttribute("", "onlineResource", "onlineResource", "", url);

        startElement("Get", attributes);
        endElement("Get");
        endElement("HTTP");
        endElement("DCPType");

        cReturn();

        attributes = new AttributesImpl();
        url = baseUrl + capabilityName;
        attributes.addAttribute("", "onlineResource", "onlineResource", "", url);
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
	startElement("Insert");
        endElement("Insert");
        startElement("Update");
        endElement("Update");
        startElement("Delete");
        endElement("Delete");
        startElement("Lock");
        endElement("Lock");
        unIndent();
        endElement("Operations");

        Collection featureTypes = server.getCatalog().getFeatureTypes().values();
        FeatureTypeConfig ftype;

        for (Iterator it = featureTypes.iterator(); it.hasNext();) {
            ftype = (FeatureTypeConfig) it.next();

            //can't handle ones that aren't enabled.
            //and they shouldn't be handled, as they won't function.
            if (ftype.isEnabled()) {
                startElement("FeatureType");
                handleFeatureType(ftype);
                unIndent();
                endElement("FeatureType");
                cReturn();
            }
        }

        endElement("FeatureTypeList");
    }

    /**
     * DOCUMENT ME!
     *
     * @param kwords DOCUMENT ME!
     *
     * @throws SAXException DOCUMENT ME!
     */
    protected void handleKeywords(List kwords) throws SAXException {
        startElement("Keywords");

        if (kwords != null) {
            for (Iterator it = kwords.iterator(); it.hasNext();) {
                characters(it.next().toString());

                if (it.hasNext()) {
                    characters(", ");
                }
            }
        }

        endElement("Keywords");
    }

    protected void handleFilters() throws SAXException {
        String ogc = "ogc:";

        //REVISIT: for now I"m just prepending ogc onto the name element.
        //Is the proper way to only do that for the qname?  I guess it
        //would only really matter if we're going to be producing capabilities
        //documents that aren't qualified, and I don't see any reason to
        //do that.
        indent();
        startElement(ogc + "Filter_Capabilities");
        indent();
        startElement(ogc + "Spatial_Capabilities");
        indent();
        startElement(ogc + "Spatial_Operators");
        cReturn();
        handleSingleElem(ogc + "Disjoint");
        cReturn();
        handleSingleElem(ogc + "Equals");
        cReturn();
        handleSingleElem(ogc + "DWithin");
        cReturn();
        handleSingleElem(ogc + "Beyond");
        cReturn();
        handleSingleElem(ogc + "Intersect");
        cReturn();
        handleSingleElem(ogc + "Touches");
        cReturn();
        handleSingleElem(ogc + "Crosses");
        cReturn();
        handleSingleElem(ogc + "Within");
        cReturn();
        handleSingleElem(ogc + "Contains");
        cReturn();
        handleSingleElem(ogc + "Overlaps");
        cReturn();
        handleSingleElem(ogc + "BBOX");
        unIndent();
        endElement(ogc + "Spatial_Operators");
        unIndent();
        endElement(ogc + "Spatial_Capabilities");
        cReturn();
        startElement(ogc + "Scalar_Capabilities");
        indent();
        handleSingleElem(ogc + "Logical_Operators");
        indent();
        startElement(ogc + "Comparison_Operators");
        indent();
        handleSingleElem(ogc + "Simple_Comparisons");
        cReturn();
        handleSingleElem(ogc + "Between");
        cReturn();
        handleSingleElem(ogc + "Like");
        cReturn();
        handleSingleElem(ogc + "NullCheck");
        unIndent();
        endElement(ogc + "Comparison_Operators");
        cReturn();
        startElement(ogc + "Arithmetic_Operators");
        indent();
        handleSingleElem(ogc + "Simple_Arithmetic");
        unIndent();
        endElement(ogc + "Arithmetic_Operators");
        unIndent();
        endElement(ogc + "Scalar_Capabilities");
        unIndent();
        endElement(ogc + "Filter_Capabilities");
        unIndent();
    }
}
