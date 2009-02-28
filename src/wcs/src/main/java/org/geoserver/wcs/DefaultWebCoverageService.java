/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wcs;

import org.geoserver.config.GeoServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.wcs.requests.CoverageRequest;
import org.vfny.geoserver.wcs.requests.DescribeRequest;
import org.vfny.geoserver.wcs.requests.WCSCapabilitiesRequest;
import org.vfny.geoserver.wcs.responses.CoverageResponse;
import org.vfny.geoserver.wcs.responses.DescribeResponse;
import org.vfny.geoserver.wcs.responses.WCSCapabilitiesResponse;
import org.vfny.geoserver.wcs.servlets.Capabilities;
import org.vfny.geoserver.wcs.servlets.Coverage;
import org.vfny.geoserver.wcs.servlets.Describe;


public class DefaultWebCoverageService implements WebCoverageService, ApplicationContextAware {
    /**
     * service configuration
     */
    WCSInfo wcs;
    /**
     * Application context
     */
    ApplicationContext context;

    public DefaultWebCoverageService(GeoServer gs) {
        this.wcs = gs.getService(WCSInfo.class);
    }
    
    public WCSInfo getServiceInfo() {
        return wcs;
    }
    
    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        this.context = context;
    }

    public WCSCapabilitiesResponse getCapabilities(WCSCapabilitiesRequest request) {
        Capabilities getCapabilities = (Capabilities) context.getBean("wcsGetCapabilities");

        return (WCSCapabilitiesResponse) getCapabilities.getResponse();
    }

    public DescribeResponse describeCoverage(DescribeRequest request) {
        Describe describeCoverage = (Describe) context.getBean("wcsDescribeCoverage");

        return (DescribeResponse) describeCoverage.getResponse();
    }

    public CoverageResponse getCoverage(CoverageRequest request) {
        Coverage getCoverage = (Coverage) context.getBean("wcsGetCoverage");

        return (CoverageResponse) getCoverage.getResponse();
    }
}
