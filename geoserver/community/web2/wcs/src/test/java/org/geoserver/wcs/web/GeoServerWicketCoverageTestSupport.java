package org.geoserver.wcs.web;

import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.geoserver.wcs.test.CoverageTestSupport;

import javax.xml.namespace.QName;

public abstract class GeoServerWicketCoverageTestSupport extends GeoServerWicketTestSupport {
    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        dataDirectory.addCoverage(
                CoverageTestSupport.TASMANIA_DEM, 
                CoverageTestSupport.class.getResource("tazdem.tiff"),
                CoverageTestSupport.TIFF,
                null);
        dataDirectory.addCoverage(
                CoverageTestSupport.TASMANIA_BM, 
                CoverageTestSupport.class.getResource("tazbm.tiff"),
                CoverageTestSupport.TIFF,
                null);
        dataDirectory.addCoverage(
                CoverageTestSupport.ROTATED_CAD,
                CoverageTestSupport.class.getResource("rotated.tiff"),
                CoverageTestSupport.TIFF,
                null);
        dataDirectory.addCoverage(
                CoverageTestSupport.WORLD,
                CoverageTestSupport.class.getResource("world.tiff"),
                CoverageTestSupport.TIFF,
                null);
    }
}
