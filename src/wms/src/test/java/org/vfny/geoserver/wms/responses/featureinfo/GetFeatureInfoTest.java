package org.vfny.geoserver.wms.responses.featureinfo;

import static org.custommonkey.xmlunit.XMLAssert.*;

import javax.xml.namespace.QName;

import junit.framework.Test;

import org.geoserver.data.test.MockData;
import org.geoserver.wms.WMSTestSupport;
import org.w3c.dom.Document;

import com.mockrunner.mock.web.MockHttpServletResponse;

public class GetFeatureInfoTest extends WMSTestSupport {
    
    public static String WCS_PREFIX = "wcs";
    public static String WCS_URI = "http://www.opengis.net/wcs/1.1.1";
    public static QName TASMANIA_BM = new QName(WCS_URI, "BlueMarble", WCS_PREFIX);

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetFeatureInfoTest());
    }
    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        getWMS().setMaxBuffer(50);
    }

    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addCoverage(TASMANIA_BM, GetFeatureInfoTest.class.getResource("tazbm.tiff"),
                "tiff", null);
        dataDirectory.addStyle("thickStroke", GetFeatureInfoTest.class.getResource("thickStroke.sld"));
    }
    
    /**
     * Tests a simple GetFeatureInfo works, and that the result contains the
     * expected polygon
     * 
     * @throws Exception
     */
    public void testSimple() throws Exception {
        String layer = getLayerId(MockData.FORESTS);
        String request = "wms?bbox=-0.002,-0.002,0.002,0.002&styles=&format=jpeg&info_format=text/plain&request=GetFeatureInfo&layers="
                + layer + "&query_layers=" + layer + "&width=20&height=20&x=10&y=10";
        String result = getAsString(request);
        assertNotNull(result);
        assertTrue(result.indexOf("Green Forest") > 0);
    }
    
    /**
     * Tests a simple GetFeatureInfo works, and that the result contains the
     * expected polygon
     * 
     * @throws Exception
     */
    public void testSimpleHtml() throws Exception {
        String layer = getLayerId(MockData.FORESTS);
        String request = "wms?bbox=-0.002,-0.002,0.002,0.002&styles=&format=jpeg&info_format=text/html&request=GetFeatureInfo&layers="
                + layer + "&query_layers=" + layer + "&width=20&height=20&x=10&y=10";
        Document dom = getAsDOM(request);
        // count lines that do contain a forest reference
        assertXpathEvaluatesTo("1", "count(/html/body/table/tr/td[starts-with(.,'Forests.')])", dom);
    }
    
    /**
     * Tests GetFeatureInfo with a buffer specified works, and that the result contains the
     * expected polygon
     * 
     * @throws Exception
     */
    public void testBuffer() throws Exception {
        // to setup the request and the buffer I rendered BASIC_POLYGONS using GeoServer, then played
        // against the image coordinates
        String layer = getLayerId(MockData.BASIC_POLYGONS);
        String base = "wms?bbox=-4.5,-2.,4.5,7&styles=&format=jpeg&info_format=text/html&request=GetFeatureInfo&layers="
                + layer + "&query_layers=" + layer + "&width=300&height=300";
        Document dom = getAsDOM(base + "&x=85&y=230");
        // make sure the document is empty, as we chose an area with no features inside
        assertXpathEvaluatesTo("0", "count(/html/body/table/tr)", dom);

        // another request that will catch one feature due to the extended buffer, make sure it's in
        dom = getAsDOM(base + "&x=85&y=230&buffer=40");
        assertXpathEvaluatesTo("1", "count(/html/body/table/tr/td[starts-with(.,'BasicPolygons.')])", dom);
        assertXpathEvaluatesTo("1", "count(/html/body/table/tr/td[. = 'BasicPolygons.1107531493630'])", dom);
        
        // this one would end up catching everything (3 features) if it wasn't that we say the max buffer at 50
        // in the WMS configuration
        dom = getAsDOM(base + "&x=85&y=230&buffer=300");
        assertXpathEvaluatesTo("1", "count(/html/body/table/tr/td[starts-with(.,'BasicPolygons.')])", dom);
        assertXpathEvaluatesTo("1", "count(/html/body/table/tr/td[. = 'BasicPolygons.1107531493630'])", dom);
    }
    
    /**
     * Tests GetFeatureInfo with a buffer specified works, and that the result contains the
     * expected polygon
     * 
     * @throws Exception
     */
    public void testAutoBuffer() throws Exception {
        String layer = getLayerId(MockData.BASIC_POLYGONS);
        String base = "wms?bbox=-4.5,-2.,4.5,7&format=jpeg&info_format=text/html&request=GetFeatureInfo&layers="
                + layer + "&query_layers=" + layer + "&width=300&height=300&x=115&y=230";
        Document dom = getAsDOM(base + "&styles=");
        // make sure the document is empty, the style we chose has thin lines
        assertXpathEvaluatesTo("0", "count(/html/body/table/tr)", dom);

        // another request that will catch one feature due to the style with a thick stroke, make sure it's in
        dom = getAsDOM(base + "&styles=thickStroke");
        assertXpathEvaluatesTo("1", "count(/html/body/table/tr/td[starts-with(.,'BasicPolygons.')])", dom);
        assertXpathEvaluatesTo("1", "count(/html/body/table/tr/td[. = 'BasicPolygons.1107531493630'])", dom);
    }
    
    /**
     * Tests a GetFeatureInfo againworks, and that the result contains the
     * expected polygon
     * 
     * @throws Exception
     */
    public void testTwoLayers() throws Exception {
        String layer = getLayerId(MockData.FORESTS) + "," + getLayerId(MockData.LAKES);
        String request = "wms?bbox=-0.002,-0.002,0.002,0.002&styles=&format=jpeg&info_format=text/html&request=GetFeatureInfo&layers="
                + layer + "&query_layers=" + layer + "&width=20&height=20&x=10&y=10&info";
        String result = getAsString(request);
        assertNotNull(result);
        assertTrue(result.indexOf("Green Forest") > 0);
        // GEOS-2603 GetFeatureInfo returns html tables without css style if more than one layer is selected
        assertTrue(result.indexOf("<style type=\"text/css\">") > 0);

    }

    /**
     * Check GetFeatureInfo returns an error if the format is not known, instead
     * of returning the text format as in
     * http://jira.codehaus.org/browse/GEOS-1924
     * 
     * @throws Exception
     */
    public void testUknownFormat() throws Exception {
        String layer = MockData.FORESTS.getPrefix() + ":" + MockData.FORESTS.getLocalPart();
        String request = "wms?bbox=-0.002,-0.002,0.002,0.002&styles=&format=jpeg&info_format=unknown/format&request=GetFeatureInfo&layers="
                + layer + "&query_layers=" + layer + "&width=20&height=20&x=10&y=10";
        Document doc = dom(get(request), true);
        print(doc);
        assertXpathEvaluatesTo("1", "count(//ServiceExceptionReport/ServiceException)", doc);
        assertXpathEvaluatesTo("InvalidParameterValue", "/ServiceExceptionReport/ServiceException/@code", doc);
        assertXpathEvaluatesTo("info_format", "/ServiceExceptionReport/ServiceException/@locator", doc);
    }
    
    public void testCoverage() throws Exception {
        // http://jira.codehaus.org/browse/GEOS-2574
        String layer = getLayerId(TASMANIA_BM);
        String request = "wms?service=wms&request=GetFeatureInfo&version=1.1.1" +
        		"&layers=" + layer + "&styles=&bbox=146.5,-44.5,148,-43&width=600&height=600" + 
        		"&info_format=text/html&query_layers=" + layer + "&x=300&y=300&srs=EPSG:4326";
        MockHttpServletResponse resp = getAsServletResponse(request);
        // we also have the charset which may be platf. dep.
        assertTrue(resp.getContentType().startsWith("text/html"));
        assertTrue(resp.getOutputStreamContent().contains("RED_BAND"));
        assertTrue(resp.getOutputStreamContent().contains("GREEN_BAND"));
        assertTrue(resp.getOutputStreamContent().contains("BLUE_BAND"));
    }
    
    
}
