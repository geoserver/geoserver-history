package org.geoserver.wms.web.data;

import org.geoserver.web.GeoServerBasePage;
import org.geoserver.catalog.StyleInfo;
import org.vfny.geoserver.global.GeoserverDataDirectory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;

@SuppressWarnings("serial")
public class StyleEditorPage extends GeoServerBasePage{
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
                    File f = GeoserverDataDirectory.findStyleFile(style.getFilename());
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
                    // writer.write(rawSLD, 0, rawSLD.length()); // Commented out for now; no other changes are persisted so let's be consistent
                    writer.flush();
                    writer.close();

                    getCatalog().save(style);
                } catch (IOException ioe){
                    throw new WicketRuntimeException("Couldn't save SLD file.", ioe);
                }
            }
        };
        theForm.add(new TextField("name"));
        theForm.add(new TextArea("sld", new PropertyModel(this, "rawSLD")));
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
}
