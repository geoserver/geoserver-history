/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.xacml.role;

import java.util.HashSet;
import java.util.Set;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;


/**
 * Acegi implementation for {@link RoleAssignmentAuthority}
 * 
 *
 * @author Christian Mueller
 *
 */
public class DefaultRoleAssignmentAuthority implements RoleAssignmentAuthority {

    public Set<String> getRoleIdsFor(Authentication auth) {
        
        Set<String> result = new HashSet<String>();
        
        for (GrantedAuthority gAut: auth.getAuthorities()) {
            result.add(gAut.getAuthority());
        }
        return result;
    }

    public boolean isCallerInRole(Authentication auth, String roleId) {
        for (GrantedAuthority gAut: auth.getAuthorities()) {
            if (roleId.equals(gAut.getAuthority())) return true;
        }
        return false;
    }

}
