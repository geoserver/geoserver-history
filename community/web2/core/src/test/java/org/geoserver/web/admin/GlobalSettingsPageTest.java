package org.geoserver.web.admin;

import org.geoserver.config.GeoServerInfo;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.vfny.geoserver.config.GlobalConfig;

import org.apache.wicket.markup.html.form.TextField;

public class GlobalSettingsPageTest extends GeoServerWicketTestSupport {
    public void testValues() {
        GeoServerInfo info = getGeoServerApplication().getGeoServer().getGlobal();
        GlobalConfig config = (GlobalConfig) getGeoServerApplication()
            .getApplicationContext()
            .getBean("globalConfig");

        login();
        tester.startPage(GlobalSettingsPage.class);
        tester.assertComponent("form:maxFeatures", TextField.class);
        tester.assertModelValue("form:maxFeatures", config.getMaxFeatures());
    }
}
