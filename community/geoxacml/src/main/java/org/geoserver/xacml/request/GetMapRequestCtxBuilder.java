/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.request;

import org.geoserver.wms.MapLayerInfo;
import org.geotools.geometry.jts.JTS;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.requests.GetMapRequest;

import com.sun.xacml.ctx.RequestCtx;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * GeoXACML request context builder for {@link GetMapRequest}
 * 
 * @author christian
 *
 */
public class GetMapRequestCtxBuilder extends WMSRequestCtxBuilder {

    @Override
    protected void fillRequestCtx(RequestCtx ctx, Request source) {
        GetMapRequest getMapSource = (GetMapRequest) source;
        super.fillRequestCtx(ctx, getMapSource);
        
        MapLayerInfo[]layerInfo = getMapSource.getLayers();
        super.fillRequestCtxLayerInfo(ctx, layerInfo);
        
        Envelope env = getMapSource.getBbox();
        Geometry g =  JTS.toGeometry(env);
        super.fillRequestCtxGeometry(ctx, g, getMapSource.getSRS());
        
    }    
    
}
