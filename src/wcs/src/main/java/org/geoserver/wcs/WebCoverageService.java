/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs;

import org.vfny.geoserver.wcs.requests.CoverageRequest;
import org.vfny.geoserver.wcs.requests.DescribeRequest;
import org.vfny.geoserver.wcs.requests.WCSCapabilitiesRequest;
import org.vfny.geoserver.wcs.responses.CoverageResponse;
import org.vfny.geoserver.wcs.responses.DescribeResponse;
import org.vfny.geoserver.wcs.responses.WCSCapabilitiesResponse;


/**
 * Web Map Service implementation.
 * <p>
 * Each of the methods on this class corresponds to an operation as defined
 * by the Web Coverage Specification. See {@link http://www.opengeospatial.org/standards/wcs}
 * for more details.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public interface WebCoverageService {
    
    /**
     * WCS Service configuration.
     */
    WCSInfo getServiceInfo();
    
    /**
    * GetCapabilities operation.
    */
    WCSCapabilitiesResponse getCapabilities(WCSCapabilitiesRequest request);

    /**
     * DescribeCoverage oeration.
     */
    DescribeResponse describeCoverage(DescribeRequest request);

    /**
     * GetCoverage operation.
     */
    CoverageResponse getCoverage(CoverageRequest request);
}
