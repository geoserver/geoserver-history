package org.geoserver.web.data.workspace;

import org.apache.wicket.util.tester.FormTester;
import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;

public class WorkspaceEditPageTest extends GeoServerWicketTestSupport {
    
    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        login();
        tester.startPage(new WorkspaceEditPage(getCatalog().getWorkspaceByName(MockData.CITE_PREFIX)));
        
        // print(tester.getLastRenderedPage(), true, true);
    }
    
    public void testLoad() {
        tester.assertRenderedPage(WorkspaceEditPage.class);
        tester.assertNoErrorMessage();
        
        tester.assertLabel("name", MockData.CITE_PREFIX);
        tester.assertModelValue("form:uri", MockData.CITE_URI);
    }
    
    public void testValidURI() {
        FormTester form = tester.newFormTester("form");
        form.setValue("uri", "http://www.geoserver.org");
        form.submit();
        
        tester.assertRenderedPage(WorkspacePage.class);
        tester.assertNoErrorMessage();
    }
    
    public void testInvalidURI()  {
        FormTester form = tester.newFormTester("form");
        form.setValue("uri", "not a valid uri");
        form.submit();
        
        tester.assertRenderedPage(WorkspaceEditPage.class);
        tester.assertErrorMessages(new String[] {"'not a valid uri' is not a valid URL."});
    }
}
