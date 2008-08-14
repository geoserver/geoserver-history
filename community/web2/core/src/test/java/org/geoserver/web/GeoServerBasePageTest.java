package org.geoserver.web;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;

public class GeoServerBasePageTest extends GeoServerWicketTestSupport {
    public void testLoginFormShowsWhenLoggedOut() throws Exception {
        // Initially, we will not be logged in, so the form should be hidden.
        tester.startPage(GeoServerHomePage.class);
        tester.assertVisible("loginform");
        tester.assertInvisible("logoutform");
    }

    public void testLogoutFormShowsWhenLoggedIn() throws Exception {
        // Simulate logging in by directly manipulating the Acegi setup.
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(
            "admin",
            "geoserver",
            new GrantedAuthority[]{
                new GrantedAuthorityImpl("ROLE_ADMINISTRATOR")
            }
            )
        );
        tester.startPage(GeoServerHomePage.class);
    }
}
