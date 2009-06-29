/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.request;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.requests.WMSRequest;

import com.sun.xacml.ctx.RequestCtx;

public class WMSRequestCtxBuilder extends RequestCtxBuilder {

    @Override
    protected void fillRequestCtx(RequestCtx ctx, Request source) {
        WMSRequest wmsSource = (WMSRequest) source; 
        super.fillRequestCtx(ctx, wmsSource);
        fillRequestCtxAction(ctx, Action.read);
    }

}
