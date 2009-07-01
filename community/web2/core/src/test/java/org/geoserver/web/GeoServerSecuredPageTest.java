package org.geoserver.web;

import org.geoserver.web.data.layer.LayerPage;

public class GeoServerSecuredPageTest extends GeoServerWicketTestSupport {
    public void testSecuredPageGivesRedirectWhenLoggedOut() {
        logout();
        tester.startPage(LayerPage.class);
        tester.assertRenderedPage(GeoServerLoginPage.class);
    }

    public void testSecuredPageAllowsAccessWhenLoggedIn() {
        login();
        tester.startPage(LayerPage.class);
        tester.assertRenderedPage(LayerPage.class);
    }
}
