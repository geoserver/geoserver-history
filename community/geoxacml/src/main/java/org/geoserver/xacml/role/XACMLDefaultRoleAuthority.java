/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.xacml.role;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
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
                        
        return filterEnabledRoles(result,auth);
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
    
    Set<XACMLRole> filterEnabledRoles(Set<XACMLRole> roles,Authentication auth) {
        List<RequestCtx> requests = new ArrayList<RequestCtx>(roles.size());
        List<XACMLRole> roleList = new ArrayList<XACMLRole>(roles.size());
        
        for (XACMLRole role : roles) {
            XACMLRoleRequestCtxBuilder b = new XACMLRoleRequestCtxBuilder(AccessMode.READ,role);
            //System.out.println(XACMLUtil.asXMLString(b.createRequestCtx()));
            requests.add(b.createRequestCtx());
            roleList.add(role);
        }
        
        List<ResponseCtx> responses =  GeoXACMLConfig.getXACMLTransport().evaluateRequestCtxList(requests);
        Set<XACMLRole> resultSet = new HashSet<XACMLRole>();
        for (int i = 0; i < responses.size();i++) {
            ResponseCtx response = responses.get(i);
            for (Result result: response.getResults()){
                if (result.getDecision()!=Result.DECISION_PERMIT) continue;
                XACMLRole role = roleList.get(i);
                for (Obligation obligation: result.getObligations()){
                    if (XACMLConstants.UserPropertyObligationId.equals(obligation.getId().toString())) {
                        setUserProperties(auth, obligation.getAssignments(),role);
                    }
                }
                resultSet.add(role);
            }
                
        }        
        return resultSet;
    }
    
    private void setUserProperties(Authentication auth, List<Attribute> assignments,XACMLRole role) {
        Object  userDetails = auth.getPrincipal();
        if (userDetails==null) return;
        if (userDetails instanceof String) return; // this is only the username
        BeanInfo bi = null;
        try {
            bi=Introspector.getBeanInfo(userDetails.getClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        for (Attribute attr: assignments) {
            String propertyName=((StringAttribute)attr.getValue()).getValue();
            for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                if (pd.getName().equals(propertyName)) {                    
                 Object value=null;
                try {
                    value = pd.getReadMethod().invoke(userDetails,new Object[0]);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } 
                // special code for geometries
                if (value instanceof Geometry) {
                    GeometryRoleParam  temp = new GeometryRoleParam(); 
                    temp.setGeometry((Geometry)value);
                    temp.setSrsName(attr.getId().toString());
                    value=temp;
                }
                
                role.getAttributes().put(propertyName, value);
                }
            }
        }
    }
}
