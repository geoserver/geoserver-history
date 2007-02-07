/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.wms.requests.DescribeLayerRequest;
import org.vfny.geoserver.wms.requests.GetFeatureInfoRequest;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.requests.WMSCapabilitiesRequest;
import org.vfny.geoserver.wms.responses.DescribeLayerResponse;
import org.vfny.geoserver.wms.responses.GetFeatureInfoResponse;
import org.vfny.geoserver.wms.responses.GetLegendGraphicResponse;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;
import org.vfny.geoserver.wms.servlets.Capabilities;
import org.vfny.geoserver.wms.servlets.DescribeLayer;
import org.vfny.geoserver.wms.servlets.GetFeatureInfo;
import org.vfny.geoserver.wms.servlets.GetLegendGraphic;
import org.vfny.geoserver.wms.servlets.GetMap;


public class DefaultWebMapService implements WebMapService, ApplicationContextAware {
    /**
     * Application context
     */
    ApplicationContext context;

    public void setApplicationContext(ApplicationContext context)
        throws BeansException {
        this.context = context;
    }

    public WMSCapabilitiesResponse getCapabilities(WMSCapabilitiesRequest request) {
        Capabilities capabilities = (Capabilities) context.getBeansOfType(Capabilities.class)
                                                          .values().iterator().next();

        return (WMSCapabilitiesResponse) capabilities.getResponse();
    }
    
    public WMSCapabilitiesResponse capabilities(WMSCapabilitiesRequest request) {
    	return getCapabilities( request );
    }

    public DescribeLayerResponse describeLayer(DescribeLayerRequest request) {
        DescribeLayer describeLayer = (DescribeLayer) context.getBeansOfType(DescribeLayer.class)
                                                             .values().iterator().next();

        return (DescribeLayerResponse) describeLayer.getResponse();
    }

    public GetMapResponse getMap(GetMapRequest request) {
        GetMap getMap = (GetMap) context.getBeansOfType(GetMap.class).values().iterator().next();

        return (GetMapResponse) getMap.getResponse();
    }

    public GetFeatureInfoResponse getFeatureInfo(GetFeatureInfoRequest request) {
        GetFeatureInfo getFeatureInfo = (GetFeatureInfo) context.getBeansOfType(GetFeatureInfo.class)
                                                                .values().iterator().next();

        return (GetFeatureInfoResponse) getFeatureInfo.getResponse();
    }

    public GetLegendGraphicResponse getLegendGraphic(GetLegendGraphicRequest request) {
        GetLegendGraphic getLegendGraphic = (GetLegendGraphic) context.getBeansOfType(GetLegendGraphic.class)
                                                                      .values().iterator().next();

        return (GetLegendGraphicResponse) getLegendGraphic.getResponse();
    }
}
