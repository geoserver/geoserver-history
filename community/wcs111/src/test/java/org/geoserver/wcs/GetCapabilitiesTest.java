package org.geoserver.wcs;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.wcs.test.WCSTestSupport;
import org.w3c.dom.Document;

public class GetCapabilitiesTest extends WCSTestSupport {

    public void testGetBasic() throws Exception {
        List<Exception> errors = new ArrayList<Exception>();
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS", errors);
        // print(dom);
        checkValidationErrors(errors);
    }

    // Commented out waiting for a fix of http://jira.codehaus.org/browse/GEOT-1635
//    public void testPostBasic() throws Exception {
////        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
////                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
////                + "xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\""
////                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
////                + "  <ows:AcceptVersions>" // 
////                + "    <ows:Version>ows:Version</ows:Version>" //
////                + "  </ows:AcceptVersions>" // 
////                + "  <ows:Sections>" //
////                + "    <ows:Section>ows:Section</ows:Section>" // 
////                + "  </ows:Sections>" //
////                + "  <ows:AcceptFormats>" //
////                + "    <ows:OutputFormat>ows:OutputFormat</ows:OutputFormat>" //
////                + "  </ows:AcceptFormats>" // 
////                + "</wcs:GetCapabilities>";
//        
//        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//            + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" "
//            + "xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" "
//            + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>";
//        System.out.println(request);
//        List<Exception> errors = new ArrayList<Exception>();
//        Document dom = postAsDOM(BASEPATH, request, errors);
//        checkValidationErrors(errors);
//    }
}
