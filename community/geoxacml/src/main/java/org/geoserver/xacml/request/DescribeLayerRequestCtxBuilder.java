/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.request;

import java.util.Iterator;

import org.geoserver.wms.MapLayerInfo;
import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.requests.DescribeLayerRequest;

import com.sun.xacml.ctx.RequestCtx;

/**
 * GeoXACML request context builder for {@link DescribeLayerRequest}
 * 
 * @author Christian Mueller
 *
 */
public class DescribeLayerRequestCtxBuilder extends WMSRequestCtxBuilder {

    @Override
    protected void fillRequestCtx(RequestCtx ctx, Request source) {
        DescribeLayerRequest  describeLayer = (DescribeLayerRequest) source;
        super.fillRequestCtx(ctx, describeLayer);
        
        MapLayerInfo[]layerInfo = new  MapLayerInfo[describeLayer.getLayers().size()]; 
        Iterator<?> it = describeLayer.getLayers().iterator();
        int index=0;
        while (it.hasNext()) {
        	layerInfo[index]=(MapLayerInfo) it.next();
        	index++;
        }       
        super.fillRequestCtxLayerInfo(ctx, layerInfo);                
    }    
    
}
