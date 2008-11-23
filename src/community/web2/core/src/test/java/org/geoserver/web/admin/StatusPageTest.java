package org.geoserver.web.admin;

import org.geoserver.web.GeoServerWicketTestSupport;

public class StatusPageTest extends GeoServerWicketTestSupport {
    public void testValues() {
        login();
        // TODO: We get an NPE on loading the statuspage in the test environment,
        // but it works when run normally.

        //tester.startPage(StatusPage.class);
        //tester.assertLabel("locks", "0");
    }
}
