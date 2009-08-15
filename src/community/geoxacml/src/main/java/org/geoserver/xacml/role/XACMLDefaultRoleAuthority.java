/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.xacml.role;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.geoserver.security.AccessMode;
import org.geoserver.xacml.geoxacml.GeoXACMLConfig;
import org.geoserver.xacml.geoxacml.XACMLConstants;
import org.geoserver.xacml.geoxacml.XACMLUtil;
import org.geoserver.xacml.request.XACMLRoleRequestCtxBuilder;

import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;


/**
 * Acegi implementation for {@link XACMLRoleAuthority}
 * 
 *
 * @author Christian Mueller
 *
 */
public class XACMLDefaultRoleAuthority implements XACMLRoleAuthority {
    

    public Set<XACMLRole> getRolesFor(Authentication auth) {
        
        Set<XACMLRole> result = new HashSet<XACMLRole>();
        
        if (auth==null)
            result.add(new XACMLRole(XACMLConstants.AnonymousRole));
        else if (auth.getAuthorities()==null) 
            result.add(new XACMLRole(XACMLConstants.AnonymousRole));        
        else {
            for (GrantedAuthority gAut: auth.getAuthorities()) {
                result.add(new XACMLRole(gAut.getAuthority()));
            }         
        }
                        
        return filterEnabledRoles(result);
    }

    public boolean isCallerInRole(Authentication auth, String roleId) {
        
        if (auth==null && XACMLConstants.AnonymousRole.equals(roleId)) {
            return roleIsEnabled(new XACMLRole(XACMLConstants.AnonymousRole));
        }
        for (GrantedAuthority gAut: auth.getAuthorities()) {
            if (roleId.equals(gAut.getAuthority())) {
                if (roleIsEnabled(new XACMLRole(roleId))) return true;
            }
        }
        return false;
    }

    private boolean roleIsEnabled(XACMLRole role) {
        XACMLRoleRequestCtxBuilder b = new XACMLRoleRequestCtxBuilder(AccessMode.READ,role);
        ResponseCtx response =  GeoXACMLConfig.getXACMLTransport().evaluateRequestCtx(b.createRequestCtx());
        return response.getResults().iterator().next().getDecision()==Result.DECISION_PERMIT;
       
    }
    
    Set<XACMLRole> filterEnabledRoles(Set<XACMLRole> roles) {
        List<RequestCtx> requests = new ArrayList<RequestCtx>(roles.size());
        List<XACMLRole> roleList = new ArrayList<XACMLRole>(roles.size());
        
        for (XACMLRole role : roles) {
            XACMLRoleRequestCtxBuilder b = new XACMLRoleRequestCtxBuilder(AccessMode.READ,role);
            //System.out.println(XACMLUtil.asXMLString(b.createRequestCtx()));
            requests.add(b.createRequestCtx());
            roleList.add(role);
        }
        
        List<ResponseCtx> responses =  GeoXACMLConfig.getXACMLTransport().evaluateRequestCtxList(requests);
        Set<XACMLRole> result = new HashSet<XACMLRole>();
        for (int i = 0; i < responses.size();i++) {
            ResponseCtx response = responses.get(i);
            if (response.getResults().iterator().next().getDecision()==Result.DECISION_PERMIT)
                result.add(roleList.get(i));
        }        
        return result;
    }
}
