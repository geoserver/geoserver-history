/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.security;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.acegisecurity.Authentication;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.security.AccessMode;
import org.geoserver.security.DataAccessManager;
import org.geoserver.xacml.geoxacml.GeoXACMLConfig;
import org.geoserver.xacml.geoxacml.XACMLConstants;
import org.geoserver.xacml.geoxacml.XACMLUtil;
import org.geoserver.xacml.request.CatalogRequestCtxBuilder;
import org.geoserver.xacml.request.RequestCtxBuilder;

import com.sun.xacml.Obligation;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;

public class XACMLDataAccessManager implements DataAccessManager {
    
    private CatalogMode mode;
    private Object modeLock = new Object();    
    private  Logger Log;
    
    private static Map<String,CatalogMode> CatalogModeMap;
    static {
        CatalogModeMap=new HashMap<String,CatalogMode>(3);
        CatalogModeMap.put("HIDE",CatalogMode.HIDE);
        CatalogModeMap.put("CHALLENGE",CatalogMode.CHALLENGE);
        CatalogModeMap.put("MIXED",CatalogMode.MIXED);
    }
    
    
    
    
    public XACMLDataAccessManager() {
        if (Log==null)
            Log = Logger.getLogger(this.getClass().getName());        
    }
    
    

    public boolean canAccess(Authentication user, WorkspaceInfo workspace, AccessMode mode) {
        return false;
    }

    public boolean canAccess(Authentication user, LayerInfo layer, AccessMode mode) {
        return false;
    }

    public boolean canAccess(Authentication user, ResourceInfo resource, AccessMode mode) {
        return false;
    }

    public CatalogMode getMode() {
        synchronized (modeLock) {
            if (mode!=null) return mode;
            RequestCtxBuilder builder = new CatalogRequestCtxBuilder(CatalogRequestCtxBuilder.GeoServerRole);
            RequestCtx requestCtx = builder.createRequestCtx();
            ResponseCtx responseCtx = GeoXACMLConfig.getPDP().evaluate(requestCtx);
            
            Result result = responseCtx.getResults().iterator().next();
            if (result==null || result.getDecision()!=Result.DECISION_PERMIT) {
                Log.severe("Geserver cannot access its catalog !!!");
                Log.severe(XACMLUtil.asXMLString(requestCtx));
            }
            
            Obligation obligation = result.getObligations().iterator().next();
            if (obligation == null || XACMLConstants.CatalogModeObligationId.equals(obligation.getId().toASCIIString())==false) {
                Log.severe("No obligation with id: "+ XACMLConstants.CatalogModeObligationId);
                Log.severe(XACMLUtil.asXMLString(requestCtx));                
            }
                       
            Attribute catalogModeAttribute = obligation.getAssignments().iterator().next();
            if (catalogModeAttribute==null || CatalogModeMap.containsKey(catalogModeAttribute.getValue().toString())==false) {
                Log.severe("No valid catalog mode ");
                Log.severe(XACMLUtil.asXMLString(requestCtx));                
            }
            
            String catalogModeKey=  catalogModeAttribute.getValue().toString();
            mode = CatalogModeMap.get(catalogModeKey);
            return mode; 
        }
        
        
    }

}
