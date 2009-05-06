package org.geoserver.web.data.layer;

import junit.framework.Test;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.geoserver.web.wicket.GeoServerTablePanel;

public class NewLayerPageTest extends GeoServerWicketTestSupport {

    
    private static final String TABLE_PATH = "selectLayersContainer:selectLayers:layersContainer:layers";

    /**
     * This is a READ ONLY TEST so we can use one time setup
     */
    public static Test suite() {
        return new OneTimeTestSetup(new NewLayerPageTest());
    }
    
    public void testKnownStore() {
        login();
        DataStoreInfo store = getCatalog().getStoreByName(MockData.CDF_PREFIX, DataStoreInfo.class); 
        tester.startPage(new NewLayerPage(store.getId()));
        
        tester.assertRenderedPage(NewLayerPage.class);
        assertNull(tester.getComponentFromLastRenderedPage("selector"));
        GeoServerTablePanel table = (GeoServerTablePanel) tester.getComponentFromLastRenderedPage(TABLE_PATH);
        assertEquals(getCatalog().getResourcesByStore(store, FeatureTypeInfo.class).size(), table.getDataProvider().size());
    }
    
    public void testAjaxChooser() {
        login();
        tester.startPage(new NewLayerPage());
        
        tester.assertRenderedPage(NewLayerPage.class);
        
        // the tester will return null if the component is there, but not visible
        assertNull(tester.getComponentFromLastRenderedPage("selectLayersContainer:selectLayers"));
        
        // select the first datastore
        tester.newFormTester("selector").select("storesDropDown", 1);
        tester.executeAjaxEvent("selector:storesDropDown", "onchange");
        
        // now it should be there
        assertNotNull(tester.getComponentFromLastRenderedPage("selectLayersContainer:selectLayers"));
        
        // select "choose one" item (unselect the form)
        tester.newFormTester("selector").setValue("storesDropDown", "");
        tester.executeAjaxEvent("selector:storesDropDown", "onchange");
        
        // now it should be there
        assertNull(tester.getComponentFromLastRenderedPage("selectLayersContainer:selectLayers"));
    }
}
