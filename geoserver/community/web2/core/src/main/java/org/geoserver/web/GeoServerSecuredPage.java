/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;

/**
 * Base class for secured web pages. By default it only allows
 * @author Andrea Aime - TOPP
 *
 */
public class GeoServerSecuredPage extends GeoServerBasePage {
    
    public GeoServerSecuredPage() {
        super();
        if(!isAccessAllowed(getSession().getAuthentication()))
            setResponsePage(new UnauthorizedPage());
    }
    
    /**
     * By default, checks the user is logged-in and has administration
     * roles, this can be overridden by subclasses
     * @param authentication
     * @return
     */
    public boolean isAccessAllowed(Authentication authentication) {
        if(authentication == null)
            return false;
        
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if("ROLE_ADMINISTRATOR".equals(authority.getAuthority()))
                return true;
        }
        return false;
    }
}
