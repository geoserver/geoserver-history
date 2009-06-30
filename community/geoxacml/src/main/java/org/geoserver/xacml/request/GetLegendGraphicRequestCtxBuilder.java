/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.request;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.requests.GetLegendGraphicRequest;
import org.vfny.geoserver.wms.servlets.GetLegendGraphic;

import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.RequestCtx;

/**
 * GeoXACML request context builder for {@link GetLegendGraphic}
 * 
 * @author Christian Mueller
 *
 */
public class GetLegendGraphicRequestCtxBuilder extends WMSRequestCtxBuilder {

    @Override
    protected void fillRequestCtx(RequestCtx ctx, Request source) {
        GetLegendGraphicRequest  legendGraphic = (GetLegendGraphicRequest) source;
        super.fillRequestCtx(ctx, legendGraphic);
        
        ctx.getResource().add(new StringAttribute(legendGraphic.getLayer().getName().getLocalPart()));         ;                        
    }    
    
}
