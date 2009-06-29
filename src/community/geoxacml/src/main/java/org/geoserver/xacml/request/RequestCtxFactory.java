/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.request;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.requests.WMSRequest;

import com.sun.xacml.ctx.RequestCtx;



public abstract class RequestCtxFactory {
    
    public RequestCtx createFromRequest(Request request) {
        if (WMSRequest.WMS_SERVICE_TYPE.equalsIgnoreCase(request.getService())) {
            
        }            
        return null;
    }
    
}
