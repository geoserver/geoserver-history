package org.vfny.geoserver.requests.wms;

import org.apache.xerces.parsers.DOMParser;
import org.vfny.geoserver.AbstractGeoserverHttpTest;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;

public class CapabiltiesTest extends AbstractGeoserverHttpTest {

	public void testGetCapabilities() throws Exception {
		WebConversation conversation = new WebConversation();
        WebRequest request = 
        	new GetMethodWebRequest(getBaseUrl()+"/wms?request=getCapabilities");
        
        WebResponse response = conversation.getResponse( request );
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(response.getInputStream()));
        
        Element e = parser.getDocument().getDocumentElement();
        assertEquals("WMT_MS_Capabilities", e.getLocalName());
    }
}
