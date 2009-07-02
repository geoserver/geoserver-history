/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */


package org.geoserver.xacml.request;

import java.util.Set;
import java.util.logging.Logger;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.requests.WMSRequest;

//import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;



/**
 * Factory for creating a geoxacml request context object 
 * from a web servcie request object
 * 
 * @author Christian Mueller
 *
 */
public class RequestCtxFactory {
    
	public static final RequestCtxFactory Singleton = new RequestCtxFactory();
	protected static final Logger LOGGER = Logger.getLogger(RequestCtxFactory.class.getName());
	
	protected GetMapRequestCtxBuilder getMapRequestBuilder = new GetMapRequestCtxBuilder();
	protected DescribeLayerRequestCtxBuilder describeLayersBuilder = new DescribeLayerRequestCtxBuilder();
	protected GetLegendGraphicRequestCtxBuilder getLegendBuilder = new GetLegendGraphicRequestCtxBuilder();
	protected GetFeatureInfoRequestCtxBuilder featureInfoBuilder = new GetFeatureInfoRequestCtxBuilder();
	
	protected RequestCtxFactory() {		
	}
	
	/**
	 * set the users roles into the geoxacml request context
	 * 
	 * @param ctx
	 * @param roles
	 */
	protected void fillRequestCtxRoles(RequestCtx ctx, Set<String> roles) {
		// TODO
	}
	
	/**
	 * @param request
	 * @return the builder for the web service request or null for an unknown web request
	 * 
	 */
	protected  RequestCtxBuilder findBuilder(Request request) {
        if (WMSRequest.WMS_SERVICE_TYPE.equals(request.getService())) {
        	if (GetMapRequest.WMS_SERVICE_TYPE.equals(request.getService())) 
        		return getMapRequestBuilder;
        	// TODO, should be a java constant
        	if ("DescribeLayer".equals(request.getService())) 
        		return describeLayersBuilder;
        	// TODO, should be a java constant
        	if ("GetLegendGraphic".equals(request.getService())) 
        		return getLegendBuilder;
            //  TODO, should be a java constant
        	if ("GetFeatureInfo".equals(request.getService())) 
        		return featureInfoBuilder;        	        	        	            
        }            

         
        return null;
	}
	
    /**
     * create geoxacml request context object based on the web 
     * servic request and the users security roles
     * 
     * @param request
     * @param roles
     * @return a geoxaml request context or null for unknwon web request
     */
    public RequestCtx createFromRequest(Request request, Set<String> roles) {
    	
    	RequestCtxBuilder builder = findBuilder(request);
    	if (builder == null) {
    		LOGGER.severe("Could not find a GeoXACML request builder for" + request.getClass().getName());
    		return null;
    	}

//    	RequestCtx ctx = new RequestCtx(
//    			new HashSet<Attribute>(),new HashSet<Attribute>(),
//    			new HashSet<Attribute>(),new HashSet<Attribute>());
//    	
//    	fillRequestCtxRoles(ctx, roles);
//    	builder.fillRequestCtx(ctx, request);
//    	 
//        return ctx;
    	return null;
    }
    
}
