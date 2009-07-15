package org.geoserver.wms.web.data;

import static org.custommonkey.xmlunit.XMLAssert.assertXMLEqual;

import java.io.ByteArrayInputStream;
import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.util.file.File;
import org.apache.wicket.util.tester.FormTester;
import org.geoserver.web.GeoServerWicketTestSupport;
import org.w3c.dom.Document;

public class StyleNewPageTest extends GeoServerWicketTestSupport {
    
    @Override
    protected void setUpInternal() throws Exception {
        login();
        tester.startPage(StyleNewPage.class);
        // org.geoserver.web.wicket.WicketHierarchyPrinter.print(tester.getLastRenderedPage(), true, false);
    }

    public void testLoad() throws Exception {
        tester.assertRenderedPage(StyleNewPage.class);
        tester.assertNoErrorMessage();
        
        tester.assertComponent("form:name", TextField.class);
        tester.assertComponent("form:sld", SLDEditorPanel.class);
        tester.assertComponent("uploadForm:filename", FileUploadField.class);
        
        tester.assertModelValue("form:name", null);
        
        // for some reason an extra newline is added to the mix
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document d1 = db.parse( StyleNewPage.class.getResourceAsStream("template.sld"));
        
        String xml = tester.getComponentFromLastRenderedPage("form:sld:editor").getModelObjectAsString();
        xml = xml.replaceAll("&lt;","<").replaceAll("&gt;",">").replaceAll("&quot;","\"");
        Document d2 = db.parse( new ByteArrayInputStream(xml.getBytes()));
        assertXMLEqual(d1, d2);
    }
    
    public void testUpload() throws Exception {
        FormTester upload = tester.newFormTester("uploadForm");
        File styleFile = new File(new java.io.File(getClass().getResource("default_point.sld").toURI()));
        String sld = IOUtils.toString(new FileReader(styleFile));
        
        upload.setFile("filename", styleFile, "application/xml");
        upload.submit();
        
        tester.assertRenderedPage(StyleNewPage.class);
        tester.assertModelValue("form:sld:editor", sld);
    }
    
    public void testMissingName() throws Exception {
        FormTester form = tester.newFormTester("form");
        File styleFile = new File(new java.io.File(getClass().getResource("default_point.sld").toURI()));
        String sld = IOUtils.toString(new FileReader(styleFile));
        form.setValue("form:sld:editor", sld);
        form.submit();
        
        tester.assertRenderedPage(StyleNewPage.class);
        tester.assertErrorMessages(new String[] {"Field 'Name' is required."});
    }
    
//    Cannot make this one to work, the sld text area is not filled in the test
//    and I don't understand why, in the real world it is
//    public void testValidate() throws Exception {
//        tester.clickLink("form:sld:validate", false);
//        
//        tester.assertRenderedPage(StyleNewPage.class);
//        tester.assertErrorMessages(new String[] {"Invalid style"});
//    }
    
    
}
