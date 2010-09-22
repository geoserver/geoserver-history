package org.geoserver.wms;

import static org.custommonkey.xmlunit.XMLAssert.assertXpathEvaluatesTo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

public class ProxyBaseURLIntegrationTest extends GeoServerTestSupport {
    
    /** default base url to feed a WMSCapsTransformer with for it to append the DTD location */
    private static final String baseUrl = "http://localhost/geoserver";

    /** test map formats to feed a WMSCapsTransformer with */
    private static final Set<String> mapFormats = Collections.singleton("image/png");

    /** test legend formats to feed a WMSCapsTransformer with */
    private static final Set<String> legendFormats = Collections.singleton("image/png");
    
    @Override
    protected void setUpInternal() throws Exception {
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }
    
    /**
     * Do the links in getcaps respect the proxy base url?
     */
    public void testProxyBaseUrl() throws Exception {
        final String proxyBaseUrl = "http://localhost/proxy";
        GeoServerInfo gs = getGeoServer().getGlobal();
        gs.setProxyBaseUrl(proxyBaseUrl);
        getGeoServer().save(gs);

        Document dom = getAsDOM("wms?request=GetCapabilities");
        print(dom);

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
