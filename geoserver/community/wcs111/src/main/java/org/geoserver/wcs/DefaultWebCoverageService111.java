package org.geoserver.wcs;

import net.opengis.wcs.v1_1_1.CapabilitiesType;
import net.opengis.wcs.v1_1_1.CoverageDescriptionsType;
import net.opengis.wcs.v1_1_1.DescribeCoverageType;
import net.opengis.wcs.v1_1_1.GetCapabilitiesType;
import net.opengis.wcs.v1_1_1.GetCoverageType;

import org.geotools.xml.transform.TransformerBase;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.wcs.responses.CoverageResponse;

public class DefaultWebCoverageService111 implements WebCoverageService111 {
    
    private WCS wcs;
    private Data catalog;

    public DefaultWebCoverageService111(WCS wcs, Data catalog) {
        this.wcs = wcs;
        this.catalog = catalog;
    }


    public TransformerBase getCapabilities(GetCapabilitiesType request) {
        return new WCSGetCapabilities(wcs, catalog).run(request);
    }

    public CoverageDescriptionsType describeCoverage(DescribeCoverageType request) {
        return null;
    }

    public CoverageResponse getCoverage(GetCoverageType request) {
        return null;
    }


    

}
