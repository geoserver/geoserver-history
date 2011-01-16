package org.geoserver.web;

import java.util.Collections;

public class GeoServerHomePageTest extends GeoServerWicketTestSupport {
    public void testLabels() {
        tester.startPage(GeoServerHomePage.class);

        tester.assertListView(
                "providedCaps",
                Collections.singletonList(getGeoServerApplication().getBeanOfType(
                        CapabilitiesHomePageLinkProvider.class)));
    }
}
