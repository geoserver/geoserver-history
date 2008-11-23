package org.geoserver.wms.web.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.XMLEditor;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.vfny.geoserver.global.GeoserverDataDirectory;

@SuppressWarnings("serial")
public class StyleEditorPage extends GeoServerSecuredPage {
    @SuppressWarnings("unused")
    private String rawSLD; // Accessed via a property model, don't remove

    public StyleEditorPage(final StyleInfo style){
        try{
            rawSLD = readFile(style.getFilename());
        } catch (IOException ioe){
            throw new WicketRuntimeException("Couldn't read SLD file.", ioe);
        }

        Form theForm = new Form("theForm", new CompoundPropertyModel(style)){
            public void onSubmit(){
                try{
                    File newFile = writeToTempFile(rawSLD);
                    if (isValidSLD(newFile)){
                        overWriteCurrent(style, newFile);
                        getCatalog().save(style);
                    } else {
                        newFile.delete();
                    }


                    getCatalog().save(style);
                } catch (IOException ioe){
                    throw new WicketRuntimeException("Couldn't save SLD file.", ioe);
                }
            }
        };

        theForm.add(new TextField("name"));
        theForm.add(new XMLEditor("sld", new PropertyModel(this, "rawSLD")));
        theForm.add(new Button("submit"));
        theForm.add(new Button("cancel"));
        add(theForm);
    }

    private static String readFile(String filename) throws IOException{
        File f = GeoserverDataDirectory.findStyleFile(filename);
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
        StringBuilder builder = new StringBuilder();
        char[] buff = new char[4096];
        int amount = -1;

        while ((amount = reader.read(buff, 0, 4096)) != -1){
            builder.append(buff, 0, amount);
        }

        return builder.toString();
    }

    private static File writeToTempFile(String raw) throws IOException {
        File f = File.createTempFile("style", "sld");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
        writer.write(raw, 0, raw.length());
        writer.flush();
        writer.close();
        return f;
    }

    private static boolean isValidSLD(File sld) throws IOException {
        StyleFactory factory = StyleFactoryFinder.createStyleFactory();
        SLDParser styleReader = new SLDParser(factory, sld.toURL());
        Style[] readStyles = null;

        try {
            readStyles = styleReader.readXML();

            if (readStyles.length == 0 || readStyles[0] == null) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();

            String message = (e.getCause() == null) 
                ? e.getLocalizedMessage()
                : e.getCause().getLocalizedMessage();
            return false;
        }

        return true;
    }

    private static void overWriteCurrent(StyleInfo style, File f){
        f.renameTo(GeoserverDataDirectory.findStyleFile(style.getFilename()));
    }
}
