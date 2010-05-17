/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.geoserver.xacml.acegi;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.Authentication;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationToken;
import org.acegisecurity.providers.anonymous.AnonymousProcessingFilter;
import org.acegisecurity.ui.AuthenticationDetailsSource;
import org.acegisecurity.ui.AuthenticationDetailsSourceImpl;
import org.geoserver.xacml.role.XACMLRole;

/**
 * Creating an AnoynmaAuthenticationToken with XACML Roles
 * 
 * @author Christian Mueller
 * 
 */
public class XACMLAnonymousProcessingFilter extends AnonymousProcessingFilter {

    private AuthenticationDetailsSource authenticationDetailsSource = new AuthenticationDetailsSourceImpl();

    @Override
    protected Authentication createAuthentication(ServletRequest request) {
        GrantedAuthority[] auths = getUserAttribute().getAuthorities();
        XACMLRole[] roles = new XACMLRole[auths.length];
        for (int i = 0; i < auths.length; i++) {
            roles[i] = new XACMLRole(auths[i].getAuthority());
            roles[i].setRoleAttributesProcessed(true); // No userinfo for anonymous
        }

        AnonymousAuthenticationToken auth = new AnonymousAuthenticationToken(getKey(),
                getUserAttribute().getPassword(), roles);
        auth.setDetails(authenticationDetailsSource.buildDetails((HttpServletRequest) request));
        return auth;
    }

    @Override
    public void setAuthenticationDetailsSource(
            AuthenticationDetailsSource authenticationDetailsSource) {
        super.setAuthenticationDetailsSource(authenticationDetailsSource);
        this.authenticationDetailsSource = authenticationDetailsSource;
    }

}
