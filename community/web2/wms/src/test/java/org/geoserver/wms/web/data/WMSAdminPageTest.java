package org.geoserver.wms.web.data;

import org.geoserver.web.GeoServerWicketTestSupport;
import org.geoserver.wms.WMSInfo;
import org.geoserver.wms.web.WMSAdminPage;

public class WMSAdminPageTest extends GeoServerWicketTestSupport {
    public void testValues() throws Exception {
        WMSInfo wfs = getGeoServerApplication().getGeoServer().getService(WMSInfo.class);

        login();
        tester.startPage(WMSAdminPage.class);
        tester.assertModelValue("form:keywords", wfs.getKeywords());
    }
}
