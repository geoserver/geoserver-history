/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.xacml.acegi;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.ConfigAttribute;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.vote.AccessDecisionVoter;
import org.geoserver.ows.Dispatcher;
import org.geoserver.ows.Request;
import org.geoserver.security.AccessMode;
import org.geoserver.xacml.geoxacml.GeoXACMLConfig;
import org.geoserver.xacml.geoxacml.XACMLUtil;
import org.geoserver.xacml.request.URLMatchRequestCtxBuilder;
import org.geoserver.xacml.role.Role;
import org.geoserver.xacml.role.RoleAssignmentAuthority;

import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;

/**
 * Acegi Decision Voter using XACML policies
 * 
 * @author Christian Mueller
 * 
 */
public class XACMLOperationDecisionVoter implements AccessDecisionVoter {

    public boolean supports(ConfigAttribute attr) {
        return true;
    }

    public boolean supports(Class aClass) {
        return true;
    }

    public int vote(Authentication auth, Object request, ConfigAttributeDefinition arg2) {

        String urlPath = null;
        if (request instanceof HttpServletRequest) {
            urlPath = ((HttpServletRequest) request).getPathInfo();
        } else {
            Request owsRequest = Dispatcher.REQUEST.get();
            urlPath = owsRequest.getHttpRequest().getPathInfo();
        }

        List<RequestCtx> requestCtxts = buildRequestCtxList(auth, urlPath);
        List<ResponseCtx> responseCtxts = GeoXACMLConfig.getXACMLTransport().evaluateRequestCtxList(requestCtxts);

        int xacmlDecision = XACMLUtil.getDecisionFromResponseCtxList(responseCtxts);
        return XACMLDecisionMapper.Exact.getAcegiDecisionFor(xacmlDecision);

    }

    private List<RequestCtx> buildRequestCtxList(Authentication auth, String urlPath) {

        List<RequestCtx> resultList = new ArrayList<RequestCtx>();
        RoleAssignmentAuthority raa = GeoXACMLConfig.getRoleAssignmentAuthority();

        for (String roleId : raa.getRoleIdsFor(auth)) {
            URLMatchRequestCtxBuilder builder = new URLMatchRequestCtxBuilder(new Role(roleId),
                    urlPath,AccessMode.READ);
            RequestCtx requestCtx = builder.createRequestCtx();
            XACMLUtil.getXACMLLogger().info(XACMLUtil.asXMLString(requestCtx));
            resultList.add(requestCtx);
        }

        return resultList;
    }

}
