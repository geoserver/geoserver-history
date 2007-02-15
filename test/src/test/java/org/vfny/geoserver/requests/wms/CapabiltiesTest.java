/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests.wms;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;
import com.meterware.httpunit.WebRequest;
import com.meterware.httpunit.WebResponse;
import org.apache.xerces.parsers.DOMParser;
import org.vfny.geoserver.AbstractGeoserverHttpTest;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import java.util.logging.Logger;


public class CapabiltiesTest extends AbstractGeoserverHttpTest {
    /** Standard logging instance */
    private static final Logger LOGGER = Logger.getLogger("org.vfny.geoserver.requests.wms");

    public void testGetCapabilities() throws Exception {
        if (isOffline()) {
            return;
        }

        LOGGER.info("Test: CapabiltiesTest.testGetCapabilities()");

        WebConversation conversation = new WebConversation();
        WebRequest request = new GetMethodWebRequest(getBaseUrl() + "/wms?request=getCapabilities");

        WebResponse response = conversation.getResponse(request);
        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(response.getInputStream()));

        Element e = parser.getDocument().getDocumentElement();
        assertEquals("WMT_MS_Capabilities", e.getLocalName());
    }
}
