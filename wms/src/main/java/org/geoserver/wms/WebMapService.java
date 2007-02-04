/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.wms.requests.DescribeLayerRequest;
import org.vfny.geoserver.wms.requests.GetFeatureInfoRequest;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.DescribeLayerResponse;
import org.vfny.geoserver.wms.responses.GetFeatureInfoResponse;
import org.vfny.geoserver.wms.responses.GetLegendGraphicResponse;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;


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
     * GetCapabilities operation.
     */
    WMSCapabilitiesResponse getCapabilities(CapabilitiesRequest request);

    /**
     * GetMap operation.
     */
    GetMapResponse getMap(GetMapRequest request);

    /**
     * DescribeLayer operation.
     */
    DescribeLayerResponse describeLayer(DescribeLayerRequest request);

    /**
     * GetFeatureInfo operation.
     */
    GetFeatureInfoResponse getFeatureInfo(GetFeatureInfoRequest request);

    /**
     * GetLegendGraphic operation.
     */
    GetLegendGraphicResponse getLegendGraphic(GetLegendGraphicRequest request);
}
