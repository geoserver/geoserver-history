/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.helpers;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.TestCase;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.config.impl.GeoServerImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfoImpl;
import org.geotools.data.ows.WMSRequest;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.requests.WMSCapabilitiesRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

/**
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class WMSCapsTransformerTest extends TestCase {

    /** default base url to feed a WMSCapsTransformer with for it to append the DTD location */
    private static final String schemaBaseUrl = "http://localhost/geoserver";

    /** test map formats to feed a WMSCapsTransformer with */
    private static final Set<String> mapFormats = Collections.singleton("image/png");

    /** test legend formats to feed a WMSCapsTransformer with */
    private static final Set<String> legendFormats = Collections.singleton("image/png");

    /**
     * a mocked up {@link GeoServer} config, almost empty after setUp(), except for the
     * {@link WMSInfo}, {@link GeoServerInfo} and empty {@link Catalog}, Specific tests should add
     * content as needed
     */
    private GeoServerImpl geosConfig;

    /**
     * a mocked up {@link GeoServerInfo} for {@link #geosConfig}. Specific tests should set its
     * properties as needed
     */
    private GeoServerInfoImpl geosInfo;

    /**
     * a mocked up {@link WMSInfo} for {@link #geosConfig}, empty except for the WMSInfo after
     * setUp(), Specific tests should set its properties as needed
     */
    private WMSInfoImpl wmsInfo;

    /**
     * a mocked up {@link Catalog} for {@link #geosConfig}, empty after setUp(), Specific tests
     * should add content as needed
     */
    private CatalogImpl catalog;

    /**
     * An old style {@link WMS} to
     */
    private WMS wms;

    private WMSCapabilitiesRequest req;

    /** the default base url for {@link WMSCapabilitiesRequest#getBaseUrl()                */
    private static final String baseUrl = "http://localhost:8080/geoserver";

    /**
     * Sets up the configuration objects with default values. Since they're live, specific tests can
     * modify their state before running the assertions
     */
    protected void setUp() throws Exception {
        geosConfig = new GeoServerImpl();

        geosInfo = new GeoServerInfoImpl();
        geosInfo.setContact(new ContactInfoImpl());
        geosConfig.setGlobal(geosInfo);

        wmsInfo = new WMSInfoImpl();
        geosConfig.add(wmsInfo);

        catalog = new CatalogImpl();
        geosConfig.setCatalog(catalog);

        wms = new WMS(geosConfig);

        req = new WMSCapabilitiesRequest(wms);
        req.setBaseUrl(baseUrl);

        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Runs the transformation on tr and returns the result as a DOM
     */
    private Document transform(WMSCapsTransformer tr) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        tr.transform(req, out);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);

        DocumentBuilder db = dbf.newDocumentBuilder();

        /**
         * Resolves everything to an empty xml document, useful for skipping errors due to missing
         * dtds and the like
         * 
         * @author Andrea Aime - TOPP
         */
        class EmptyResolver implements org.xml.sax.EntityResolver {
            public InputSource resolveEntity(String publicId, String systemId)
                    throws org.xml.sax.SAXException, IOException {
                StringReader reader = new StringReader("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                InputSource source = new InputSource(reader);
                source.setPublicId(publicId);
                source.setSystemId(systemId);

                return source;
            }
        }
        db.setEntityResolver(new EmptyResolver());

        System.out.println(out.toString().substring(0, 2000));

        Document doc = db.parse(new ByteArrayInputStream(out.toByteArray()));
        return doc;
    }

    public void testHeader() throws Exception {
        WMSCapsTransformer tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);
        StringWriter writer = new StringWriter();
        tr.transform(req, writer);
        String content = writer.getBuffer().toString();

        assertTrue(content.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        String dtdDef = "<!DOCTYPE WMT_MS_Capabilities SYSTEM \"" + schemaBaseUrl
                + "/schemas/wms/1.1.1/WMS_MS_Capabilities.dtd\">";
        assertTrue(content.contains(dtdDef));
    }

    public void testRootElement() throws Exception {
        WMSCapsTransformer tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);

        Document dom = transform(tr);
        Element root = dom.getDocumentElement();
        assertEquals("WMT_MS_Capabilities", root.getNodeName());
        assertEquals("1.1.1", root.getAttribute("version"));
        assertEquals("0", root.getAttribute("updateSequence"));

        geosInfo.setUpdateSequence(10);
        tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);
        dom = transform(tr);
        root = dom.getDocumentElement();
        assertEquals("10", root.getAttribute("updateSequence"));
    }

    public void testServiceSection() throws Exception {
        wmsInfo.setTitle("title");
        wmsInfo.setAbstract("abstract");
        wmsInfo.getKeywords().add("k1");
        wmsInfo.getKeywords().add("k2");
        // @REVISIT: this is not being respected, but the onlineresource is being set based on the
        // proxyBaseUrl... not sure if that's correct
        wmsInfo.setOnlineResource("http://onlineresource/fake");

        ContactInfo contactInfo = new ContactInfoImpl();
        geosInfo.setContact(contactInfo);
        contactInfo.setContactPerson("contactPerson");
        contactInfo.setContactOrganization("contactOrganization");
        contactInfo.setContactPosition("contactPosition");
        contactInfo.setAddress("address");
        contactInfo.setAddressType("addressType");
        contactInfo.setAddressCity("city");
        contactInfo.setAddressState("state");
        contactInfo.setAddressPostalCode("postCode");
        contactInfo.setAddressCountry("country");
        contactInfo.setContactVoice("voice");
        contactInfo.setContactEmail("email");
        contactInfo.setContactFacsimile("fax");

        wmsInfo.setFees("fees");
        wmsInfo.setAccessConstraints("accessConstraints");

        WMSCapsTransformer tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);
        tr.setIndentation(2);
        Document dom = transform(tr);

        String service = "/WMT_MS_Capabilities/Service";
        assertXpathEvaluatesTo("OGC:WMS", service + "/Name", dom);

        assertXpathEvaluatesTo("title", service + "/Title", dom);
        assertXpathEvaluatesTo("abstract", service + "/Abstract", dom);
        assertXpathEvaluatesTo("k1", service + "/KeywordList/Keyword[1]", dom);
        assertXpathEvaluatesTo("k2", service + "/KeywordList/Keyword[2]", dom);
        // @REVISIT: shouldn't it be WmsInfo.getOnlineResource?
        assertXpathEvaluatesTo(baseUrl + "/wms", service + "/OnlineResource/@xlink:href", dom);

        assertXpathEvaluatesTo("contactPerson", service
                + "/ContactInformation/ContactPersonPrimary/ContactPerson", dom);
        assertXpathEvaluatesTo("contactOrganization", service
                + "/ContactInformation/ContactPersonPrimary/ContactOrganization", dom);
        assertXpathEvaluatesTo("contactPosition", service + "/ContactInformation/ContactPosition",
                dom);
        assertXpathEvaluatesTo("address", service + "/ContactInformation/ContactAddress/Address",
                dom);
        assertXpathEvaluatesTo("addressType", service
                + "/ContactInformation/ContactAddress/AddressType", dom);
        assertXpathEvaluatesTo("city", service + "/ContactInformation/ContactAddress/City", dom);
        assertXpathEvaluatesTo("state", service
                + "/ContactInformation/ContactAddress/StateOrProvince", dom);
        assertXpathEvaluatesTo("postCode", service + "/ContactInformation/ContactAddress/PostCode",
                dom);
        assertXpathEvaluatesTo("country", service + "/ContactInformation/ContactAddress/Country",
                dom);
        assertXpathEvaluatesTo("voice", service + "/ContactInformation/ContactVoiceTelephone", dom);
        assertXpathEvaluatesTo("fax", service + "/ContactInformation/ContactFacsimileTelephone",
                dom);
        assertXpathEvaluatesTo("email", service
                + "/ContactInformation/ContactElectronicMailAddress", dom);

        assertXpathEvaluatesTo("fees", service + "/Fees", dom);
        assertXpathEvaluatesTo("accessConstraints", service + "/AccessConstraints", dom);
    }

    /**
     * Do the links in getcaps respect the proxy base url?
     */
    public void testProxyBaseUrl() throws Exception {
        final String proxyBaseUrl = "http://localhost/proxy";
        geosInfo.setProxyBaseUrl(proxyBaseUrl);

        WMSCapsTransformer tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);
        tr.setIndentation(2);
        Document dom = transform(tr);

        String serviceOnlineRes = "/WMT_MS_Capabilities/Service/OnlineResource/@xlink:href";
        // @REVISIT: shouldn't it be WmsInfo.getOnlineResource?
        assertXpathEvaluatesTo(proxyBaseUrl + "/wms", serviceOnlineRes, dom);

        String getCapsGet = "/WMT_MS_Capabilities/Capability/Request/GetCapabilities/DCPType/HTTP/Get/OnlineResource/@xlink:href";
        assertXpathEvaluatesTo(proxyBaseUrl + "/wms?SERVICE=WMS&", getCapsGet, dom);

        String getCapsPost = "/WMT_MS_Capabilities/Capability/Request/GetCapabilities/DCPType/HTTP/Post/OnlineResource/@xlink:href";
        assertXpathEvaluatesTo(proxyBaseUrl + "/wms?SERVICE=WMS&", getCapsPost, dom);

        String getMapGet = "/WMT_MS_Capabilities/Capability/Request/GetMap/DCPType/HTTP/Get/OnlineResource/@xlink:href";
        assertXpathEvaluatesTo(proxyBaseUrl + "/wms?SERVICE=WMS&", getMapGet, dom);

        String getFeatureInfoGet = "/WMT_MS_Capabilities/Capability/Request/GetFeatureInfo/DCPType/HTTP/Get/OnlineResource/@xlink:href";
        assertXpathEvaluatesTo(proxyBaseUrl + "/wms?SERVICE=WMS&", getFeatureInfoGet, dom);

        String getFeatureInfoPost = "/WMT_MS_Capabilities/Capability/Request/GetFeatureInfo/DCPType/HTTP/Post/OnlineResource/@xlink:href";
        assertXpathEvaluatesTo(proxyBaseUrl + "/wms?SERVICE=WMS&", getFeatureInfoPost, dom);

        String describeLayerGet = "/WMT_MS_Capabilities/Capability/Request/DescribeLayer/DCPType/HTTP/Get/OnlineResource/@xlink:href";
        assertXpathEvaluatesTo(proxyBaseUrl + "/wms?SERVICE=WMS&", describeLayerGet, dom);

        String getLegentGet = "/WMT_MS_Capabilities/Capability/Request/GetLegendGraphic/DCPType/HTTP/Get/OnlineResource/@xlink:href";
        assertXpathEvaluatesTo(proxyBaseUrl + "/wms?SERVICE=WMS&", getLegentGet, dom);
    }
}
