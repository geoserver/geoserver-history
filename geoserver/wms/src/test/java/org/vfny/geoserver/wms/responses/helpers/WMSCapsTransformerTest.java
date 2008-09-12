/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.responses.helpers;

import static org.custommonkey.xmlunit.XMLAssert.*;

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
import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.impl.CatalogImpl;
import org.geoserver.catalog.impl.CoverageInfoImpl;
import org.geoserver.catalog.impl.CoverageStoreInfoImpl;
import org.geoserver.catalog.impl.LayerInfoImpl;
import org.geoserver.catalog.impl.NamespaceInfoImpl;
import org.geoserver.catalog.impl.StyleInfoImpl;
import org.geoserver.catalog.impl.WorkspaceInfoImpl;
import org.geoserver.config.ContactInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.impl.ContactInfoImpl;
import org.geoserver.config.impl.GeoServerImpl;
import org.geoserver.config.impl.GeoServerInfoImpl;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.WMSInfoImpl;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.wms.requests.WMSCapabilitiesRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * 
 * @author Gabriel Roldan
 * @version $Id$
 */
public class WMSCapsTransformerTest extends TestCase {

    /**
     * Namespace used for the resources in this test suite
     */
    private static final String TEST_NAMESPACE = "http://geoserver.org";

    private XpathEngine XPATH;

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

    private NamespaceInfoImpl namespaceInfo;

    private WorkspaceInfoImpl workspaceInfo;

    private StyleInfoImpl defaultStyle;

    /**
     * An old style {@link WMS} to
     */
    private WMS wms;

    private WMSCapabilitiesRequest req;

    /**
     * the default base url for
     * {@link WMSCapabilitiesRequest#getBaseUrl()                           
     */
    private static final String baseUrl = "http://localhost:8080/geoserver";

    /**
     * Sets up the configuration objects with default values. Since they're live, specific tests can
     * modify their state before running the assertions
     */
    protected void setUp() throws Exception {
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        namespaces.put("geos", TEST_NAMESPACE);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
        XPATH = XMLUnit.newXpathEngine();

        geosConfig = new GeoServerImpl();

        geosInfo = new GeoServerInfoImpl();
        geosConfig.setGlobal(geosInfo);
        geosInfo.setContactInfo(new ContactInfoImpl());

        wmsInfo = new WMSInfoImpl();
        geosConfig.add(wmsInfo);
        wmsInfo.setTitle("My GeoServer WMS");

        catalog = new CatalogImpl();
        geosConfig.setCatalog(catalog);

        wms = new WMS(geosConfig);

        req = new WMSCapabilitiesRequest(wms);
        req.setBaseUrl(baseUrl);

        namespaceInfo = new NamespaceInfoImpl(catalog);
        namespaceInfo.setId("testNs");
        namespaceInfo.setPrefix("geos");
        namespaceInfo.setURI(TEST_NAMESPACE);
        catalog.add(namespaceInfo);

        workspaceInfo = new WorkspaceInfoImpl();
        catalog.setDefaultWorkspace(workspaceInfo);

        defaultStyle = new StyleInfoImpl(catalog) {
            /**
             * Override so it does not try to load a file from disk
             */
            @Override
            public Style getStyle() throws IOException {
                StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
                Style style = styleFactory.createStyle();
                style.setName("Default Style");
                return style;
            }
        };
        defaultStyle.setFilename("defaultStyleFileName");
        defaultStyle.setId("defaultStyleId");
        defaultStyle.setName("defaultStyleName");
        catalog.add(defaultStyle);

    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Runs the transformation on tr with the provided request and returns the result as a DOM
     */
    private Document transform(WMSCapabilitiesRequest req, WMSCapsTransformer tr) throws Exception {
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

        //System.out.println(out.toString());

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

        Document dom = transform(req, tr);
        Element root = dom.getDocumentElement();
        assertEquals("WMT_MS_Capabilities", root.getNodeName());
        assertEquals("1.1.1", root.getAttribute("version"));
        assertEquals("0", root.getAttribute("updateSequence"));

        geosInfo.setUpdateSequence(10);
        tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);
        dom = transform(req, tr);
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
        geosInfo.setContactInfo(contactInfo);
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
        Document dom = transform(req, tr);

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
        Document dom = transform(req, tr);

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

    public void testCRSList() throws Exception {
        WMSCapsTransformer tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);
        tr.setIndentation(2);
        Document dom = transform(req, tr);
        final Set<String> supportedCodes = CRS.getSupportedCodes("EPSG");
        NodeList allCrsCodes = XPATH.getMatchingNodes("/WMT_MS_Capabilities/Capability/Layer/SRS",
                dom);
        assertEquals(supportedCodes.size(), allCrsCodes.getLength());
    }

    public void testLimitedCRSList() throws Exception {
        wmsInfo.getSRS().add("EPSG:3246");
        wmsInfo.getSRS().add("EPSG:23030");

        WMSCapsTransformer tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);
        tr.setIndentation(2);
        Document dom = transform(req, tr);
        NodeList limitedCrsCodes = XPATH.getMatchingNodes(
                "/WMT_MS_Capabilities/Capability/Layer/SRS", dom);
        assertEquals(2, limitedCrsCodes.getLength());
    }

    /**
     * Tests that the basic coverage properties are correctly encoded
     * 
     * @throws Exception
     */
    public void testEncodeCoverage() throws Exception {
        wmsInfo.getSRS().add("EPSG:4326");

        CoverageStoreInfoImpl coverageStoreInfo = new CoverageStoreInfoImpl(catalog);
        CoverageInfoImpl coverageInfo = new CoverageInfoImpl(catalog);
        LayerInfoImpl coverageLayerInfo = new LayerInfoImpl();
        setUpBasicTestCoverage(coverageStoreInfo, coverageInfo, coverageLayerInfo);

        // make latLon and native bbox differ
        ReferencedEnvelope nativeBbox = new ReferencedEnvelope(-170, 170, -80, 80,
                DefaultGeographicCRS.WGS84);
        coverageInfo.setNativeBoundingBox(nativeBbox);

        WMSCapsTransformer tr = new WMSCapsTransformer(schemaBaseUrl, mapFormats, legendFormats);
        tr.setIndentation(2);
        Document dom = transform(req, tr);

        final String pathToLayer = "/WMT_MS_Capabilities/Capability/Layer/Layer";
        assertXpathExists(pathToLayer, dom);
        assertXpathEvaluatesTo("1", pathToLayer + "/@queryable", dom);
        assertXpathEvaluatesTo("geos:testCoverageName", pathToLayer + "/Name", dom);
        assertXpathEvaluatesTo("testCoverageTitle", pathToLayer + "/Title", dom);
        assertXpathEvaluatesTo("testCoverageDescription", pathToLayer + "/Abstract", dom);
        assertXpathEvaluatesTo("EPSG:4326", pathToLayer + "/SRS", dom);

        assertXpathEvaluatesTo("-180.0", pathToLayer + "/LatLonBoundingBox/@minx", dom);
        assertXpathEvaluatesTo("-90.0", pathToLayer + "/LatLonBoundingBox/@miny", dom);
        assertXpathEvaluatesTo("180.0", pathToLayer + "/LatLonBoundingBox/@maxx", dom);
        assertXpathEvaluatesTo("90.0", pathToLayer + "/LatLonBoundingBox/@maxy", dom);

        assertXpathEvaluatesTo("EPSG:4326", pathToLayer + "/BoundingBox/@SRS", dom);
        assertXpathEvaluatesTo("-170.0", pathToLayer + "/BoundingBox/@minx", dom);
        assertXpathEvaluatesTo("-80.0", pathToLayer + "/BoundingBox/@miny", dom);
        assertXpathEvaluatesTo("170.0", pathToLayer + "/BoundingBox/@maxx", dom);
        assertXpathEvaluatesTo("80.0", pathToLayer + "/BoundingBox/@maxy", dom);

        // just check the correct style was added, its not this test business to fully check how
        // styles are encoded
        assertXpathExists(pathToLayer + "/Style", dom);
        assertXpathEvaluatesTo("Default Style", pathToLayer + "/Style/Name", dom);
    }

    /**
     * Ties up the provided coverage store, coverage and layer together and adds them to the catalog
     * in order to assert the encoding of a coverage in the GetCapabilities response.
     * <p>
     * This method only sets up the simplest and minimal properties needed for each of the
     * arguments. Callers could enhance them after this method returns in order to set up the
     * objects properties of interest for the specific test.
     * </p>
     * 
     * @param coverageStoreInfo
     *                the coverage store info to add to the catalog and where to configure the
     *                coverage
     * @param coverageInfo
     *                the coverage to configure with basic resource properties, add to the catalog
     *                as a component of the {@code coverageStoreInfo}
     * @param coverageLayerInfo
     *                the layer to configure for {@code coverageInfo} and add to the catalog with
     *                {@link #defaultStyle} and some default simple properties
     */
    private void setUpBasicTestCoverage(CoverageStoreInfoImpl coverageStoreInfo,
            CoverageInfoImpl coverageInfo, LayerInfoImpl coverageLayerInfo) {

        coverageInfo.setStore(coverageStoreInfo);

        coverageInfo.setId("testCoverageId");
        coverageInfo.setEnabled(true);
        coverageInfo.setName("testCoverageName");
        coverageInfo.setNamespace(namespaceInfo);
        coverageInfo.setId("testCoverage");
        coverageInfo.setTitle("testCoverageTitle");
        coverageInfo.setAbstract("testCoverageAbstract");
        coverageInfo.setDescription("testCoverageDescription");
        coverageInfo.setDefaultInterpolationMethod("defaultInterpolationMethod");
        ReferencedEnvelope latLonBbox = new ReferencedEnvelope(-180, 180, -90, 90,
                DefaultGeographicCRS.WGS84);
        coverageInfo.setLatLonBoundingBox(latLonBbox);
        coverageInfo.setNativeCRS(DefaultGeographicCRS.WGS84);
        coverageInfo.setNativeBoundingBox(latLonBbox);
        coverageInfo.setSRS("EPSG:4326");

        coverageLayerInfo.setResource(coverageInfo);
        coverageLayerInfo.setEnabled(true);
        coverageLayerInfo.setId("testLayerId");
        coverageLayerInfo.setName("testLayerName");

        // no path, top level layer
        // coverageLayerInfo.setPath("testLayerPath");
        coverageLayerInfo.setType(LayerInfo.Type.RASTER);
        coverageLayerInfo.setDefaultStyle(defaultStyle);

        catalog.add(coverageLayerInfo);
        catalog.add(coverageStoreInfo);
        catalog.add(coverageInfo);
    }
}
