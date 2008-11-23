package org.geoserver.web.data.tree;

import org.geoserver.web.GeoServerWicketTestSupport;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.ajax.markup.html.AjaxLink;

public class DataPageTest extends GeoServerWicketTestSupport {
    public void testValues() {
        tester.startPage(DataPage.class);
        tester.executeAjaxEvent("treeParent:dataTree:i:5:middleColumns:1:link", "onclick");
        tester.executeAjaxEvent("treeParent:dataTree:i:6:sideColumns:2:edit", "onclick");
        tester.assertRenderedPage(DataStoreConfiguration.class);
       
//      tester.clickLink("treeParent:dataTree:i:25:sideColumns:2:edit");
    }
}
