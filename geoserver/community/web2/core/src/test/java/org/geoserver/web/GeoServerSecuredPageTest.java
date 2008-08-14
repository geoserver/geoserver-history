package org.geoserver.web;

import org.geoserver.web.data.tree.DataPage;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.context.SecurityContextImpl;

public class GeoServerSecuredPageTest extends GeoServerWicketTestSupport {
    public void testSecuredPageGivesRedirectWhenLoggedOut() {
        logout();
        tester.startPage(DataPage.class);
        tester.assertRenderedPage(UnauthorizedPage.class);
    }

    public void testSecuredPageAllowsAccessWhenLoggedIn() {
        login();
        tester.startPage(DataPage.class);
        tester.assertRenderedPage(DataPage.class);
    }
}
