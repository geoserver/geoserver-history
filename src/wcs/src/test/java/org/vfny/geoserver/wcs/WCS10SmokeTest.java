package org.vfny.geoserver.wcs;

import org.geoserver.test.GeoServerTestSupport;
import org.w3c.dom.Document;

/**
 * Simply makes sure all is properly wired up and we can make a WCS 1.0 caps request succefully
 * @author aaime
 *
 */
public class WCS10SmokeTest extends GeoServerTestSupport {
	
	public void testCapabilities() throws Exception {
		Document dom = getAsDOM("ows?service=WCS&version=1.0.0&request=getCapabilities");
		assertEquals("WCSCapabilities", dom.getDocumentElement().getNodeName());
	}
}
