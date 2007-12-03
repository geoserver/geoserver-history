package org.geoserver.wcs;

import java.util.ArrayList;
import java.util.List;

import org.geoserver.wcs.test.WCSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
    }
    
    public void testDescribeImageCoverage() throws Exception {
        List<Exception> errors = new ArrayList<Exception>();
        Document dom = getAsDOM(BASEPATH
                + "?request=DescribeCoverage&service=WCS&version=1.1.1&identifiers="
                + layerId(WCSTestSupport.TASMANIA_BM), errors);
//      print(dom);
        checkValidationErrors(errors);
    }
    
    public void testDescribeRotatedCoverage() throws Exception {
//        fail("Remember to write this one using a world image");
    }
}
