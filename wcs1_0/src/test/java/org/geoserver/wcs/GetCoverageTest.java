package org.geoserver.wcs;

import static org.geoserver.data.test.MockData.TASMANIA_BM;
import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.textui.TestRunner;
import net.opengis.wcs10.GetCoverageType;

import org.geoserver.wcs.kvp.Wcs10GetCoverageRequestReader;
import org.geoserver.wcs.test.WCSTestSupport;
import org.geoserver.wcs.xml.v1_0_0.WcsXmlReader;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.wcs.WCSConfiguration;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.vfny.geoserver.wcs.WcsException;

public class GetCoverageTest extends WCSTestSupport {

    private Wcs10GetCoverageRequestReader kvpreader;

    private WebCoverageService100 service;

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
        kvpreader = (Wcs10GetCoverageRequestReader) applicationContext
                .getBean("wcs100GetCoverageRequestReader");
        service = (WebCoverageService100) applicationContext.getBean("wcs100ServiceTarget");
        configuration = new WCSConfiguration();
        xmlReader = new WcsXmlReader("GetCoverage", "1.0.0", configuration);
    }

    @Override
    protected String getLogConfiguration() {
        return "/DEFAULT_LOGGING.properties";
    }

    public void testKvpBasic() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String layerId = getLayerId(TASMANIA_BM);
        raw.put("coverage",layerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "-180,-90,180,90");
        raw.put("CRS", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");

        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        assertEquals(1, coverages.length);
        GridCoverage2D coverage = (GridCoverage2D) coverages[0];
        CoordinateReferenceSystem cvCRS = CRS.decode(CRS.lookupIdentifier(coverage.getEnvelope()
                .getCoordinateReferenceSystem(), true), true);
        assertTrue(CRS.equalsIgnoreMetadata(DefaultGeographicCRS.WGS84, cvCRS));
    }

    public void testWrongFormatParams() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("coverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "SuperCoolFormat");
        raw.put("BBox", "146,-45,147,-42");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");

        try {
            @SuppressWarnings("unused")
			GridCoverage[] coverages = executeGetCoverageKvp(raw);
            fail("When did we learn to encode SuperCoolFormat?");
        } catch (WcsException e) {
            assertEquals(InvalidParameterValue.toString(), e.getCode());
            assertEquals("format", e.getLocator());
        }
    }

    public void testDefaultGridOrigin() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("sourcecoverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "146.49999999999477,-44.49999999999785,147.0,-42.99999999999787");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");

        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        AffineTransform2D tx = (AffineTransform2D) coverages[0].getGridGeometry().getGridToCRS();
        // assertEquals(146.49999999999477, tx.getTranslateX());
        // assertEquals(-42.99999999999787, tx.getTranslateY());
        // ?
    }

    /**
     * Tests valid range subset expressions, but with a mix of valid and invalid identifiers
     * 
     * @throws Exception
     */

    public void testRangeSubsetSingle() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("coverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "146,-45,147,-42");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");

        // extract all bands. We had two bugs here, one related to the case sensitiveness
        // and the other about the inability to extract bands at all (with exception of the red one)
        String[] bands = new String[] { "1", "2", "3" };
        String[] band_names = new String[] { "red_band", "green_band", "blue_band" };
        for (int i = 0; i < bands.length; i++) {
            raw.put("Band", bands[i]);
            GridCoverage[] coverages = executeGetCoverageKvp(raw);
            assertEquals(1, coverages[0].getNumSampleDimensions());
            final String coverageBand = coverages[0].getSampleDimension(0).getDescription()
                    .toString();
            assertEquals(band_names[i], coverageBand.toLowerCase());
        }
    }

    public void testRangeSubsetMulti() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("coverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "146,-45,147,-42");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");

        raw.put("band", "1,3");
        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        assertEquals(2, coverages[0].getNumSampleDimensions());
        assertEquals("RED_BAND", coverages[0].getSampleDimension(0).getDescription().toString());
        assertEquals("BLUE_BAND", coverages[0].getSampleDimension(1).getDescription().toString());
    }

    public void testRangeSubsetSwap() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("coverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "146,-45,147,-42");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");

        raw.put("band", "3,2");
        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        assertEquals(2, coverages[0].getNumSampleDimensions());
        assertEquals("BLUE_BAND", coverages[0].getSampleDimension(0).getDescription().toString());
        assertEquals("GREEN_BAND", coverages[0].getSampleDimension(1).getDescription().toString());
    }

    public void testRangeSubsetOnlyField() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(TASMANIA_BM);
        raw.put("sourcecoverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "146,-45,147,-42");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");

        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        assertEquals(3, coverages[0].getNumSampleDimensions());
    }
    
//    public void testRangeSubsetOnlyFieldRes() throws Exception {
//        Map<String, Object> raw = new HashMap<String, Object>();
//        final String getLayerId = getLayerId(TASMANIA_BM);
//        raw.put("sourcecoverage", getLayerId);
//        raw.put("version", "1.0.0");
//        raw.put("format", "image/geotiff");
//        raw.put("BBox", "146,-45,147,-42");
//        raw.put("crs", "EPSG:4326");
//        raw.put("resx", "0.2");
//        raw.put("resy", "0.2");
//
//        GridCoverage[] coverages = executeGetCoverageKvp(raw);
//        assertEquals(3, coverages[0].getNumSampleDimensions());
//        
//        AffineTransform2D tx = (AffineTransform2D) coverages[0].getGridGeometry().getGridToCRS();
//        assertEquals(0.2, tx.getScaleX());
//        assertEquals(-0.2, tx.getScaleY());        
//    }

    /**
     * Runs GetCoverage on the specified parameters and returns an array of coverages
     */
    GridCoverage[] executeGetCoverageKvp(Map<String, Object> raw) throws Exception {
        final GetCoverageType getCoverage = (GetCoverageType) kvpreader.read(kvpreader.createRequest(),parseKvp(raw), raw);
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

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

}
