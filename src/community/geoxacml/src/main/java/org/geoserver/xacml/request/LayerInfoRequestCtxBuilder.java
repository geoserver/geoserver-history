/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.request;

import java.util.HashSet;
import java.util.Set;

import org.geoserver.catalog.LayerInfo;
import org.geoserver.security.AccessMode;
import org.geoserver.xacml.geoxacml.XACMLConstants;
import org.geoserver.xacml.role.Role;

import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.Subject;

/**
 * Builds a request for layer info access control
 * 
 * 
 * @author Christian Mueller
 * 
 */
public class LayerInfoRequestCtxBuilder extends RequestCtxBuilder {
    private String layerName = null;

    public String getLayerName() {
        return layerName;
    }

    public LayerInfoRequestCtxBuilder(Role role, LayerInfo layerInfo,AccessMode mode) {
        super(role,mode);
        this.layerName = layerInfo.getName();
    }

    @Override
    public RequestCtx createRequestCtx() {

        Set<Subject> subjects = new HashSet<Subject>(1);
        addRole(subjects);

        Set<Attribute> resources = new HashSet<Attribute>(1);
        addResource(resources, layerName,XACMLConstants.LayerResourceType);

        Set<Attribute> actions = new HashSet<Attribute>(1);
        addAction(actions);
        
        Set<Attribute> environment = new HashSet<Attribute>(1);

        RequestCtx ctx = new RequestCtx(subjects, resources, actions, environment);
        return ctx;
    }

}
