package org.geoserver.web;

import org.geoserver.platform.Service;

public class GeoServerHomePageTest extends GeoServerWicketTestSupport {
    public void testLabels(){
        tester.startPage(GeoServerHomePage.class);
        tester.assertListView("services", getGeoServerApplication().getBeansOfType(Service.class));
    }
}
