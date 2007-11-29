package org.geoserver.wcs;

import org.geoserver.wcs.test.WCSTestSupport;

public class GetCapabilitiesTest extends WCSTestSupport {
    
    public void testBasicCapabilities() throws Exception {
        getAsDomAndValidate(BASEPATH + "?request=GetCapabilities&service=WCS");
    }
}
