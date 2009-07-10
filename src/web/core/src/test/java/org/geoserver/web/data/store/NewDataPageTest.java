/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListView;
import org.geoserver.web.GeoServerWicketTestSupport;

public class NewDataPageTest extends GeoServerWicketTestSupport {

    @Override
    protected void setUpInternal() throws Exception {
        tester.startPage(new NewDataPage());
        
        //print(tester.getLastRenderedPage(), true, true);
    }
    
    public void testLoad() {
        tester.assertRenderedPage(NewDataPage.class);
        tester.assertNoErrorMessage();
        
        tester.assertComponent("storeForm:vectorResources", ListView.class);
        tester.assertComponent("storeForm:rasterResources", ListView.class);
    }
    
    public void testClickLink() {
        Label label = (Label) findComponentByContent(tester.getLastRenderedPage(), "Properties", Label.class);
        // getPath() will start with 0: which indicates the page
        tester.clickLink(label.getParent().getPath().substring(2));
        
        tester.assertNoErrorMessage();
        tester.assertRenderedPage(DataAccessNewPage.class);
        
        // print(tester.getLastRenderedPage(), true, true);
        tester.assertModelValue("dataStoreForm:storeType", "Properties");
        
    }

}
