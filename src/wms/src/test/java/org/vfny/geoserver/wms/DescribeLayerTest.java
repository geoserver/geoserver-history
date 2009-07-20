package org.vfny.geoserver.wms;

import junit.framework.Test;

import org.geoserver.config.GeoServerInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.w3c.dom.Document;

public class DescribeLayerTest extends WMSTestSupport {
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new DescribeLayerTest());
    }
    
    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();

        // Uncomment the following lines if you want to enable DTD validation for this test 
        // (by passing false as the second param to getAsDOM(). 
        // BUG: Currently, this doesn't seem to actually validate the document, although 
        // 'validation' fails if the DTD is missing.
        
        // GeoServerInfo global = getGeoServer().getGlobal();
        // global.setProxyBaseUrl("src/test/resources/geoserver");
        // getGeoServer().save(global);
    }

    public void testDescribeLayerVersion111() throws Exception {
        String layer = MockData.FORESTS.getPrefix() + ":" + MockData.FORESTS.getLocalPart();
        String request = "wms?service=wms&version=1.1.1&request=DescribeLayer&layers=" + layer;
        assertEquals("src/test/resources/geoserver", getGeoServer().getGlobal().getProxyBaseUrl());
        Document dom = getAsDOM(request, true);
        
        assertEquals("1.1.1", dom.getDocumentElement().getAttributes().getNamedItem("version").getNodeValue());
    }
    
//    public void testDescribeLayerVersion110() throws Exception {
//        String layer = MockData.FORESTS.getPrefix() + ":" + MockData.FORESTS.getLocalPart();
//        String request = "wms?service=wms&version=1.1.0&request=DescribeLayer&layers=" + layer;
//        Document dom = getAsDOM(request);
//        assertEquals("1.1.0", dom.getDocumentElement().getAttributes().getNamedItem("version").getNodeValue());
//    }
}
