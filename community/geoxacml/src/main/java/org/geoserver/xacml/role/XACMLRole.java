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
 *  An example is the role "EMPLOYEE" with a role parameter PERSONAL_NUMBER
 *  
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
    
    public XACMLRole(String authority) {
     this(authority, null);   
    }
    
    public XACMLRole(String authority , Map<String,Serializable> attributes) {
        this.authority=authority;
        this.attributes=attributes;
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
