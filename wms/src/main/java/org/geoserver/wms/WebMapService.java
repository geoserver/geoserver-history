/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.sld.GetStylesRequest;
import org.geoserver.wms.response.DescribeLayerTransformer;
import org.geoserver.wms.response.GetCapabilitiesTransformer;
import org.geoserver.wms.response.LegendGraphic;
import org.geotools.styling.StyledLayerDescriptor;

/**
 * Web Map Service implementation.
 * <p>
 * Each of the methods on this class corresponds to an operation as defined by the Web Map
 * Specification. See {@link http://www.opengeospatial.org/standards/wms} for more details.
 * </p>
 * 
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
    Map getMap(GetMapRequest request);

    Map map(GetMapRequest request);

    /**
     * DescribeLayer operation.
     */
    DescribeLayerTransformer describeLayer(DescribeLayerRequest request);

    /**
     * GetFeatureInfo operation.
     */
    FeatureCollectionType getFeatureInfo(GetFeatureInfoRequest request);

    /**
     * GetLegendGraphic operation.
     */
    LegendGraphic getLegendGraphic(GetLegendGraphicRequest request);

    /**
     * GetMap reflector
     */
    Map reflect(GetMapRequest request);

    Map getMapReflect(GetMapRequest request);

    StyledLayerDescriptor getStyles(GetStylesRequest request);
}
