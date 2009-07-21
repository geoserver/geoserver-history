/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
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

/**
 * Style edit page
 */
@SuppressWarnings("serial")
public class StyleEditPage extends GeoServerSecuredPage {
    SLDEditorPanel sldEditorPanel;
    TextField name;
    
    public StyleEditPage(final StyleInfo style){
        final Form theForm = new Form("theForm", new CompoundPropertyModel(style));
        theForm.add(name = new TextField("name"));
        name.setRequired(true);
        
        sldEditorPanel = new SLDEditorPanel("sld", new Model() );
        
        theForm.add(sldEditorPanel);
        try {
            sldEditorPanel.setRawSLD( readFile( style ) );
        } 
        catch (IOException e) {
            throw new WicketRuntimeException( e );
        }
        SubmitLink submit = new SubmitLink("submit"){
            @Override
            public void onSubmit() {
                //write out the file and save name modifications
                try {
                    getCatalog().save((StyleInfo) theForm.getModelObject());
                    writeFile( style, sldEditorPanel.getRawSLD() );
                    setResponsePage( StylePage.class );
                }
                catch( Exception e ) {
                    theForm.error( e );
                }
            }
        };
        theForm.add(submit);
        theForm.setDefaultButton(submit);
        
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
