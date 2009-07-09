package org.geoserver.web.data.store;

import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;

public class DataAccessEditPageTest extends GeoServerWicketTestSupport {

    @Override
    protected void populateDataDirectory(MockData dataDirectory) throws Exception {
        super.populateDataDirectory(dataDirectory);
        dataDirectory.addWellKnownCoverageTypes();
    }
    
    @Override
    protected void setUpInternal() throws Exception {
        DataStoreInfo store = getCatalog().getStoreByName(MockData.CITE_PREFIX, DataStoreInfo.class);
        tester.startPage(new DataAccessEditPage(store.getId()));
        
//        print(tester.getLastRenderedPage(), true, true);
    }
    
    public void testLoad() {
        tester.assertRenderedPage(DataAccessEditPage.class);
        tester.assertNoErrorMessage();
        
        tester.assertLabel("dataStoreForm:storeType", "Properties");
        tester.assertModelValue("dataStoreForm:dataStoreNamePanel:border:paramValue", "cite");
        String expectedPath = new File(getTestData().getDataDirectoryRoot(), "cite").getPath();
        tester.assertModelValue("dataStoreForm:parameters:0:parameterPanel:border:paramValue", expectedPath);
    }
    
    
    // This is disabled due to bad interactions between the submit link and the form submit
    // I need to reproduce ina stand alone test case and report to the Wicket devs
//    public void testEditName() {
//        
//        FormTester form = tester.newFormTester("dataStoreForm");
//        prefillForm(form);
//        form.setValue("dataStoreNamePanel:border:paramValue", "citeModified");
//        form.submit();
//        tester.assertNoErrorMessage();
//        tester.clickLink("dataStoreForm:save");
//        tester.assertNoErrorMessage();
//        
//        tester.assertRenderedPage(StorePage.class);
//    }
    
    public void testNameRequired() {
        FormTester form = tester.newFormTester("dataStoreForm");
        form.setValue("dataStoreNamePanel:border:paramValue", null);
        form.submit();
        // missing click link , the validation triggers before it
        
        tester.assertRenderedPage(DataAccessEditPage.class);
        tester.assertErrorMessages(new String[] {"Store name is required"});
    }
    
    
}
