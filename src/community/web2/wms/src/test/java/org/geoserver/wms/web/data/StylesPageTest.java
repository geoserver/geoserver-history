package org.geoserver.wms.web.data;

import org.geoserver.web.GeoServerWicketTestSupport;
import org.geoserver.web.wicket.RichEditableLabel;

public class StylesPageTest extends GeoServerWicketTestSupport {
    public void testValues(){
        login();
        tester.startPage(StylesPage.class);
        tester.assertComponent("styles:1:name", RichEditableLabel.class);
        tester.clickLink("styles:1:edit");
        tester.assertRenderedPage(StyleEditorPage.class);
    }
}
