package org.geoserver.web.data;

import org.geoserver.web.GeoServerWicketTestSupport;
import org.geoserver.catalog.ResourceInfo;

public class ResourceConfigurationPageTest extends GeoServerWicketTestSupport {
    public void testValues() {
        ResourceInfo info = getGeoServerApplication()
            .getCatalog()
            .getResources(ResourceInfo.class).get(0);

        login();
        tester.startPage(new ResourceConfigurationPage(info, false));
        tester.assertLabel("resourcename", info.getName());
        tester.assertComponent("resource:tabs:panel:theList:0:content", BasicResourceConfig.class);
    }
}
