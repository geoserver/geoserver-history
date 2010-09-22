/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import org.geoserver.wms.request.DescribeLayerRequest;
import org.geoserver.wms.request.GetCapabilitiesRequest;
import org.geoserver.wms.request.GetFeatureInfoRequest;
import org.geoserver.wms.request.GetLegendGraphicRequest;
import org.geoserver.wms.request.GetMapRequest;
import org.geoserver.wms.request.GetStylesRequest;
import org.geoserver.wms.response.DescribeLayerTransformer;
import org.geoserver.wms.response.GetCapabilitiesTransformer;
import org.geotools.styling.StyledLayerDescriptor;
import org.vfny.geoserver.wms.responses.GetFeatureInfoResponse;
import org.vfny.geoserver.wms.responses.GetLegendGraphicResponse;
import org.vfny.geoserver.wms.responses.GetMapResponse;


/**
 * Web Map Service implementation.
 * <p>
 * Each of the methods on this class corresponds to an operation as defined
 * by the Web Map Specification. See {@link http://www.opengeospatial.org/standards/wms}
 * for more details.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public interface WebMapService {
    
    /**
     * WMS service configuration.
     */
    WMSInfo getServiceInfo();
    
    /**
     * GetCapabilities operation.
     */
    GetCapabilitiesTransformer getCapabilities(GetCapabilitiesRequest request);

    GetCapabilitiesTransformer capabilities(GetCapabilitiesRequest request);

    /**
     * GetMap operation.
     */
    GetMapResponse getMap(GetMapRequest request);

    GetMapResponse map(GetMapRequest request);

    /**
     * DescribeLayer operation.
     */
    DescribeLayerTransformer describeLayer(DescribeLayerRequest request);

    /**
     * GetFeatureInfo operation.
     */
    GetFeatureInfoResponse getFeatureInfo(GetFeatureInfoRequest request);

    /**
     * GetLegendGraphic operation.
     */
    GetLegendGraphicResponse getLegendGraphic(GetLegendGraphicRequest request);

    /**
     * GetMap reflector
     */
    GetMapResponse reflect(GetMapRequest request);

    GetMapResponse getMapReflect(GetMapRequest request);

    StyledLayerDescriptor getStyles(GetStylesRequest request);
}
