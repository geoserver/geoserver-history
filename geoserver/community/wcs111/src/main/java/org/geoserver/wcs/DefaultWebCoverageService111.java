package org.geoserver.wcs;

import java.util.ArrayList;
import java.util.List;

import net.opengis.wcs.v1_1_1.DescribeCoverageType;
import net.opengis.wcs.v1_1_1.GetCapabilitiesType;
import net.opengis.wcs.v1_1_1.GetCoverageType;

import org.geoserver.ows.util.CapabilitiesUtils;
import org.geoserver.wcs.response.DescribeCoverageTransformer;
import org.geoserver.wcs.response.WCSCapsTransformer;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.wcs.WcsException;
import org.vfny.geoserver.wcs.responses.CoverageResponse;

public class DefaultWebCoverageService111 implements WebCoverageService111 {
    
    private WCS wcs;
    private Data catalog;

    public DefaultWebCoverageService111(WCS wcs, Data catalog) {
        this.wcs = wcs;
        this.catalog = catalog;
    }


    public WCSCapsTransformer getCapabilities(GetCapabilitiesType request) {
        // do the version negotiation dance
        List<String> provided = new ArrayList<String>();
//        provided.add("1.0.0");
        provided.add("1.1.0");
        provided.add("1.1.1");
        List<String> accepted = null;
        if(request.getAcceptVersions() != null)
            accepted = request.getAcceptVersions().getVersion();
        String version = CapabilitiesUtils.getVersion(provided, accepted);
        
        // TODO: add support for 1.0.0 in here

        if ("1.1.0".equals(version) || "1.1.1".equals(version)) {
            return new WCSCapsTransformer(wcs, catalog);
        }

        throw new WcsException("Could not understand version:" + version);
    }

    public DescribeCoverageTransformer describeCoverage(DescribeCoverageType request) {
        final String version = request.getVersion();
        if ("1.1.0".equals(version) || "1.1.1".equals(version)) {
            return new DescribeCoverageTransformer(wcs, catalog);
        }

        throw new WcsException("Could not understand version:" + version);
    }

    public CoverageResponse getCoverage(GetCoverageType request) {
        return null;
    }


    

}
