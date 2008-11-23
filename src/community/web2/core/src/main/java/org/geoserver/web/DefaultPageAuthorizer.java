package org.geoserver.web;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;

public class DefaultPageAuthorizer implements PageAuthorizer {

    /**
     * Checks the user is logged-in and has administration roles, this can be
     * overridden by subclasses
     */
    public boolean isAccessAllowed(Class pageClass, Authentication authentication) {
        if (authentication == null)
            return false;

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if ("ROLE_ADMINISTRATOR".equals(authority.getAuthority()))
                return true;
        }
        return false;
    }
}
