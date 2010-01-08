package org.geoserver.wcs;

import static org.geoserver.data.test.MockData.TASMANIA_BM;
import static org.vfny.geoserver.wcs.WcsException.WcsExceptionCode.InvalidParameterValue;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.textui.TestRunner;
import net.opengis.wcs10.GetCoverageType;

import org.geoserver.catalog.Catalog;
import org.geoserver.data.test.MockData;
import org.geoserver.wcs.kvp.Wcs10GetCoverageRequestReader;
import org.geoserver.wcs.test.WCSTestSupport;
import org.geoserver.wcs.xml.v1_0_0.WcsXmlReader;
import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.wcs.WCSConfiguration;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.vfny.geoserver.wcs.WcsException;
/**
 * Tests for GetCoverage operation on WCS.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class GetCoverageTest extends WCSTestSupport {

    private Wcs10GetCoverageRequestReader kvpreader;
    private WebCoverageService100 service;

    private WCSConfiguration configuration;

    private WcsXmlReader xmlReader;

    private Catalog catalog;

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new GetCoverageTest());
    }

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        kvpreader = (Wcs10GetCoverageRequestReader) applicationContext.getBean("wcs100GetCoverageRequestReader");
        service = (WebCoverageService100) applicationContext.getBean("wcs100ServiceTarget");
        configuration = new WCSConfiguration();
        catalog=(Catalog)applicationContext.getBean("catalog");
        xmlReader = new WcsXmlReader("GetCoverage", "1.0.0", configuration);
    }

    @Override
    protected String getLogConfiguration() {
        return "/DEFAULT_LOGGING.properties";
    }
    public void testOriginalGridWH() throws Exception {
    	// get base  coverage
        final GridCoverage baseCoverage = catalog.getCoverageByName(TASMANIA_BM.getLocalPart()).getGridCoverage(null, null);
        
        Map<String, Object> raw = new HashMap<String, Object>();
        final String layerID = getLayerId(TASMANIA_BM);
        raw.put("sourcecoverage", layerID);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "146.49999999999477,-44.49999999999785,147.99999999999474,-42.99999999999787");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "360");
        raw.put("height", "360");

        final GridCoverage[] coverages = executeGetCoverageKvp(raw);
        assertTrue(coverages.length==1);
        AffineTransform2D tx = (AffineTransform2D) coverages[0].getGridGeometry().getGridToCRS();
        AffineTransform2D expectedTx = (AffineTransform2D) baseCoverage.getGridGeometry().getGridToCRS();
        compareGrid2World(expectedTx, tx);
        ((GridCoverage2D)coverages[0]).dispose(true);
    }
    
    public void testDomainSubsetRxRy() throws Exception {
    	// get base  coverage
        final GridCoverage baseCoverage = catalog.getCoverageByName(TASMANIA_BM.getLocalPart()).getGridCoverage(null, null);
        final AffineTransform2D expectedTx = (AffineTransform2D) baseCoverage.getGridGeometry().getGridToCRS();        
        final GeneralEnvelope originalEnvelope = (GeneralEnvelope) baseCoverage.getEnvelope();
        final GeneralEnvelope newEnvelope=new GeneralEnvelope(originalEnvelope);
        newEnvelope.setEnvelope(
        		originalEnvelope.getMinimum(0),
        		originalEnvelope.getMaximum(1)-originalEnvelope.getSpan(1)/2,
        		originalEnvelope.getMinimum(0)+originalEnvelope.getSpan(0)/2,
        		originalEnvelope.getMaximum(1)
        		);
        
        final MathTransform cornerWorldToGrid = PixelTranslation.translate(expectedTx,PixelInCell.CELL_CENTER,PixelInCell.CELL_CORNER);
        final GeneralGridEnvelope expectedGridEnvelope = new GeneralGridEnvelope(CRS.transform(cornerWorldToGrid.inverse(), newEnvelope),PixelInCell.CELL_CORNER,true);
        final StringBuilder envelopeBuilder= new StringBuilder();
        envelopeBuilder.append(newEnvelope.getMinimum(0)).append(",");
        envelopeBuilder.append(newEnvelope.getMinimum(1)).append(",");
        envelopeBuilder.append(newEnvelope.getMaximum(0)).append(",");
        envelopeBuilder.append(newEnvelope.getMaximum(1));
        
        Map<String, Object> raw = new HashMap<String, Object>();
        final String layerID = getLayerId(TASMANIA_BM);
        raw.put("sourcecoverage", layerID);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", envelopeBuilder.toString());
        raw.put("crs", "EPSG:4326");
        raw.put("resx", Double.toString(expectedTx.getScaleX()));
        raw.put("resy", Double.toString(Math.abs(expectedTx.getScaleY())));

        final GridCoverage[] coverages = executeGetCoverageKvp(raw);
        final GridCoverage2D result=(GridCoverage2D) coverages[0];
        assertTrue(coverages.length==1);
        final AffineTransform2D tx = (AffineTransform2D) result.getGridGeometry().getGridToCRS();
        assertEquals("resx",expectedTx.getScaleX(),tx.getScaleX(),1E-6);
        assertEquals("resx",Math.abs(expectedTx.getScaleY()),Math.abs(tx.getScaleY()),1E-6);
        
        final GridEnvelope gridEnvelope = result.getGridGeometry().getGridRange();
        assertEquals("w",181,gridEnvelope.getSpan(0));
        assertEquals("h",181,gridEnvelope.getSpan(1));
        assertEquals("grid envelope",expectedGridEnvelope, gridEnvelope);
        
        // dispose
        ((GridCoverage2D)coverages[0]).dispose(true);
    }
    
    public void testOriginalGridRxRy() throws Exception {
    	// get base  coverage
        final GridCoverage baseCoverage = catalog.getCoverageByName(TASMANIA_BM.getLocalPart()).getGridCoverage(null, null);
        AffineTransform2D expectedTx = (AffineTransform2D) baseCoverage.getGridGeometry().getGridToCRS();
        
        Map<String, Object> raw = new HashMap<String, Object>();
        final String layerID = getLayerId(TASMANIA_BM);
        raw.put("sourcecoverage", layerID);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "146.49999999999477,-44.49999999999785,147.99999999999474,-42.99999999999787");
        raw.put("crs", "EPSG:4326");
        raw.put("resx", Double.toString(expectedTx.getScaleX()));
        raw.put("resy", Double.toString(Math.abs(expectedTx.getScaleY())));

        final GridCoverage[] coverages = executeGetCoverageKvp(raw);
        assertTrue(coverages.length==1);
        AffineTransform2D tx = (AffineTransform2D) coverages[0].getGridGeometry().getGridToCRS();
        compareGrid2World(expectedTx, tx);
        ((GridCoverage2D)coverages[0]).dispose(true);
    }

	/**
	 * Compare two grid to world transformations
	 * @param expectedTx
	 * @param tx
	 */
	private static void compareGrid2World(AffineTransform2D expectedTx,
			AffineTransform2D tx) {
		assertEquals("scalex",tx.getScaleX(), expectedTx.getScaleX(), 1E-6);
        assertEquals("scaley",tx.getScaleY(), expectedTx.getScaleY(), 1E-6);
        assertEquals("shearx",tx.getShearX(), expectedTx.getShearX(), 1E-6);
        assertEquals("sheary",tx.getShearY(), expectedTx.getShearY(), 1E-6);
        assertEquals("translatex",tx.getTranslateX(), expectedTx.getTranslateX(), 1E-6);
        assertEquals("translatey",tx.getTranslateY(), expectedTx.getTranslateY(), 1E-6);
	}


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
	    CoordinateReferenceSystem cvCRS = CRS.decode(CRS.lookupIdentifier(coverage.getEnvelope().getCoordinateReferenceSystem(), true), true);
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

	// ////////////////////////////////////////////////////////////////////
	//
	// ImageMosaic WCS-ND tests
	//
	// ////////////////////////////////////////////////////////////////////
	public void testUnacceptable3DBbox() throws Exception {
		if(!MockData.SpatioTemporalRasterTests)
			return;
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(MockData.WATTEMP);
        raw.put("sourcecoverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "0.5,40.5,14.856,44.496,0.0,50.0");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");
    
        try {
            @SuppressWarnings("unused")
                    GridCoverage[] coverages = executeGetCoverageKvp(raw);
            fail("When did we learn to encode SuperCoolFormat?");
        } catch (WcsException e) {
            assertEquals(InvalidParameterValue.toString(), e.getCode());
            assertEquals("bbox", e.getLocator());
        }
        }
	
	public void testUnacceptable3DParameters() throws Exception {
		if(!MockData.SpatioTemporalRasterTests)
			return;
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(MockData.WATTEMP);
        raw.put("sourcecoverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "0.5,40.5,14.856,44.496");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");
        raw.put("depth", "100.0");
    
        try {
            @SuppressWarnings("unused")
                    GridCoverage[] coverages = executeGetCoverageKvp(raw);
            fail("When did we learn to encode SuperCoolFormat?");
        } catch (WcsException e) {
            assertEquals(InvalidParameterValue.toString(), e.getCode());
            assertEquals("depth", e.getLocator());
        }
        
        raw.remove("width");
        raw.remove("height");
        raw.remove("depth");
        
        raw.put("resx", "0.1");
        raw.put("resy", "0.1");
        raw.put("resz", "1");
        
        try {
            @SuppressWarnings("unused")
                    GridCoverage[] coverages = executeGetCoverageKvp(raw);
            fail("When did we learn to encode SuperCoolFormat?");
        } catch (WcsException e) {
            assertEquals(InvalidParameterValue.toString(), e.getCode());
            assertEquals("resz", e.getLocator());
        }
    }
	
    public void testWrongTimeInstant() throws Exception {
		if(!MockData.SpatioTemporalRasterTests)
			return;
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(MockData.WATTEMP);
        raw.put("sourcecoverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "0.5,40.5,14.856,44.496");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");
        raw.put("TIME", "2000-00-00T00:00:000Z");
    
        // TODO: check the TIME parameter behavior; it looks like a wrong time instant returns back the current time
        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        
        assertNotNull(coverages);
        assertEquals(1, coverages.length);
        
//            try {
//                @SuppressWarnings("unused")
//                        GridCoverage[] coverages = executeGetCoverageKvp(raw);
//                fail("When did we learn to encode SuperCoolFormat?");
//            } catch (WcsException e) {
//                assertEquals(IllegalArgumentException.class, e.getCause().getClass());
//            }
        }
	
	public void testWrongElevationRangeSubset() throws Exception {
		if(!MockData.SpatioTemporalRasterTests)
			return;
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(MockData.WATTEMP);
        raw.put("sourcecoverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "0.5,40.5,14.856,44.496");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");
        raw.put("ELEVATION", "50.0");
    
        try {
            @SuppressWarnings("unused")
                    GridCoverage[] coverages = executeGetCoverageKvp(raw);
            fail("When did we learn to encode SuperCoolFormat?");
        } catch (WcsException e) {
            assertEquals(IllegalArgumentException.class, e.getCause().getClass());
        }
        }
	
	public void testTimeInstant() throws Exception {
		if(!MockData.SpatioTemporalRasterTests)
			return;
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(MockData.WATTEMP);
        raw.put("sourcecoverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "0.5,40.5,14.856,44.496");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");
        raw.put("TIME", "2008-10-31T00:00:000Z");
    
        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        
        assertNotNull(coverages);
        assertEquals(1, coverages.length);
        }
	
	public void testElevationRangeSubset() throws Exception {
		if(!MockData.SpatioTemporalRasterTests)
			return;
        Map<String, Object> raw = new HashMap<String, Object>();
        final String getLayerId = getLayerId(MockData.WATTEMP);
        raw.put("sourcecoverage", getLayerId);
        raw.put("version", "1.0.0");
        raw.put("format", "image/geotiff");
        raw.put("BBox", "0.5,40.5,14.856,44.496");
        raw.put("crs", "EPSG:4326");
        raw.put("width", "150");
        raw.put("height", "150");
        raw.put("ELEVATION", "0.0");
    
        GridCoverage[] coverages = executeGetCoverageKvp(raw);
        
        assertNotNull(coverages);
        assertEquals(1, coverages.length);
        }
	
	public static void main(String[] args) {
        TestRunner.run(suite());
    }

}