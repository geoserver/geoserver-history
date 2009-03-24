package org.geoserver.wms.web.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.GeoServerSecuredPage;

public class StyleEditPage extends GeoServerSecuredPage {
    SLDEditorPanel sldEditorPanel;
    
    public StyleEditPage(final StyleInfo style){
        final Form theForm = new Form("theForm", new CompoundPropertyModel(style));
        theForm.add(new TextField("name"));
        
        sldEditorPanel = new SLDEditorPanel("sld", new Model() );
        
        //final XMLEditor xmlEditor = new XMLEditor("sld", new PropertyModel(this, "rawSLD")); 
        theForm.add(sldEditorPanel);
        try {
            sldEditorPanel.setRawSLD( readFile( style ) );
        } 
        catch (IOException e) {
            throw new WicketRuntimeException( e );
        }
        theForm.add(new SubmitLink("submit"){
            @Override
            public void onSubmit() {
                //write out the file
                try {
                    writeFile( style, sldEditorPanel.getRawSLD() );
                    setResponsePage( StylePage.class );
                }
                catch( Exception e ) {
                    theForm.error( e );
                }
            }
        });
        
        theForm.add(new SubmitLink("cancel"){
            @Override
            public void onSubmit() {
                setResponsePage( StylePage.class );
            }
        });
        
        add(theForm);
    }

    Reader readFile(StyleInfo style) throws IOException{
        ResourcePool pool = getCatalog().getResourcePool();
        return pool.readStyle( style );
    }
    
    void writeFile(StyleInfo style, String sld) throws IOException {
        ResourcePool pool = getCatalog().getResourcePool();
        pool.writeStyle( style, new ByteArrayInputStream( sld.getBytes() ) );
    }
}
