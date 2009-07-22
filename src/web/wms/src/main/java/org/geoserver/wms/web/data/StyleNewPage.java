package org.geoserver.wms.web.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

import org.apache.commons.io.IOUtils;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.calldecorator.AjaxPreprocessingCallDecorator;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.lang.Bytes;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.ows.util.ResponseUtils;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.wms.web.publish.StylesModel;

/**
 * Allows for editing a new style, includes file upload
 */
@SuppressWarnings("serial")
public class StyleNewPage extends GeoServerSecuredPage {

    TextField nameTextField;
    SLDEditorPanel sldEditorPanel;
    FileUploadField fileUploadField;
    private DropDownChoice styles;
    private AjaxSubmitLink copyLink;
    
    public StyleNewPage() {
        StyleInfo s = getCatalog().getFactory().createStyle();
        
        final Form form = new Form( "form", new CompoundPropertyModel( s ) ) {
            @Override
            protected void onSubmit() {
              //add the style
                Catalog catalog = getCatalog();
                StyleInfo s = (StyleInfo) getModelObject();
                
                if ( s.getFilename() == null ) {
                    //TODO: check that this does not overriDe any existing files
                    s.setFilename( s.getName() + ".sld" );
                }
                try {
                    getCatalog().add( s );    
                }
                catch( Exception e ) {
                    error( e );
                    return;
                }

                //write out the SLD
                try {
                    catalog.getResourcePool().writeStyle( s, 
                            new ByteArrayInputStream( sldEditorPanel.getRawSLD().getBytes() ) );
                } 
                catch (IOException e) {
                    throw new WicketRuntimeException(e);
                }
                
                setResponsePage(StylePage.class);
            }
        };
        form.setMarkupId("mainForm");
        add(form);
        
        form.add( nameTextField = new TextField( "name") );
        nameTextField.setRequired(true);
        form.add( sldEditorPanel = new SLDEditorPanel( "sld", new Model() ) );
        sldEditorPanel.setOutputMarkupId( true );
        
        // style copy functionality
        styles = new DropDownChoice("existingStyles", new Model(), new StylesModel());
        styles.setOutputMarkupId(true);
        styles.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                styles.validate();
                copyLink.setEnabled(styles.getConvertedInput() != null);
                target.addComponent(copyLink);
            }
        });
        form.add(styles);
        copyLink = copyLink();
        copyLink.setEnabled(false);
        form.add(copyLink);
        
        Form uploadForm = new Form("uploadForm") {
          @Override
            protected void onSubmit() {
                  FileUpload upload = fileUploadField.getFileUpload();
                  if ( upload == null ) {
                      warn( "No file selected.");
                      return;
                  }
                  ByteArrayOutputStream bout = new ByteArrayOutputStream();
                  
                  try {
                      IOUtils.copy(upload.getInputStream(), bout );
                      sldEditorPanel.setRawSLD( new ByteArrayInputStream( bout.toByteArray() ) );
                      sldEditorPanel.updateModel();
                  } 
                  catch (IOException e) {
                      throw new WicketRuntimeException( e );
                  }
                  
                  //update the style object
                  StyleInfo s = (StyleInfo) form.getModelObject();
                  s.setFilename( upload.getClientFileName() );
                  
                  if ( s.getName() == null || "".equals( s.getName().trim() ) ) {
                      //set it
                      nameTextField.setModelValue( ResponseUtils.stripExtension( upload.getClientFileName() ) );
                      nameTextField.modelChanged();
                  }
            }  
        };
        uploadForm.setMultiPart(true);
        uploadForm.setMaxSize(Bytes.megabytes(1));
        uploadForm.setMarkupId("uploadForm");
        add(uploadForm);

        
        uploadForm.add( fileUploadField = new FileUploadField( "filename" ) );
        
        AjaxLink cancelLink = new AjaxLink( "cancel" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage( StylePage.class );
            }
        };
        add( cancelLink );
    }
    
    private AjaxSubmitLink copyLink() {
        return new AjaxSubmitLink("copy") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                // we need to force validation or the value won't be converted
                styles.validate();
                StyleInfo style = (StyleInfo) styles.getConvertedInput();
    
                if(style != null) {
                    try {
                        // same here, force validation or the field won't be udpated
                        sldEditorPanel.getTextArea().validate();
                        sldEditorPanel.getTextArea().clearInput();
                        sldEditorPanel.setRawSLD( readFile( style ) );
                    } catch(Exception e) {
                        error("Errors occurred loading the '" + style.getName() + "' style");
                    }
                    target.addComponent(sldEditorPanel);
                }
            }
            
            @Override
            protected IAjaxCallDecorator getAjaxCallDecorator() {
                return new AjaxPreprocessingCallDecorator(super.getAjaxCallDecorator()) {

                    @Override
                    public CharSequence preDecorateScript(CharSequence script) {
                        return "if(editAreaLoader.getValue('" + sldEditorPanel.getTextArea().getMarkupId() + "') != '' &&" +
                                "!confirm('" + new ParamResourceModel("confirmOverwrite", StyleNewPage.this).getString()
                                + "')) return false;" + script;
                    }
                };
            }
            
            @Override
            public boolean getDefaultFormProcessing() {
                return false;
            }
            
        };
    }
    
    Reader readFile(StyleInfo style) throws IOException{
        ResourcePool pool = getCatalog().getResourcePool();
        return pool.readStyle( style );
    }
}
