package org.geoserver.wcs;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.wcs.test.WCSTestSupport;
import org.w3c.dom.Document;

public class GetCapabilitiesTest extends WCSTestSupport {
    
    public void testBasicCapabilities() throws Exception {
        List<Exception> errors = new ArrayList<Exception>();
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS", null);
//        print(dom);
        checkValidationErrors(errors);
    }
}
