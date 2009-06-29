/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

import org.geoserver.wms.MapLayerInfo;
import org.vfny.geoserver.Request;

import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.vividsolutions.jts.geom.Geometry;


public abstract class RequestCtxBuilder extends Object {
    
    protected final static String URN_PREFIX="org:geoserver:";
    protected enum Action {
        read { public String toString() { return "read"; }},
        write { public String toString() { return "write"; }}
    };
    protected final static String READ_ACTION="read";
    protected final static String WRITE_ACTION="wirte";
    
    protected static URI SERVICE_ATTR_ID = null;
    protected static URI REQUEST_ATTR_ID = null;
    protected static URI ACTION_ATTR_ID = null;
    protected static URI LAYER_ATTR_ID = null;
    static {
        try {
            SERVICE_ATTR_ID = new URI(URN_PREFIX+"service");
            REQUEST_ATTR_ID = new URI(URN_PREFIX+"request");
            ACTION_ATTR_ID = new URI(URN_PREFIX+"action");
            ACTION_ATTR_ID = new URI(URN_PREFIX+"layer");
        } catch (URISyntaxException e) {
            // should not happen
        }
        
    }
    
    protected void fillRequestCtxLayerInfo(RequestCtx ctx, MapLayerInfo[] layerInfo) {
        Set<Attribute> resourceAttributes = ctx.getResource();
        // TODO, Multivalued attributes
        
    }
    protected void fillRequestCtxGeometry(RequestCtx ctx, Geometry g, String srsName) {
        // TDOO
    }
    
    protected void fillRequestCtxAction(RequestCtx ctx, Action action ) {
        ctx.getAction().add(new Attribute(ACTION_ATTR_ID,null,null,
                new StringAttribute(action.toString())));
        
    }
    protected void fillRequestCtx(RequestCtx ctx, Request source) {
            Set<Attribute> resourceAttributes = ctx.getResource();
            
            resourceAttributes.add(new Attribute(SERVICE_ATTR_ID,null,null,
                            new StringAttribute(source.getService())));
            resourceAttributes.add(new Attribute(REQUEST_ATTR_ID,null,null,
                    new StringAttribute(source.getRequest())));
            
    }
        
}
