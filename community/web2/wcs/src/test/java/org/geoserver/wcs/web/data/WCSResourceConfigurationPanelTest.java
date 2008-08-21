package org.geoserver.wcs.web.data;

import org.geoserver.catalog.CoverageInfo;
import org.geoserver.wcs.web.GeoServerWicketCoverageTestSupport;
import org.geoserver.web.data.ResourceConfigurationPage;

import org.apache.wicket.model.CompoundPropertyModel;

public class WCSResourceConfigurationPanelTest extends GeoServerWicketCoverageTestSupport{
    public void testValues() {
        CoverageInfo info = 
            getGeoServerApplication().getCatalog().getResources(CoverageInfo.class).get(0);

        login();
        tester.startPage(new ResourceConfigurationPage(info, false));
    }
}
