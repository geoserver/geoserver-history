package org.geoserver.web;

import org.acegisecurity.Authentication;

public interface PageAuthorizer {
    
    /**
     * Checks if the (bookmarkable) page can be accessed
     * @param pageClass The page class to be checked
     * @param authentication The current user
     * @return
     */
    public boolean isAccessAllowed(Class pageClass, Authentication authentication);
}
