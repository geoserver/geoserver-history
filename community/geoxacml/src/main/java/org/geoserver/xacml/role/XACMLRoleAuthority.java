/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */


package org.geoserver.xacml.role;

import java.util.Set;

import org.acegisecurity.Authentication;

/**
 * A RoleAssignmentAuthority is NOT responsible for assignment from roles to subjects
 * The Authentication object has already roles roles assigned (GrantedAuthorities)
 * 
 * The purpose of this Authority is
 * 
 * 1) add needed role parameters 
 * 2) check against the XACML repository if the role is enabled 
 *    (e.g if the role is enabled only between 8:00 and 16:00)  
 * 
 * 
 * @author Christian Mueller
 *
 */
public interface XACMLRoleAuthority {
        
    public Set<XACMLRole> getRolesFor (Authentication auth);        
    public boolean isCallerInRole (Authentication auth, String roleId);
        
}
