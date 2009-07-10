package org.geoserver.web.data.store;

import org.apache.wicket.util.tester.FormTester;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;

public class CoverageStoreEditPageTest extends GeoServerWicketTestSupport {

    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        dataDirectory.addWellKnownCoverageTypes();
    }
    
    @Override
    protected void setUpInternal() throws Exception {
        CoverageStoreInfo coverageStore = getCatalog().getStoreByName(MockData.TASMANIA_BM.getLocalPart(), CoverageStoreInfo.class);
        tester.startPage(new CoverageStoreEditPage(coverageStore.getId()));
        
        // print(tester.getLastRenderedPage(), true, true);
    }
    
    public void testLoad() {
        tester.assertRenderedPage(CoverageStoreEditPage.class);
        tester.assertNoErrorMessage();
        
        tester.assertLabel("rasterStoreForm:storeType", "GeoTIFF");
        tester.assertModelValue("rasterStoreForm:namePanel:border:paramValue", "BlueMarble");
    }
    
    public void testChangeName() {
        FormTester form = tester.newFormTester("rasterStoreForm");
        form.setValue("namePanel:border:paramValue", "BlueMarbleModified");
        form.submit();
        tester.clickLink("rasterStoreForm:save");
        
        tester.assertNoErrorMessage();
        tester.assertRenderedPage(StorePage.class);
        assertNotNull(getCatalog().getStoreByName("BlueMarbleModified", CoverageStoreInfo.class));
    }
    
    public void testNameRequired() {
        FormTester form = tester.newFormTester("rasterStoreForm");
        form.setValue("namePanel:border:paramValue", null);
        form.submit();
        tester.clickLink("rasterStoreForm:save");
        
        tester.assertRenderedPage(CoverageStoreEditPage.class);
        tester.assertErrorMessages(new String[] {"Store name is required"});
    }
    
    
}
