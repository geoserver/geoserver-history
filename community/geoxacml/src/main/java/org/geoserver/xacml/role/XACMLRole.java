/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.xacml.role;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Christian Mueller
 * 
 * Class for holding a security role, roles can have attributes.
 * This role class is intended for building a role object for an xacml
 * request.
 * 
 *  An example is the role "EMPLOYEE" with a role parameter PERSONAL_NUMBER
 *  
 *  this class is NOT used for role assignment 
 *
 */
public class XACMLRole {
       

    private String id;
    private Map<String,Object> attributes;
    
    public XACMLRole(String id) {
     this(id, null);   
    }
    
    public XACMLRole(String id , Map<String,Object> attributes) {
        this.id=id;
        this.attributes=attributes;
    }    
    public String getId() {
        return id;
    }
    public Map<String, Object> getAttributes() {
        if (attributes==null) 
            attributes=new HashMap<String,Object>();
        return attributes;
    }
    
    public boolean hasAttributes() {
        return attributes!=null && attributes.isEmpty()==false;
    }
}
