package org.geoserver.wms;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class CapabilitiesTest extends WMSTestSupport {
    
    public CapabilitiesTest() {
        super();
    }

    public void testCapabilities() throws Exception {
        Document dom = dom(get("wms?request=getCapabilities"), true);
        Element e = dom.getDocumentElement();
        assertEquals("WMT_MS_Capabilities", e.getLocalName());
    }
    
    
}
