package org.geoserver.wms;

import org.w3c.dom.Document;

public class WMSDisabledTest extends WMSTestSupport {
    
    public void testDisabledServiceResponse() throws Exception {
        WMSInfo wms = getGeoServer().getService(WMSInfo.class);
        wms.setEnabled(false);
        getGeoServer().save(wms);
        
        Document doc = getAsDOM("wms?service=WMS&request=getCapabilities");
        assertEquals("ows:ExceptionReport", doc.getDocumentElement()
                .getNodeName());
    }
    
    public void testEnabledServiceResponse() throws Exception {
        WMSInfo wms = getGeoServer().getService(WMSInfo.class);
        wms.setEnabled(true);
        getGeoServer().save(wms);

        Document doc = getAsDOM("wms?service=WMS&request=getCapabilities");
        assertEquals("WMT_MS_Capabilities", doc.getDocumentElement()
                .getNodeName());
    }
}
