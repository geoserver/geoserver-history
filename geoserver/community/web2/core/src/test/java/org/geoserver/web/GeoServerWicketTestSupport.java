package org.geoserver.web;

import org.geoserver.test.GeoServerTestSupport;
import org.apache.wicket.util.tester.WicketTester;

public abstract class GeoServerWicketTestSupport extends GeoServerTestSupport {
    WicketTester tester;

    public void oneTimeSetUp() throws Exception {
        super.oneTimeSetUp();
        GeoServerApplication app = 
            (GeoServerApplication) applicationContext.getBean("webApplication");
        tester = new WicketTester(app);
        app.init();
    }

    public GeoServerApplication getGeoServerApplication(){
        return GeoServerApplication.get();
    }
}
