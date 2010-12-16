package org.geoserver.inspire.wms;

import org.geoserver.test.GeoServerTestSupport;
import org.geoserver.wms.WMSInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.geoserver.inspire.wms.WMSExtendedCapabilitiesProvider.NAMESPACE;

public class WMSExtendedCapabilitiesTest extends GeoServerTestSupport {

    public void testExtendedCaps() throws Exception {
        WMSInfo wms = getGeoServer().getService(WMSInfo.class);
        wms.getSRS().add("EPSG:4326");
        wms.getMetadata().put(InspireMetadata.LANGUAGE.key, "fre");
        wms.getMetadata().put(InspireMetadata.METADATA_URL.key, "http://foo.com?bar=baz");
        getGeoServer().save(wms);
        
        Document dom = getAsDOM("wms?request=getcapabilities");
        assertEquals(NAMESPACE, dom.getDocumentElement().getAttribute("xmlns:inspire_vs"));
        
        Element e = getFirstElementByTagName(dom, "inspire_vs:ExtendedCapabilities");
        assertNotNull(e);
        
        Element f = getFirstElementByTagName(e, "inspire_vs:MetadataUrl");
        assertNotNull(f);
        
        f = getFirstElementByTagName(f, "gmd:URL");
        assertNotNull(f);
        assertEquals("http://foo.com?bar=baz", f.getFirstChild().getNodeValue());
        
        f = getFirstElementByTagName(e, "inspire_vs:Language");
        assertNotNull(f);
        assertEquals("fre", f.getFirstChild().getNodeValue());
    }
}
