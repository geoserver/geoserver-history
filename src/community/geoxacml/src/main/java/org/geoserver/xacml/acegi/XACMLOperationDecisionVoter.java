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
import org.geoserver.xacml.role.XACMLRole;
import org.geoserver.xacml.role.XACMLRoleAuthority;

import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;

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

        List<RequestCtx> requestCtxts = buildRequestCtxListFromRoles(auth, urlPath);
        if (requestCtxts.isEmpty())
            return XACMLDecisionMapper.Exact.getAcegiDecisionFor(Result.DECISION_DENY);

        List<ResponseCtx> responseCtxts = GeoXACMLConfig.getXACMLTransport()
                .evaluateRequestCtxList(requestCtxts);

        int xacmlDecision = XACMLUtil.getDecisionFromRoleResponses(responseCtxts);
        return XACMLDecisionMapper.Exact.getAcegiDecisionFor(xacmlDecision);

    }

    private List<RequestCtx> buildRequestCtxListFromRoles(Authentication auth, String urlPath) {

        List<RequestCtx> resultList = new ArrayList<RequestCtx>();
        XACMLRoleAuthority raa = GeoXACMLConfig.getXACMLRoleAuthority();

        for (XACMLRole role : raa.getRolesFor(auth)) {
            RequestCtx requestCtx = GeoXACMLConfig.getRequestCtxBuilderFactory()
                    .getURLMatchRequestCtxBuilder(role, urlPath, AccessMode.READ)
                    .createRequestCtx();
            XACMLUtil.getXACMLLogger().info(XACMLUtil.asXMLString(requestCtx));
            resultList.add(requestCtx);
        }

        return resultList;
    }

}
