package org.geoserver.wcs;

import static org.geoserver.data.test.MockData.TASMANIA_BM;
import static org.geoserver.data.test.MockData.WORLD;
import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletResponse;

import junit.framework.Test;
import junit.textui.TestRunner;
import net.opengis.wcs11.GetCoverageType;

import org.geoserver.wcs.kvp.GetCoverageRequestReader;
import org.geoserver.wcs.test.WCSTestSupport;
import org.geoserver.wcs.xml.v1_1_1.WCSConfiguration;
import org.geoserver.wcs.xml.v1_1_1.WcsXmlReader;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.GridCoverage;
import org.vfny.geoserver.wcs.WcsException;
import org.w3c.dom.Document;

public class GetCoverageTest extends WCSTestSupport {

    private static final double EPS = 10 - 6;

    private GetCoverageRequestReader kvpreader;

    private WebCoverageService111 service;

    private WCSConfiguration configuration;

    private WcsXmlReader xmlReader;

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetCoverageTest());
    }

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        kvpreader = (GetCoverageRequestReader) applicationContext
                .getBean("wcs111GetCoverageRequestReader");
        service = (WebCoverageService111) applicationContext.getBean("wcs111ServiceTarget");
        configuration = new WCSConfiguration();
        xmlReader = new WcsXmlReader("GetCoverage", "1.1.1", configuration);
    }

    @Override
    protected String getLogConfiguration() {
        return "/DEFAULT_LOGGING.properties";
    }

    // public void testNullGridOrigin() throws Exception {
    // String request = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + //
    // "<wcs:GetCoverage service=\"WCS\" " + //
    // "xmlns:ows=\"http://www.opengis.net/ows/1.1\"\r\n" + //
    // "  xmlns:wcs=\"http://www.opengis.net/wcs/1.1.1\"\r\n" + //
    // "  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \r\n" + //
    // "  xsi:schemaLocation=\"http://www.opengis.net/wcs/1.1.1 " + //
    // "schemas/wcs/1.1.1/wcsAll.xsd\"\r\n" + //
    // "  version=\"1.1.1\" >\r\n" + //
    // "  <ows:Identifier>wcs:BlueMarble</ows:Identifier>\r\n" + //
    // "  <wcs:DomainSubset>\r\n" + //
    // "    <ows:BoundingBox crs=\"urn:ogc:def:crs:EPSG:6.6:4326\">\r\n" + //
    // "      <ows:LowerCorner>-90 -180</ows:LowerCorner>\r\n" + //
    // "      <ows:UpperCorner>90 180</ows:UpperCorner>\r\n" + //
    // "    </ows:BoundingBox>\r\n" + //
    // "  </wcs:DomainSubset>\r\n" + //
    // "  <wcs:Output format=\"image/tiff\">\r\n" + //
    // "    <wcs:GridCRS>\r\n" + //
    // "      <wcs:GridBaseCRS>urn:ogc:def:crs:EPSG:6.6:4326</wcs:GridBaseCRS>\r\n" + //
    // "      <wcs:GridType>urn:ogc:def:method:WCS:1.1:2dSimpleGrid</wcs:GridType>\r\n" + //
    // "      <wcs:GridOffsets>-1 2</wcs:GridOffsets>\r\n" + //
    // "    </wcs:GridCRS>\r\n" + //
    // "  </wcs:Output>\r\n" + //
    // "</wcs:GetCoverage>";
    //    
    // executeGetCoverageXml(request);
    // }

    /**
     * Runs GetCoverage on the specified parameters and returns an array of coverages
     */
    GridCoverage[] executeGetCoverageKvp(Map<String, Object> raw) throws Exception {
        GetCoverageType getCoverage = (GetCoverageType) kvpreader.read(kvpreader.createRequest(),
                parseKvp(raw), raw);
        return service.getCoverage(getCoverage);
    }

    /**
     * Runs GetCoverage on the specified parameters and returns an array of coverages
     */
    GridCoverage[] executeGetCoverageXml(String request) throws Exception {
        GetCoverageType getCoverage = (GetCoverageType) xmlReader.read(null, new StringReader(
                request), null);
        return service.getCoverage(getCoverage);
    }

    public void testKvpBasic() throws Exception {
        Map<String, Object> raw = baseMap();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("identifier", getLayerId);
        raw.put("format", "image/tiff");
        raw.put("BoundingBox", "-90,-180,90,180,urn:ogc:def:crs:EPSG:6.6:4326");
        raw.put("store", "false");
        raw.put("GridBaseCRS", "urn:ogc:def:crs:EPSG:6.6:4326");

        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        assertEquals(1, coverages.length);
        GridCoverage2D coverage = (GridCoverage2D) coverages[0];
        assertEquals(CRS.decode("urn:ogc:def:crs:EPSG:6.6:4326"), coverage.getEnvelope()
                .getCoordinateReferenceSystem());
    }

    private Map<String, Object> baseMap() {
        Map<String, Object> raw = new HashMap<String, Object>();
        raw.put("service", "WCS");
        raw.put("version", "1.1.1");
        raw.put("request", "GetCoverage");
        return raw;
    }

    public void testAntimeridianWorld() throws Exception {
        // for the moment, just make sure we don't die and return something, see
        Map<String, Object> raw = baseMap();
        final String getLayerId = getLayerId(WORLD);
        raw.put("identifier", getLayerId);
        raw.put("format", "image/geotiff");
        raw.put("BoundingBox", "175,10,-175,20,urn:ogc:def:crs:OGC:1.3:CRS84");
        raw.put("store", "false");
        // raw.put("GridBaseCRS", "urn:ogc:def:crs:EPSG:6.6:4326");

        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        assertEquals(1, coverages.length);
        GridCoverage2D coverage = (GridCoverage2D) coverages[0];
        assertEquals(CRS.decode("urn:ogc:def:crs:EPSG:6.6:4326"), coverage.getEnvelope()
                .getCoordinateReferenceSystem());
    }

    public void testAntimeridianTaz() throws Exception {
        // for the moment, just make sure we don't die and return something, see
        Map<String, Object> raw = baseMap();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("identifier", getLayerId);
        raw.put("format", "image/geotiff");
        raw.put("store", "false");

        // complete coverage from left side of request bbox
        raw.put("BoundingBox", "145,-80,-175,0,urn:ogc:def:crs:OGC:1.3:CRS84");
        executeGetCoverageKvp(raw);

        // partial coverage from left side of request bbox
        raw.put("BoundingBox", "147,-80,-175,0,urn:ogc:def:crs:OGC:1.3:CRS84");
        executeGetCoverageKvp(raw);

        // partial coverage from both left and right side
        raw.put("BoundingBox", "147.2,-80,147,0,urn:ogc:def:crs:OGC:1.3:CRS84");
        executeGetCoverageKvp(raw);

        // partial coverage from right side
        raw.put("BoundingBox", "175,-80,147,0,urn:ogc:def:crs:OGC:1.3:CRS84");
        executeGetCoverageKvp(raw);

        // full coverage from right side
        raw.put("BoundingBox", "175,-80,150,0,urn:ogc:def:crs:OGC:1.3:CRS84");
        executeGetCoverageKvp(raw);
    }

    public void testWrongFormatParams() throws Exception {
        Map<String, Object> raw = baseMap();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("identifier", getLayerId);
        raw.put("format", "SuperCoolFormat");
        raw.put("BoundingBox", "-45,146,-42,147,urn:ogc:def:crs:EPSG:6.6:4326");
        try {
            executeGetCoverageKvp(raw);
            fail("When did we learn to encode SuperCoolFormat?");
        } catch (WcsException e) {
            assertEquals(InvalidParameterValue.toString(), e.getCode());
            assertEquals("format", e.getLocator());
        }
    }

    // public void testDefaultGridOrigin() throws Exception {
    // Map<String, Object> raw = new HashMap<String, Object>();
    // final String getLayerId = getLayerId(TASMANIA_BM);
    // raw.put("identifier", getLayerId);
    // raw.put("format", "image/geotiff");
    // raw.put("BoundingBox", "-45,146,-42,147,urn:ogc:def:crs:EPSG:6.6:4326");
    //	
    // GridCoverage[] coverages = executeGetCoverageKvp(raw);
    // AffineTransform2D tx = (AffineTransform2D) coverages[0].getGridGeometry().getGridToCRS();
    // assertEquals(0.0, tx.getTranslateX());
    // assertEquals(0.0, tx.getTranslateY());
    // }

    public void testWrongGridOrigin() throws Exception {
        Map<String, Object> raw = baseMap();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("identifier", getLayerId);
        raw.put("format", "image/geotiff");
        raw.put("BoundingBox", "-45,146,-42,147,urn:ogc:def:crs:EPSG:6.6:4326");
        raw.put("GridOrigin", "12,13,14");
        try {
            executeGetCoverageKvp(raw);
            fail("We should have had a WcsException here?");
        } catch (WcsException e) {
            assertEquals(InvalidParameterValue.name(), e.getCode());
            assertEquals("GridOrigin", e.getLocator());
        }
    }

    public void testWorkspaceQualified() throws Exception {
        String queryString = "&request=getcoverage&service=wcs&version=1.1.1&&format=image/geotiff"
                + "&BoundingBox=-45,146,-42,147,urn:ogc:def:crs:EPSG:6.6:4326";
        ServletResponse r = getAsServletResponse("wcs?identifier=" + TASMANIA_BM.getLocalPart()
                + queryString);
        assertTrue(r.getContentType().startsWith("multipart/related"));

        Document dom = getAsDOM("cdf/wcs?identifier=" + TASMANIA_BM.getLocalPart() + queryString);
        assertEquals("ows:ExceptionReport", dom.getDocumentElement().getNodeName());
    }

    public void testLayerQualified() throws Exception {
        String queryString = "&request=getcoverage&service=wcs&version=1.1.1&&format=image/geotiff"
                + "&BoundingBox=-45,146,-42,147,urn:ogc:def:crs:EPSG:6.6:4326";
        ServletResponse r = getAsServletResponse("wcs/BlueMarble/wcs?identifier=BlueMarble"
                + queryString);
        assertTrue(r.getContentType().startsWith("multipart/related"));

        Document dom = getAsDOM("wcs/DEM/wcs?identifier=BlueMarble" + queryString);
        assertEquals("ows:ExceptionReport", dom.getDocumentElement().getNodeName());
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

}
