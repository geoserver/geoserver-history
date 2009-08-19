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
import java.util.List;

import org.acegisecurity.Authentication;
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

    public <T extends UserDetails> void transformUserDetails(T details) {
        for (int i = 0; i < details.getAuthorities().length; i++) {
            details.getAuthorities()[i] = new XACMLRole(details.getAuthorities()[i].getAuthority());
        }
    }


    public void prepareRoles(Authentication auth) {

        List<RequestCtx> requests = new ArrayList<RequestCtx>(auth.getAuthorities().length);

        for (GrantedAuthority ga : auth.getAuthorities()) {
            requests.add(GeoXACMLConfig.getRequestCtxBuilderFactory()
                    .getXACMLRoleRequestCtxBuilder((XACMLRole) ga).createRequestCtx());
        }

        List<ResponseCtx> responses = GeoXACMLConfig.getXACMLTransport().evaluateRequestCtxList(
                requests);

        outer: for (int i = 0; i < responses.size(); i++) {
            ResponseCtx response = responses.get(i);
            XACMLRole role = (XACMLRole) auth.getAuthorities()[i];
            for (Result result : response.getResults()) {
                if (result.getDecision() != Result.DECISION_PERMIT) {
                    role.setEnabled(false);
                    continue outer;
                }
                role.setEnabled(true);
                setUserProperties(auth, result, role);
            }

        }
    }

    private void setUserProperties(Authentication auth, Result result, XACMLRole role) {

        if (role.isRoleAttributesProcessed())
            return; // already done

        if (auth.getPrincipal() == null || auth.getPrincipal() instanceof String) {
            role.setRoleAttributesProcessed(true);
            return;
        }

        BeanInfo bi = null;
        try {
            bi = Introspector.getBeanInfo(auth.getPrincipal().getClass());
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        for (Obligation obligation : result.getObligations()) {
            if (XACMLConstants.UserPropertyObligationId.equals(obligation.getId().toString())) {
                for (Attribute attr : obligation.getAssignments()) {
                    String propertyName = ((StringAttribute) attr.getValue()).getValue();
                    for (PropertyDescriptor pd : bi.getPropertyDescriptors()) {
                        if (pd.getName().equals(propertyName)) {
                            Serializable value = null;
                            try {
                                Object tmp = pd.getReadMethod().invoke(auth.getPrincipal(),
                                        new Object[0]);
                                if (tmp == null)
                                    continue;
                                if (tmp instanceof Serializable == false) {
                                    throw new RuntimeException("Role params must be serializable, "
                                            + tmp.getClass() + " is not");
                                }
                                value = (Serializable) tmp;
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
        role.setRoleAttributesProcessed(true);
    }
}
