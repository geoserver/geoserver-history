package org.geoserver.wcs;

import static org.custommonkey.xmlunit.XMLAssert.*;
import junit.framework.Test;

import org.geoserver.wcs.test.WCSTestSupport;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.dto.ContactDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.wcs.WcsException.WcsExceptionCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class GetCapabilitiesTest extends WCSTestSupport {
    
    private static GeoServer geoServer;
    
    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetCapabilitiesTest());
    }
    
    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        geoServer = (GeoServer) applicationContext.getBean("geoServer");
    }
    
//    @Override
//    protected String getDefaultLogConfiguration() {
//        return "/GEOTOOLS_DEVELOPER_LOGGING.properties";
//    }

    public void testGetBasic() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&version=1.0.0");
//         print(dom);
        checkValidationErrors(dom, WCS10_SCHEMA);
    }
    
    
    
    public void testNoServiceContactInfo() throws Exception {
        // alter geoserver state so that there is no contact information
        GeoServerDTO dto = (GeoServerDTO) geoServer.toDTO();
        dto.setContact(new ContactDTO());
        geoServer.load(dto);
        
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS");
//         print(dom);
        checkValidationErrors(dom, WCS10_SCHEMA);
    }

    public void testPostBasic() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\" "
                + "xmlns:wcs=\"http://www.opengis.net/wcs\" "
                + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"/>";
        Document dom = postAsDOM(BASEPATH, request);
//        print(dom);
        checkValidationErrors(dom, WCS10_SCHEMA);
    }

    public void testUnsupportedVersionPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"9.9.9\">"
                + "</wcs:GetCapabilities>";
        Document dom = postAsDOM(BASEPATH, request);
//        print(dom);
        checkOws11Exception(dom);
        assertEquals("ows:ExceptionReport", dom.getFirstChild().getNodeName());
        assertXpathEvaluatesTo("NoApplicableCode", "ows:ExceptionReport/ows:Exception/@exceptionCode", dom);
    }
    
    public void testUnsupportedVersionGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&version=9.9.9");
        checkOws11Exception(dom);
        assertXpathEvaluatesTo("VersionNegotiationFailed", "ows:ExceptionReport/ows:Exception/@exceptionCode", dom);
    }
    
    public void testUpdateSequenceInferiorGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&updateSequence=-1");
        checkValidationErrors(dom, WCS10_SCHEMA);
        final Node root = dom.getFirstChild();
        assertEquals("wcs:WCS_Capabilities", root.getNodeName());
        assertTrue(root.getChildNodes().getLength() > 0);
    }
    
    public void testUpdateSequenceInferiorPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " updateSequence=\"-1\"/>";
        Document dom = postAsDOM(BASEPATH, request);
        checkValidationErrors(dom, WCS10_SCHEMA);
        final Node root = dom.getFirstChild();
        assertEquals("wcs:WCS_Capabilities", root.getNodeName());
        assertTrue(root.getChildNodes().getLength() > 0);
    }
    
    public void testUpdateSequenceEqualsGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&version=1.0.0&updateSequence=0");
        final Node root = dom.getFirstChild();
        assertEquals("wcs:WCS_Capabilities", root.getNodeName());
        assertEquals(0, root.getChildNodes().getLength());
    }
    
    public void testUpdateSequenceEqualsPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " updateSequence=\"0\"/>";
        Document dom = postAsDOM(BASEPATH, request);
        final Node root = dom.getFirstChild();
        assertEquals("wcs:WCS_Capabilities", root.getNodeName());
        assertEquals(0, root.getChildNodes().getLength());
    }
    
    public void testUpdateSequenceSuperiorGet() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&version=1.0.0&updateSequence=1");
//        print(dom);
        checkOws11Exception(dom);
    }
    
    public void testUpdateSequenceSuperiorPost() throws Exception {
        String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<wcs:GetCapabilities service=\"WCS\" xmlns:ows=\"http://www.opengis.net/ows/1.1\""
                + " xmlns:wcs=\"http://www.opengis.net/wcs\""
                + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
                + " updateSequence=\"1\" version=\"1.0.0\"/>";
        Document dom = postAsDOM(BASEPATH, request);
        checkOws11Exception(dom);
    }
    
    public void testSectionsBogus() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&version=1.0.0&section=Bogus");
        checkOws11Exception(dom);
        assertXpathEvaluatesTo(WcsExceptionCode.InvalidParameterValue.toString(), "/ows:ExceptionReport/ows:Exception/@exceptionCode", dom);
    }
    
    public void testSectionsAll() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&version=1.0.0&section=/");
        checkValidationErrors(dom, WCS10_SCHEMA);
        assertXpathEvaluatesTo("1", "count(//wcs:Service)", dom);
        assertXpathEvaluatesTo("1", "count(//wcs:Capability)", dom);
        assertXpathEvaluatesTo("1", "count(//wcs:ContentMetadata)", dom);
    }
    
    public void testOneSection() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=GetCapabilities&service=WCS&version=1.0.0&section=/WCS_Capabilities/Service");
        assertXpathEvaluatesTo("1", "count(//wcs:Service)", dom);
        assertXpathEvaluatesTo("0", "count(//wcs:Capability)", dom);
        assertXpathEvaluatesTo("0", "count(//wcs:ContentMetadata)", dom);
    }
}
