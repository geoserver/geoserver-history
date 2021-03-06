/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import net.opengis.wfs.FeatureCollectionType;

import org.geoserver.ows.Response;
import org.geoserver.sld.GetStylesRequest;
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
    Object getCapabilities(GetCapabilitiesRequest request);

    Object capabilities(GetCapabilitiesRequest request);

    /**
     * GetMap operation.
     */
    WebMap getMap(GetMapRequest request);

    WebMap map(GetMapRequest request);

    /**
     * DescribeLayer operation.
     */
    Object describeLayer(DescribeLayerRequest request);

    /**
     * GetFeatureInfo operation.
     */
    FeatureCollectionType getFeatureInfo(GetFeatureInfoRequest request);

    /**
     * GetLegendGraphic operation.
     * 
     * @return the representation of the legend graphic to be encoded by a {@link Response} object
     *         that can handle it
     */
    Object getLegendGraphic(GetLegendGraphicRequest request);

    /**
     * GetMap reflector
     */
    WebMap reflect(GetMapRequest request);

    WebMap getMapReflect(GetMapRequest request);
    
    /**
     * KML reflector
     */
    WebMap kml(GetMapRequest getMap);

    StyledLayerDescriptor getStyles(GetStylesRequest request);
}
