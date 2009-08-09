/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */


package org.geoserver.xacml.role;

import java.util.Set;

import org.acegisecurity.Authentication;

/**
 * A RoleAssignmentAuthority is responsible for finding the
 * role(Id)s for a given already authenticated subject.   
 * 
 * 
 * @author Christian Mueller
 *
 */
public interface RoleAssignmentAuthority {
        
    public Set<String> getRoleIdsFor (Authentication auth);        
    public boolean isCallerInRole (Authentication auth, String roleId);
        
}
