/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.request;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.acegisecurity.Authentication;
import org.geoserver.security.AccessMode;
import org.geoserver.wms.MapLayerInfo;
import org.geoserver.xacml.geoxacml.GeoXACMLConfig;
import org.geoserver.xacml.geoxacml.XACMLConstants;
import org.geoserver.xacml.role.Role;
import org.geoserver.xacml.role.RoleAssignmentAuthority;
import org.vfny.geoserver.Request;

import com.sun.xacml.attr.AnyURIAttribute;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.Subject;
import com.vividsolutions.jts.geom.Geometry;


/**
 * Base class for geoxacml request context builders
 * The class inheritance structure is mirrored from {@link Request}
 * 
 * 
 * @author Christian Mueller
 *
 */
public abstract class RequestCtxBuilder extends Object {
    
                
    private Role role;
    private AccessMode mode;
    
    public Role getRole() {
        return role;
    }


    protected RequestCtxBuilder(Role role,AccessMode mode) {
        this.role=role;
        this.mode=mode;
    }
    
    
    protected void addRole(Set<Subject> subjects) {
        
            
        URI roleIdURI = null,roleURI=null;
        try {
            roleIdURI=new URI(XACMLConstants.RoleIdPrefix+role.getId());
            roleURI=new URI(role.getId());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        
        Set<Attribute> subjectAttributes = new HashSet<Attribute>(1+role.getAttributes().size());
        
        AttributeValue roleAttributeValue = new AnyURIAttribute(roleURI);
        Attribute roleAttribute= new Attribute(roleIdURI,null,null,roleAttributeValue);
        subjectAttributes.add(roleAttribute);
        
        for (Entry<String,Object> paramEntry: role.getAttributes().entrySet()) {
            URI paramURI = null;
            try {
                paramURI=new URI(XACMLConstants.RoleIdPrefix+role.getId()+XACMLConstants.RoleParamIdInfix+paramEntry.getKey());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            // TODO, not anything is a string
            AttributeValue paramValue = new StringAttribute(paramEntry.getValue().toString());
            Attribute paramAttribute = new Attribute(paramURI,null,null,paramValue);
            subjectAttributes.add(paramAttribute);
        }
                
        Subject subject = new Subject(subjectAttributes);
        subjects.add(subject);

    }
    
    protected void addAction(Set<Attribute> actions) {
        actions.add(new Attribute(XACMLConstants.ActionAttributeURI,null,null,
                new StringAttribute(mode.toString())));        
    }

    
    
    protected void addResource(Set<Attribute> resources, String name, String type) {
        resources.add(new Attribute(XACMLConstants.ResourceAttributeURI,null,null,
                new StringAttribute(name)));        
        resources.add(new Attribute(XACMLConstants.ResourceTypeURI,null,null,
                new StringAttribute(type)));                       
    }
    protected void addGeometry(RequestCtx ctx, Geometry g, String srsName) {
        // TDOO
    }
    
    abstract public RequestCtx createRequestCtx();
        

        
}
