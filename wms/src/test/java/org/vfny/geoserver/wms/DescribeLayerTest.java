package org.vfny.geoserver.wms;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.w3c.dom.Document;

public class DescribeLayerTest extends WMSTestSupport {
    
    protected void setUp() throws Exception {
        super.setUp();
        GeoServerDTO dto =  (GeoServerDTO) getGeoServer().toDTO();
        dto.setProxyBaseUrl("src/test/resources");
        getGeoServer().load(dto);
    }

    public void testDescribeLayerVersion111() throws Exception {
        String layer = MockData.FORESTS.getPrefix() + ":" + MockData.FORESTS.getLocalPart();
        String request = "wms?service=wms&version=1.1.1&request=DescribeLayer&layers=" + layer;
        Document dom = getAsDOM(request);
        
        assertEquals("1.1.1", dom.getDocumentElement().getAttributes().getNamedItem("version").getNodeValue());
    }
    
    public void testDescribeLayerVersion110() throws Exception {
        String layer = MockData.FORESTS.getPrefix() + ":" + MockData.FORESTS.getLocalPart();
        String request = "wms?service=wms&version=1.1.0&request=DescribeLayer&layers=" + layer;
        Document dom = getAsDOM(request);
        assertEquals("1.1.0", dom.getDocumentElement().getAttributes().getNamedItem("version").getNodeValue());
    }
}
