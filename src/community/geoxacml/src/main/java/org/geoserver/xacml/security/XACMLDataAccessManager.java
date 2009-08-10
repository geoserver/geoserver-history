/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.geoserver.xacml.request.LayerInfoRequestCtxBuilder;
import org.geoserver.xacml.request.RequestCtxBuilder;
import org.geoserver.xacml.request.ResourceInfoRequestCtxBuilder;
import org.geoserver.xacml.request.WorkspaceRequestCtxBuilder;
import org.geoserver.xacml.role.Role;
import org.geoserver.xacml.role.RoleAssignmentAuthority;

import com.sun.xacml.Obligation;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;

public class XACMLDataAccessManager implements DataAccessManager {

    private CatalogMode mode;

    private Object modeLock = new Object();

    private Logger Log;

    private static Map<String, CatalogMode> CatalogModeMap;
    static {
        CatalogModeMap = new HashMap<String, CatalogMode>(3);
        CatalogModeMap.put("HIDE", CatalogMode.HIDE);
        CatalogModeMap.put("CHALLENGE", CatalogMode.CHALLENGE);
        CatalogModeMap.put("MIXED", CatalogMode.MIXED);
    }

    public XACMLDataAccessManager() {
        if (Log == null)
            Log = Logger.getLogger(this.getClass().getName());
    }

    public boolean canAccess(Authentication user, WorkspaceInfo workspace, AccessMode mode) {

        List<RequestCtx> requestCtxts = buildWorkspaceRequestCtxList(user, workspace, mode);
        List<ResponseCtx> responseCtxts = GeoXACMLConfig.evaluateRequestCtxList(requestCtxts);

        int xacmlDecision = XACMLUtil.getDecisionFromResponseCtxList(responseCtxts);

        if (xacmlDecision == Result.DECISION_PERMIT)
            return true;
        return false;
    }

    public boolean canAccess(Authentication user, LayerInfo layer, AccessMode mode) {
        List<RequestCtx> requestCtxts = buildLayerInfoRequestCtxList(user, layer, mode);
        List<ResponseCtx> responseCtxts = GeoXACMLConfig.evaluateRequestCtxList(requestCtxts);

        int xacmlDecision = XACMLUtil.getDecisionFromResponseCtxList(responseCtxts);

        if (xacmlDecision == Result.DECISION_PERMIT)
            return true;
        return false;
    }

    public boolean canAccess(Authentication user, ResourceInfo resource, AccessMode mode) {
        List<RequestCtx> requestCtxts = buildResourceInfoRequestCtxList(user, resource, mode);
        List<ResponseCtx> responseCtxts = GeoXACMLConfig.evaluateRequestCtxList(requestCtxts);

        int xacmlDecision = XACMLUtil.getDecisionFromResponseCtxList(responseCtxts);

        if (xacmlDecision == Result.DECISION_PERMIT)
            return true;
        return false;
    }

    public CatalogMode getMode() {
        synchronized (modeLock) {
            if (mode != null)
                return mode;
            RequestCtxBuilder builder = new CatalogRequestCtxBuilder(
                    CatalogRequestCtxBuilder.GeoServerRole, AccessMode.READ);
            RequestCtx requestCtx = builder.createRequestCtx();
            ResponseCtx responseCtx = GeoXACMLConfig.getPDP().evaluate(requestCtx);

            Result result = responseCtx.getResults().iterator().next();
            if (result == null || result.getDecision() != Result.DECISION_PERMIT) {
                Log.severe("Geserver cannot access its catalog !!!");
                Log.severe(XACMLUtil.asXMLString(requestCtx));
                return null;
            }

            Obligation obligation = result.getObligations().iterator().next();
            if (obligation == null
                    || XACMLConstants.CatalogModeObligationId.equals(obligation.getId()
                            .toASCIIString()) == false) {
                Log.severe("No obligation with id: " + XACMLConstants.CatalogModeObligationId);
                Log.severe(XACMLUtil.asXMLString(requestCtx));
                return null;
            }

            Attribute catalogModeAssignment = obligation.getAssignments().iterator().next();
            if (catalogModeAssignment == null
                    || CatalogModeMap.containsKey(((StringAttribute) catalogModeAssignment
                            .getValue()).getValue()) == false) {
                Log.severe("No valid catalog mode ");
                Log.severe(XACMLUtil.asXMLString(requestCtx));
                return null;
            }

            String catalogModeKey = ((StringAttribute) catalogModeAssignment.getValue()).getValue();
            mode = CatalogModeMap.get(catalogModeKey);
            return mode;
        }

    }

    private List<RequestCtx> buildWorkspaceRequestCtxList(Authentication auth,
            WorkspaceInfo workspaceInfo, AccessMode mode) {

        List<RequestCtx> resultList = new ArrayList<RequestCtx>(auth.getAuthorities().length);
        RoleAssignmentAuthority raa = GeoXACMLConfig.getRoleAssignmentAuthority();

        for (String roleId : raa.getRoleIdsFor(auth)) {
            WorkspaceRequestCtxBuilder builder = new WorkspaceRequestCtxBuilder(new Role(roleId),
                    workspaceInfo, mode);
            RequestCtx requestCtx = builder.createRequestCtx();
            XACMLUtil.getXACMLLogger().info(XACMLUtil.asXMLString(requestCtx));
            resultList.add(requestCtx);
        }

        return resultList;
    }

    private List<RequestCtx> buildLayerInfoRequestCtxList(Authentication auth, LayerInfo layerInfo,
            AccessMode mode) {

        List<RequestCtx> resultList = new ArrayList<RequestCtx>(auth.getAuthorities().length);
        RoleAssignmentAuthority raa = GeoXACMLConfig.getRoleAssignmentAuthority();

        for (String roleId : raa.getRoleIdsFor(auth)) {
            LayerInfoRequestCtxBuilder builder = new LayerInfoRequestCtxBuilder(new Role(roleId),
                    layerInfo, mode);
            RequestCtx requestCtx = builder.createRequestCtx();
            XACMLUtil.getXACMLLogger().info(XACMLUtil.asXMLString(requestCtx));
            resultList.add(requestCtx);
        }

        return resultList;
    }

    private List<RequestCtx> buildResourceInfoRequestCtxList(Authentication auth,
            ResourceInfo resourceInfo, AccessMode mode) {

        List<RequestCtx> resultList = new ArrayList<RequestCtx>(auth.getAuthorities().length);
        RoleAssignmentAuthority raa = GeoXACMLConfig.getRoleAssignmentAuthority();

        for (String roleId : raa.getRoleIdsFor(auth)) {
            ResourceInfoRequestCtxBuilder builder = new ResourceInfoRequestCtxBuilder(new Role(
                    roleId), resourceInfo, mode);
            RequestCtx requestCtx = builder.createRequestCtx();
            XACMLUtil.getXACMLLogger().info(XACMLUtil.asXMLString(requestCtx));
            resultList.add(requestCtx);
        }

        return resultList;
    }

}
