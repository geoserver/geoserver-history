package org.geoserver.web.wicket.browser;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.Model;
import org.geoserver.web.ComponentBuilder;
import org.geoserver.web.FormTestPage;
import org.geoserver.web.GeoServerWicketTestSupport;

public class GeoServerFileChooserTest extends GeoServerWicketTestSupport {

    private File root;
    private File one;
    private File two;
    private File child;

    @Override
    protected void setUpInternal() throws Exception {
        super.setUpInternal();
        
        root = new File("target/test-filechooser");
        if(root.exists())
            FileUtils.deleteDirectory(root);
        child = new File(root, "child");
        child.mkdirs();
        one = new File(child, "one.txt");
        one.createNewFile();
        two = new File(child, "two.sld");
        two.createNewFile();
        
        tester.startPage(new FormTestPage(new ComponentBuilder() {
            
            public Component buildComponent(String id) {
                return new GeoServerFileChooser(id, new Model(root));
            }
        }));
        
        // WicketHierarchyPrinter.print(tester.getLastRenderedPage(), true, true);
    }
    
    public void testLoad() {
        tester.assertRenderedPage(FormTestPage.class);
        tester.assertNoErrorMessage();
        
        tester.assertLabel("form:panel:fileTable:fileTable:files:1:nameLink:name", "child/");
        assertEquals(1, ((DataView) tester.getComponentFromLastRenderedPage("form:panel:fileTable:fileTable:files")).size());
    }
    
}
