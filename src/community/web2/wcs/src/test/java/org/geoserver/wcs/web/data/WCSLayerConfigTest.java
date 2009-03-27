package org.geoserver.wcs.web.data;

import org.apache.wicket.model.Model;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.wcs.web.GeoServerWicketCoverageTestSupport;
import org.geoserver.wcs.web.publish.WCSLayerConfig;

public class WCSLayerConfigTest extends GeoServerWicketCoverageTestSupport{
    public void testValues() {
        CoverageInfo info = getCatalog().getResources(CoverageInfo.class).get(0);
        LayerInfo layer = getCatalog().getLayerByName(info.getName());

        login();
        tester.startComponent(new WCSLayerConfig("testId", new Model(layer)));
    }
}
