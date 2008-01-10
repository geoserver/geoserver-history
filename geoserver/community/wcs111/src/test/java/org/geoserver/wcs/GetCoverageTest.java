package org.geoserver.wcs;

import java.util.HashMap;
import java.util.Map;

import net.opengis.wcs.v1_1_1.GetCoverageType;

import org.geoserver.wcs.kvp.GetCoverageRequestReader;
import org.geoserver.wcs.test.WCSTestSupport;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.referencing.CRS;
import org.opengis.coverage.grid.GridCoverage;

public class GetCoverageTest extends WCSTestSupport {
    
    private GetCoverageRequestReader kvpreader;
    private WebCoverageService111 service;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        kvpreader = (GetCoverageRequestReader) applicationContext.getBean("wcs111GetCoverageRequestReader");
        service = (WebCoverageService111) applicationContext.getBean("wcs111ServiceTarget");
    }

    @Override
    protected String getDefaultLogConfiguration() {
        return "/DEFAULT_LOGGING.properties";
    }

    public void testKvpBasic() throws Exception {
        Map<String, Object> raw = new HashMap<String, Object>();
        final String layerId = layerId(WCSTestSupport.TASMANIA_BM);
        raw.put("identifier", layerId);
        raw.put("format", "GeoTiff");
        raw.put("BoundingBox", "-90,-180,90,180,urn:ogc:def:crs:EPSG:6.6:4326");
        raw.put("store", "false");
        raw.put("GridBaseCRS", "urn:ogc:def:crs:EPSG:6.6:4326");

        GetCoverageType getCoverage = (GetCoverageType) kvpreader.read(kvpreader.createRequest(),
                parseKvp(raw), raw);
        GridCoverage[] coverages = service.getCoverage(getCoverage);
        assertEquals(1, coverages.length);
        GridCoverage2D coverage = (GridCoverage2D) coverages[0];
        assertEquals(CRS.decode("urn:ogc:def:crs:EPSG:6.6:4326"), coverage.getEnvelope().getCoordinateReferenceSystem());
    }

    
}
