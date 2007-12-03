package org.geoserver.wcs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.xpath.XPathAPI;
import org.geoserver.wcs.test.WCSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DescribeCoverageTest extends WCSTestSupport {

//    @Override
//    protected String getDefaultLogConfiguration() {
//        return "/GEOSERVER_DEVELOPER_LOGGING.properties";
//    }

    public void testDescribeNoIdentifiers() throws Exception {
        Document dom = getAsDOM(BASEPATH + "?request=DescribeCoverage&service=WCS&version=1.1.1");
//        print(dom);
        assertEquals(1, dom.getElementsByTagName("ows:ExceptionReport").getLength());
        Element element = (Element) dom.getElementsByTagName("ows:Exception").item(0);
        assertEquals("MissingParameterValue", element.getAttribute("exceptionCode"));
        assertEquals("identifiers", element.getAttribute("locator"));
    }

    public void testDescribeUnknownCoverage() throws Exception {
        Document dom = getAsDOM(BASEPATH
                + "?request=DescribeCoverage&service=WCS&version=1.1.1&identifiers=plop");
//        print(dom);
        assertEquals(1, dom.getElementsByTagName("ows:ExceptionReport").getLength());
        Element element = (Element) dom.getElementsByTagName("ows:Exception").item(0);
        assertEquals("InvalidParameterValue", element.getAttribute("exceptionCode"));
        assertEquals("identifiers", element.getAttribute("locator"));
        assertTrue(element.getTextContent().contains("plop"));
    }

    public void testDescribeDemCoverage() throws Exception {
        List<Exception> errors = new ArrayList<Exception>();
        Document dom = getAsDOM(BASEPATH
                + "?request=DescribeCoverage&service=WCS&version=1.1.1&identifiers="
                + layerId(WCSTestSupport.TASMANIA_DEM), errors);
//        print(dom);
        checkValidationErrors(errors);
        // check the basics, the output is a single coverage description with the expected id
        assertEquals(1, dom.getElementsByTagName("wcs:CoverageDescriptions").getLength());
        assertEquals(1, dom.getElementsByTagName("wcs:CoverageDescription").getLength());
        Node identifier = XPathAPI.selectSingleNode(dom, "/wcs:CoverageDescriptions/wcs:CoverageDescription/wcs:Identifier");
        assertEquals(layerId(WCSTestSupport.TASMANIA_DEM), identifier.getTextContent());
        // check there is no rotation
        Node gridOffsets = XPathAPI.selectSingleNode(dom, "/wcs:CoverageDescriptions/wcs:CoverageDescription/" +
                "wcs:Domain/wcs:SpatialDomain/wcs:GridCRS/wcs:GridOffsets");
        String[] offsetStrs = gridOffsets.getTextContent().split(" ");
        assertEquals(4, offsetStrs.length);
        double[] offsets = new double[4];
        for (int i = 0; i < offsetStrs.length; i++) {
            offsets[i] = Double.parseDouble(offsetStrs[i]);
        }
        assertTrue(offsets[0] > 0);
        assertEquals(0.0, offsets[1]);
        assertEquals(0.0, offsets[2]);
        assertTrue(offsets[3] < 0);
        // check there is one field, one axis, one key (this one is a dem, just one band)
        assertEquals(1, dom.getElementsByTagName("wcs:Field").getLength());
        assertEquals(1, dom.getElementsByTagName("wcs:Axis").getLength());
        assertEquals(1, dom.getElementsByTagName("wcs:Key").getLength());

    }
    
    public void testDescribeImageCoverage() throws Exception {
        List<Exception> errors = new ArrayList<Exception>();
        Document dom = getAsDOM(BASEPATH
                + "?request=DescribeCoverage&service=WCS&version=1.1.1&identifiers="
                + layerId(WCSTestSupport.TASMANIA_BM), errors);
//        print(dom);
        checkValidationErrors(errors);
        // check the basics, the output is a single coverage description with the expected id
        assertEquals(1, dom.getElementsByTagName("wcs:CoverageDescriptions").getLength());
        assertEquals(1, dom.getElementsByTagName("wcs:CoverageDescription").getLength());
        Node identifier = XPathAPI.selectSingleNode(dom, "/wcs:CoverageDescriptions/wcs:CoverageDescription/wcs:Identifier");
        assertEquals(layerId(WCSTestSupport.TASMANIA_BM), identifier.getTextContent());
        // check there is no rotation
        Node gridOffsets = XPathAPI.selectSingleNode(dom, "/wcs:CoverageDescriptions/wcs:CoverageDescription/" +
        		"wcs:Domain/wcs:SpatialDomain/wcs:GridCRS/wcs:GridOffsets");
        String[] offsetStrs = gridOffsets.getTextContent().split(" ");
        assertEquals(4, offsetStrs.length);
        double[] offsets = new double[4];
        for (int i = 0; i < offsetStrs.length; i++) {
            offsets[i] = Double.parseDouble(offsetStrs[i]);
        }
        assertTrue(offsets[0] > 0);
        assertEquals(0.0, offsets[1]);
        assertEquals(0.0, offsets[2]);
        assertTrue(offsets[3] < 0);
        // check there is one field, one axis, three keys (bands)
        assertEquals(1, dom.getElementsByTagName("wcs:Field").getLength());
        assertEquals(1, dom.getElementsByTagName("wcs:Axis").getLength());
        assertEquals(3, dom.getElementsByTagName("wcs:Key").getLength());
    }
    
    public void testDescribeRotatedCoverage() throws Exception {
//        fail("Remember to write this one using a world image");
    }
}
