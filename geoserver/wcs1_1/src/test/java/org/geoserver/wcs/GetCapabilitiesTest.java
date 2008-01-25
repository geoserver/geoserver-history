package org.geoserver.wcs;

import java.util.ArrayList;
import java.util.List;

import org.apache.xpath.XPathAPI;
import org.geoserver.wcs.test.WCSTestSupport;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.dto.ContactDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class GetCapabilitiesTest extends WCSTestSupport {
    
    private GeoServer geoServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        geoServer = (GeoServer) applicationContext.getBean("geoServer");
    }

    public void testGetBasic() throws Exception {
        List<Exception> errors = new ArrayList<Exception>();
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS", errors);
         print(dom);
        checkValidationErrors(errors);
    }
    
    public void testNoServiceContactInfo() throws Exception {
        // alter geoserver state so that there is no concact information
        GeoServerDTO dto = (GeoServerDTO) geoServer.toDTO();
        dto.setContact(new ContactDTO());
        geoServer.load(dto);
        
        List<Exception> errors = new ArrayList<Exception>();
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS", errors);
         print(dom);
        checkValidationErrors(errors);
    }

    public void testPostBasic() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" "
                + "xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>";
        List<Exception> errors = new ArrayList<Exception>();
        Document dom = postAsDOM(BASEPATH, request, errors);
        print(dom);
        checkValidationErrors(errors);
    }

    public void testUnsupportedVersionPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "  <ows:AcceptVersions>" // 
                + "    <ows:Version>9.9.9</ows:Version>" //
                + "  </ows:AcceptVersions>" // 
                + "</wcs:GetCapabilities>";
        Document dom = postAsDOM(BASEPATH, request);
        assertEquals("ows:ExceptionReport", dom.getFirstChild().getNodeName());
        Node node = XPathAPI.selectSingleNode(dom, "ows:ExceptionReport/ows:Exception/@exceptionCode");
        assertEquals("VersionNegotiationFailed", node.getTextContent());
    }
    
    public void testUnsupportedVersionGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&acceptVersions=9.9.9,8.8.8");
        assertEquals("ows:ExceptionReport", dom.getFirstChild().getNodeName());
        Node node = XPathAPI.selectSingleNode(dom, "ows:ExceptionReport/ows:Exception/@exceptionCode");
        assertEquals("VersionNegotiationFailed", node.getTextContent());
    }
    
    public void testSupportedVersionGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&acceptVersions=0.5.0,1.1.1");
        assertEquals("wcs:Capabilities", dom.getFirstChild().getNodeName());
    }
    
    public void testSupportedVersionPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
                + "  <ows:AcceptVersions>" // 
                + "    <ows:Version>0.5.0</ows:Version>" //
                + "    <ows:Version>1.1.1</ows:Version>" //
                + "  </ows:AcceptVersions>" // 
                + "</wcs:GetCapabilities>";
        Document dom = postAsDOM(BASEPATH, request);
        assertEquals("wcs:Capabilities", dom.getFirstChild().getNodeName());
    }
    
    
}
