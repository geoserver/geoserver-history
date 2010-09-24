package org.vfny.geoserver.wcs;

import static org.geoserver.data.test.MockData.TASMANIA_BM;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.textui.TestRunner;

import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.geoserver.config.GeoServer;
import org.geoserver.data.test.MockData;
import org.geoserver.test.GeoServerTestSupport;
import org.geoserver.wcs.WCSInfo;
import org.w3c.dom.Document;

/**
 * Tests for GetCoverage operation on WCS.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class GetCoverageTest extends GeoServerTestSupport {

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetCoverageTest());
    }

    private static XpathEngine xpath;
    
    @Override
    protected void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();

        // init xmlunit
        Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("wcs", "http://www.opengis.net/wcs");
        namespaces.put("ows", "http://www.opengis.net/ows/1.1");
        namespaces.put("ogc", "http://www.opengis.net/ogc");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
        xpath = XMLUnit.newXpathEngine();
    }
    
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addWellKnownCoverageTypes();
    }


    public void testInputLimits() throws Exception {
        try {
            // ridicolous limit, just one byte
            setInputLimit(1);
            String queryString = "&request=getcoverage&service=wcs&version=1.0.0&format=TIFF&bbox=146,-45,147,-42"
                    + "&crs=EPSG:4326&width=150&height=150";
            Document dom = getAsDOM("wcs/BlueMarble/wcs?coverage=" + getLayerId(TASMANIA_BM)
                    + queryString);
            // print(dom);
            // check it's an error, check we're getting it because of the input limits
            assertEquals("ServiceExceptionReport", dom.getDocumentElement().getNodeName());
            String error = xpath.evaluate(
                    "/ServiceExceptionReport/ServiceException/text()", dom).trim();
            assertTrue(Pattern.compile(".*read too much data.*", Pattern.DOTALL).matcher(error).matches());
        } finally {
            setInputLimit(0);
        }
    }

    public void testOutputLimits() throws Exception {
        try {
            // ridicolous limit, just one byte
            setOutputLimit(1);
            String queryString = "&request=getcoverage&service=wcs&version=1.0.0&format=TIFF&bbox=146,-45,147,-42"
                    + "&crs=EPSG:4326&width=150&height=150";
            Document dom = getAsDOM("wcs/BlueMarble/wcs?coverage=" + getLayerId(TASMANIA_BM)
                    + queryString);
            // print(dom);
            // check it's an error, check we're getting it because of the output limits
            assertEquals("ServiceExceptionReport", dom.getDocumentElement().getNodeName());
            String error = xpath.evaluate(
                    "/ServiceExceptionReport/ServiceException/text()", dom).trim();
            assertTrue(Pattern.compile(".*generate too much data.*", Pattern.DOTALL).matcher(error).matches());
        } finally {
            setOutputLimit(0);
        }
    }

    private void setInputLimit(int kbytes) {
        GeoServer gs = getGeoServer();
        WCSInfo info = gs.getService(WCSInfo.class);
        info.setMaxInputMemory(kbytes);
        gs.save(info);
    } 
    

    private void setOutputLimit(int kbytes) {
        GeoServer gs = getGeoServer();
        WCSInfo info = gs.getService(WCSInfo.class);
        info.setMaxOutputMemory(kbytes);
        gs.save(info);
    } 

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

}
