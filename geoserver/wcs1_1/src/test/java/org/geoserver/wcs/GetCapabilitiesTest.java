package org.geoserver.wcs;

import static org.custommonkey.xmlunit.XMLAssert.*;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.wcs.test.WCSTestSupport;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.dto.ContactDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class GetCapabilitiesTest extends WCSTestSupport {
    
    private GeoServer geoServer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        geoServer = (GeoServer) applicationContext.getBean("geoServer");
    }
    
//    @Override
//    protected String getDefaultLogConfiguration() {
//        return "/GEOTOOLS_DEVELOPER_LOGGING.properties";
//    }

    public void testGetBasic() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&acceptversions=1.1.1");
//         print(dom);
        checkValidationErrors(dom, WCS11_SCHEMA);
        
        // make sure we provided the store values (for the moment, unsupported, 
        // so store param should be false
        assertXpathEvaluatesTo("False", "/wcs:Capabilities/ows:OperationsMetadata" +
        		"/ows:Operation[@name=\"GetCoverage\"]/ows:Parameter/ows:AllowedValues", dom);
    }
    
    
    
    public void testNoServiceContactInfo() throws Exception {
        // alter geoserver state so that there is no contact information
        GeoServerDTO dto = (GeoServerDTO) geoServer.toDTO();
        dto.setContact(new ContactDTO());
        geoServer.load(dto);
        
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS");
//         print(dom);
        checkValidationErrors(dom, WCS11_SCHEMA);
    }

    public void testPostBasic() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" "
                + "xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>";
        Document dom = postAsDOM(BASEPATH, request);
//        print(dom);
        checkValidationErrors(dom, WCS11_SCHEMA);
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
        checkValidationErrors(dom, WCS11_SCHEMA);
        checkOws11Exception(dom);
        assertEquals("ows:ExceptionReport", dom.getFirstChild().getNodeName());
        assertXpathEvaluatesTo("VersionNegotiationFailed", "ows:ExceptionReport/ows:Exception/@exceptionCode", dom);
    }
    
    public void testUnsupportedVersionGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&acceptVersions=9.9.9,8.8.8");
        checkValidationErrors(dom, WCS11_SCHEMA);
        checkOws11Exception(dom);
        assertXpathEvaluatesTo("VersionNegotiationFailed", "ows:ExceptionReport/ows:Exception/@exceptionCode", dom);
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
    
    public void testUpdateSequenceInferiorGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&updateSequence=-1");
        checkValidationErrors(dom, WCS11_SCHEMA);
        final Node root = dom.getFirstChild();
        assertEquals("wcs:Capabilities", root.getNodeName());
        assertTrue(root.getChildNodes().getLength() > 0);
    }
    
    public void testUpdateSequenceInferiorPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " updateSequence=\"-1\"/>";
        Document dom = postAsDOM(BASEPATH, request);
        checkValidationErrors(dom, WCS11_SCHEMA);
        final Node root = dom.getFirstChild();
        assertEquals("wcs:Capabilities", root.getNodeName());
        assertTrue(root.getChildNodes().getLength() > 0);
    }
    
    public void testUpdateSequenceEqualsGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&updateSequence=0");
        checkValidationErrors(dom, WCS11_SCHEMA);
        final Node root = dom.getFirstChild();
        assertEquals("wcs:Capabilities", root.getNodeName());
        assertEquals(0, root.getChildNodes().getLength());
    }
    
    public void testUpdateSequenceEqualsPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " updateSequence=\"0\"/>";
        Document dom = postAsDOM(BASEPATH, request);
        checkValidationErrors(dom, WCS11_SCHEMA);
        final Node root = dom.getFirstChild();
        assertEquals("wcs:Capabilities", root.getNodeName());
        assertEquals(0, root.getChildNodes().getLength());
    }
    
    public void testUpdateSequenceSuperiorGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&updateSequence=1");
        checkValidationErrors(dom, WCS11_SCHEMA);
//        print(dom);
        assertXpathEvaluatesTo("1", "count(/ows:ExceptionReport)", dom);
    }
    
    public void testUpdateSequenceSuperiorPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " updateSequence=\"1\"/>";
        Document dom = postAsDOM(BASEPATH, request);
        checkValidationErrors(dom, WCS11_SCHEMA);
//        print(dom);
        assertXpathEvaluatesTo("1", "count(/ows:ExceptionReport)", dom);
    }
    
    public void testSectionsIgnoreGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&sections=Bogus");
        checkValidationErrors(dom, WCS11_SCHEMA);
        final Node root = dom.getFirstChild();
        assertEquals("wcs:Capabilities", root.getNodeName());
        assertTrue(root.getChildNodes().getLength() > 0);
    }
    
//    public void testBogusSectionPost() throws Exception {
//        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
//            + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
//            + " xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\""
//            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
//            + "  <ows:Sections>\r\n" 
//            + "    <ows:Section>Bogus</ows:Section>\r\n" 
//            + "  </ows:Sections> " 
//            + "</wcs:GetCapabilities>";
//        Document dom = postAsDOM(BASEPATH, request);
//        assertEquals("ows:ExceptionReport", dom.getFirstChild().getNodeName());
//    }
}
