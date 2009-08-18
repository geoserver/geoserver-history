/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.xacml.role;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.geoserver.xacml.geoxacml.GeoXACMLConfig;
import org.geoserver.xacml.geoxacml.XACMLConstants;

import com.sun.xacml.Obligation;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Result;
import com.vividsolutions.jts.geom.Geometry;

/**
 * Acegi implementation for {@link XACMLRoleAuthority}
 * 
 * 
 * @author Christian Mueller
 * 
 */
public class XACMLDefaultRoleAuthority implements XACMLRoleAuthority {

    
    public XACMLRole[] getXACMLRolesFor (UserDetails details, GrantedAuthority[] authorities) {

        Set<XACMLRole> candidates = new HashSet<XACMLRole>();

        if (authorities == null)
            candidates.add(new XACMLRole(XACMLConstants.AnonymousRole));
        else if (authorities.length == 0)
            candidates.add(new XACMLRole(XACMLConstants.AnonymousRole));
        else {
            for (GrantedAuthority gAut : authorities) {
                candidates.add(new XACMLRole(gAut.getAuthority()));
            }
        }

        Set<XACMLRole> filteredCandidates = filterEnabledRoles(candidates, details);
        return filteredCandidates.toArray(new XACMLRole[filteredCandidates.size()]);
    }


    Set<XACMLRole> filterEnabledRoles(Set<XACMLRole> roles, UserDetails details) {
        List<RequestCtx> requests = new ArrayList<RequestCtx>(roles.size());
        List<XACMLRole> roleList = new ArrayList<XACMLRole>(roles.size());

        for (XACMLRole role : roles) {
            RequestCtx requestCtx = GeoXACMLConfig.getRequestCtxBuilderFactory()
                    .getXACMLRoleRequestCtxBuilder(role).createRequestCtx();
            // System.out.println(XACMLUtil.asXMLString(b.createRequestCtx()));
            requests.add(requestCtx);
            roleList.add(role);
        }

        List<ResponseCtx> responses = GeoXACMLConfig.getXACMLTransport().evaluateRequestCtxList(
                requests);
        Set<XACMLRole> resultSet = new HashSet<XACMLRole>();
        for (int i = 0; i < responses.size(); i++) {
            ResponseCtx response = responses.get(i);
            for (Result result : response.getResults()) {
                if (result.getDecision() != Result.DECISION_PERMIT)
                    continue;
                XACMLRole role = roleList.get(i);
                for (Obligation obligation : result.getObligations()) {
                    if (XACMLConstants.UserPropertyObligationId.equals(obligation.getId()
                            .toString())) {
                        setUserProperties(details, obligation.getAssignments(), role);
                    }
                }
                resultSet.add(role);
            }

        }
        return resultSet;
    }

    private void setUserProperties(UserDetails userDetails, List<Attribute> assignments, XACMLRole role) {        
        if (userDetails == null)
            return;
        
        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(userDetails.getClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        for (Attribute attr : assignments) {
            String propertyName = ((StringAttribute) attr.getValue()).getValue();
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(propertyName)) {
                    Serializable value = null;
                    try {
                        Object tmp = pd.getReadMethod().invoke(userDetails, new Object[0]);
                        if (tmp==null) continue;
                        if (tmp instanceof Serializable == false) {
                            throw new RuntimeException("Role params must be serializable, "+tmp.getClass()+ " is not");
                        }
                        value=(Serializable)tmp;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    // special code for geometries
                    if (value instanceof Geometry) {
                        GeometryRoleParam temp = new GeometryRoleParam();
                        temp.setGeometry((Geometry) value);
                        temp.setSrsName(attr.getId().toString());
                        value = temp;
                    }

                    role.getAttributes().put(propertyName, value);
                }
            }
        }
    }
}
