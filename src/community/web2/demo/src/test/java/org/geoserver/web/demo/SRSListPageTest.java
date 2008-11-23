package org.geoserver.web.demo;

import org.geoserver.web.GeoServerWicketTestSupport;
import org.apache.wicket.markup.html.link.Link;

public class SRSListPageTest extends GeoServerWicketTestSupport {
    public void testValues() throws Exception {
        tester.startPage(SRSListPage.class);
        tester.assertComponent("listContainer:srslist:0:codeLink", Link.class);
        tester.clickLink("listContainer:srslist:0:codeLink");
        tester.assertRenderedPage(SRSDescriptionPage.class);
    }
}
