package org.geoserver.wms.web.data;

import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.FormTester;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.vfny.geoserver.global.GeoserverDataDirectory;

public class StyleEditPageTest extends GeoServerWicketTestSupport {
    
    StyleInfo buildingsStyle;

    @Override
    protected void setUpInternal() throws Exception {
        login();
        buildingsStyle = getCatalog().getStyleByName(MockData.BUILDINGS.getLocalPart());
        StyleEditPage edit = new StyleEditPage(buildingsStyle);
        tester.startPage(edit);
//        org.geoserver.web.wicket.WicketHierarchyPrinter.print(tester.getLastRenderedPage(), true, false);
    }

    public void testLoad() throws Exception {
        tester.assertRenderedPage(StyleEditPage.class);
        tester.assertNoErrorMessage();
        
        tester.assertComponent("theForm:name", TextField.class);
        tester.assertComponent("theForm:sld", SLDEditorPanel.class);
        
        tester.assertModelValue("theForm:name", "Buildings");
        // for some reason an extra newline is added to the mix
        File styleFile = GeoserverDataDirectory.findStyleFile( buildingsStyle.getFilename() );
        String style = IOUtils.toString(new FileReader(styleFile));
        tester.assertModelValue("theForm:sld:editor", style);
    }
    
    public void testMissingName() throws Exception {
        FormTester form = tester.newFormTester("theForm");
        form.setValue("name", "");
        form.submit();
        
        tester.assertRenderedPage(StyleEditPage.class);
        tester.assertErrorMessages(new String[] {"Field 'Name' is required."});
    }
}
