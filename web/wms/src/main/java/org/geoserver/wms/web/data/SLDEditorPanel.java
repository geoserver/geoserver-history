/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.web.wicket.EditAreaBehavior;
import org.geotools.sld.SLDConfiguration;
import org.geotools.xml.Parser;

/**
 * Panel allowing for style editing   
 */
@SuppressWarnings("serial")
public class SLDEditorPanel extends FormComponentPanel {
    
    private String rawSLD; // Accessed via a property model, don't remove
    private TextArea editor;

    public SLDEditorPanel( String id ) {
        super(id);
        
        initComponents();
    }
    
    public SLDEditorPanel( String id, IModel model ) {
        super( id, model );
        
        initComponents();
    }
    
    void initComponents() {
        add( editor = new TextArea("editor", new PropertyModel(this, "rawSLD")) );
        editor.add(new EditAreaBehavior());
        add(new SubmitLink("validate") {
            @Override
            public void onSubmit() {
                Form form = getForm();
                
                if ( form != null ) {
                    List<Exception> errors = validateSLD();
                    
                    if ( errors.isEmpty() ) {
                        form.info( "No validation errors.");
                    }
                    else {
                        for( Exception e : errors ) {
                            form.error( e );
                        }    
                    }        
                }
            }
        });
    }
    
    public String getRawSLD() {
        return rawSLD;
    }
    
    public void setRawSLD(String rawSLD) {
        this.rawSLD = rawSLD;
    }
    
    public void setRawSLD(InputStream in) throws IOException {
        setRawSLD( new InputStreamReader ( in ) );
    }
    
    public void setRawSLD(Reader in) throws IOException {
        BufferedReader bin = null;
        if ( in instanceof BufferedReader ) {
            bin = (BufferedReader) in;
        }
        else {
            bin = new BufferedReader( in );
        }
        
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = bin.readLine()) != null ) {
            builder.append(line).append("\n");
        }

        this.rawSLD = builder.toString();
        editor.setModelObject(rawSLD);
        in.close();
    }
    
    List<Exception> validateSLD() {
        Parser parser = new Parser(new SLDConfiguration());
        try {
            parser.validate( new ByteArrayInputStream(editor.getInput().getBytes()) );
        } catch( Exception e ) {
            return Arrays.asList( e );
        }
        
        return parser.getValidationErrors();
    }
    
    public TextArea getTextArea() {
        return editor;
    }
}
