/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.xacml.role;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.acegisecurity.GrantedAuthority;

/**
 * @author Christian Mueller
 * 
 * Class for holding a security role, roles can have attributes.
 * This role class is intended for building a role object for an xacml
 * request.
 * 
 * According to the RBAC XACML specification, roles can be disabled. 
 * 
 * An example for a role is  "EMPLOYEE" with a role parameter PERSONAL_NUMBER
 *  
 * For integration into acegi security framework, this class implements the
 * acegi GrantedAuthority interface. 
 *   
 *
 */
public class XACMLRole implements GrantedAuthority {
       

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String authority;
    private Map<String,Serializable> attributes;
    private boolean enabled;
    private boolean roleAttributesProcessed;
    public boolean isRoleAttributesProcessed() {
        return roleAttributesProcessed;
    }

    public void setRoleAttributesProcessed(boolean roleAttributesProcessed) {
        this.roleAttributesProcessed = roleAttributesProcessed;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public XACMLRole(String authority) {
     this(authority, null);   
    }
    
    public XACMLRole(String authority , Map<String,Serializable> attributes) {
        this.authority=authority;
        this.attributes=attributes;
        this.enabled=true;
        this.roleAttributesProcessed=false;
    }    
    public String getAuthority() {
        return authority;
    }
    public Map<String,Serializable> getAttributes() {
        if (attributes==null) 
            attributes=new HashMap<String,Serializable>();
        return attributes;
    }
    
    public boolean hasAttributes() {
        return attributes!=null && attributes.isEmpty()==false;
    }

}
