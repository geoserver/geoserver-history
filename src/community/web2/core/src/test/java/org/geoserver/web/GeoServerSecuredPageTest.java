package org.geoserver.web;

import org.geoserver.web.data.table.LayerPage;

public class GeoServerSecuredPageTest extends GeoServerWicketTestSupport {
    public void testSecuredPageGivesRedirectWhenLoggedOut() {
        logout();
        tester.startPage(LayerPage.class);
        tester.assertRenderedPage(UnauthorizedPage.class);
    }

    public void testSecuredPageAllowsAccessWhenLoggedIn() {
        login();
        tester.startPage(LayerPage.class);
        tester.assertRenderedPage(LayerPage.class);
    }
}
