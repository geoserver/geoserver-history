/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.request;

import org.geoserver.wms.MapLayerInfo;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.requests.GetFeatureInfoRequest;

import com.sun.xacml.ctx.RequestCtx;

/**
 * GeoXACML request context builder for {@link GetFeatureInfoRequest}
 * 
 * @author christian
 *
 */
public class GetFeatureInfoRequestCtxBuilder extends WMSRequestCtxBuilder {

    @Override
    protected void fillRequestCtx(RequestCtx ctx, Request source) {
        GetFeatureInfoRequest fetureInfo = (GetFeatureInfoRequest) source;
        super.fillRequestCtx(ctx, fetureInfo);
        
        MapLayerInfo[]layerInfo = fetureInfo.getQueryLayers();
        super.fillRequestCtxLayerInfo(ctx, layerInfo);                
    }    
    
}
