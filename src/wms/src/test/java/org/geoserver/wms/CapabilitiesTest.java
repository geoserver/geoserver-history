package org.geoserver.wms;

import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
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
    
    public void testFilteredCapabilitiesCite() throws Exception {
        Document dom = dom(get("wms?request=getCapabilities&namespace=cite"), true);
        Element e = dom.getDocumentElement();
        assertEquals("WMT_MS_Capabilities", e.getLocalName());
        XpathEngine xpath =  XMLUnit.newXpathEngine();
        assertTrue(xpath.getMatchingNodes("//Layer/Name[starts-with(., cite)]", dom).getLength() > 0);
        assertEquals(0, xpath.getMatchingNodes("//Layer/Name[not(starts-with(., cite))]", dom).getLength());
    }
    
}
