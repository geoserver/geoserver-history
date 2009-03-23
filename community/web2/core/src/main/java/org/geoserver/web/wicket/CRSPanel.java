/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A form component for a {@link CoordinateReferenceSystem} object.
 * <p>
 * This panel provides the following functionality/information:
 * <ul>
 *   <li>The SRS (epsg code) of the CRS
 *   <li>View the full WKT of the CRS.
 *   <li>A mechanism to guess the SRS (epsg code) from the CRS
 *   <li>A lookup for browsing for a particular CRS 
 * </ul>
 * </p>
 * @author Justin Deoliveira, OpenGeo
 */
public class CRSPanel extends FormComponentPanel {
    
    /** pop-up window for WKT and SRS list */
    ModalWindow popupWindow;
    
    /** srs/epsg code text field */
    TextField srsTextField;
    
    /** find link */
    AjaxLink findLink;
    
    /** wkt label */
    Label wktLabel;
    
    /**
     * Constructs the CRS panel.
     * <p>
     * This constructor should be used if the panel is to inherit from a parent model 
     * (ie a CompoundPropertyModel). If no such model is available the CRS will be left 
     * uninitialized. To avoid inheriting from a parent model the constructor {@link #CRSPanel(String, IModel)}
     * should be used, specifying explicitly an uninitialized model. 
     *</p>
     * @param id The component id.
     */
    public CRSPanel(String id) {
        super(id);
        initComponents();
    }

    /**
     * Constructs the CRS panel with an explicit model.
     *
     * @param id The component id.
     * @param model The model, usually a {@link PropertyModel}.
     */
    public CRSPanel(String id, IModel model) {
        super(id, model);
        initComponents();
    }
    
    /**
     * Constructs the CRS panel specifying the underlying CRS explicitly.
     * <p>
     * When this constructor is used the {@link #getCRS()} method should be used 
     * after the form is submitted to retrieve the final value of the CRS.
     * </p>
     * @param id The component id.
     * @param crs The underlying CRS object.
     */
    public CRSPanel(String id, CoordinateReferenceSystem crs ) {
        //JD: while the CoordinateReferenceSystem interface does not implement Serializable
        // all the CRS objects we use do, hence the cast
        super(id, new Model((Serializable) crs));
        initComponents();
        setConvertedInput(crs);
    }
    
    /*
     * helper for internally creating the panel. 
     */
    void initComponents() {
            
        popupWindow = new ModalWindow("popup");
        add( popupWindow );
        
        srsTextField = new TextField( "srs", new Model() );
        add( srsTextField );
        srsTextField.setOutputMarkupId( true );
        
        findLink = new AjaxLink( "find" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                popupWindow.setInitialHeight( 375 );
                popupWindow.setInitialWidth( 525 );
                popupWindow.setContent(srsListPanel());
                popupWindow.show(target);
            }
        };
        add(findLink);
        
        AjaxLink wktLink = new AjaxLink( "wkt" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                popupWindow.setInitialHeight( 275 );
                popupWindow.setInitialWidth( 425 );
                popupWindow.setContent(new WKTPanel( popupWindow.getContentId(), getCRS()));
                popupWindow.show(target);
            }
        };
        add(wktLink);
        
        wktLabel = new Label( "wktLabel", new Model());
        wktLink.add( wktLabel );
        wktLabel.setOutputMarkupId( true );
    }
    
    @Override
    protected void onBeforeRender() {
        Object value = getModelObject();
        
        if ( value != null ) {
            CoordinateReferenceSystem crs = null;
            if ( value instanceof CoordinateReferenceSystem ) {
                crs = (CoordinateReferenceSystem) value;
            }
            else if ( value instanceof String ) {
                String s = (String) value;
                crs = fromSRS( s );
                if ( crs == null ) {
                    //try as wkt
                    crs = fromWKT( s );
                }
            }
            
            if ( crs != null ) {
                srsTextField.setModelObject( toSRS(crs) );
                wktLabel.setModelObject( crs.getName().toString() );    
            }
        }
        
        super.onBeforeRender();
    }
    
    @Override
    protected void convertInput() {
        String srs = srsTextField.getModelObjectAsString();
        CoordinateReferenceSystem crs = null;
        if ( srs != null ) {
            if ( "UNKNOWN".equals( srs ) ) {
                //leave underlying crs unchanged
                if ( getModelObject() instanceof CoordinateReferenceSystem ) {
                    setConvertedInput(getModelObject());
                }
                return;
            }
            crs = fromSRS( srs );
        }
        setConvertedInput( crs );
    }
    
    /**
     * Sets the panel to be read only.
     */
    public CRSPanel setReadOnly( boolean readOnly ) {
        srsTextField.setEnabled( !readOnly );
        findLink.setVisible( !readOnly );
        return this;
    }
    
    /**
     * Returns the underlying CRS for the panel.
     * <p>
     * This method is convenience for:
     * <pre>
     * (CoordinateReferenceSystem) this.getModelObject();
     * </pre>
     * </p>
     */
    public CoordinateReferenceSystem getCRS() {
        convertInput();
        return (CoordinateReferenceSystem) getConvertedInput();
    }
    
    /*
     * Goes from CRS to SRS. 
     */
    String toSRS( CoordinateReferenceSystem crs ) {
        try {
            Integer epsgCode = CRS.lookupEpsgCode(crs, false);
            return epsgCode != null ? "EPSG:" + epsgCode : "UNKNOWN";
        } 
        catch (Exception e) {
            return null;
        }
    }
    
    /*
     * Goes from SRS to CRS.
     */
    CoordinateReferenceSystem fromSRS( String srs ) {
        try {
            return CRS.decode( srs );
        } 
        catch (Exception e) {
            return null;
        }
    }
    
    /*
     * Goes from WKT to CRS.
     */
    CoordinateReferenceSystem fromWKT( String wkt ) {
        try {
            return CRS.parseWKT( wkt );
        }
        catch(Exception e) {
            return null;
        }
    }
    
    /*
     * Builds the srs list panel component.
     */
    SRSListPanel srsListPanel() {
        return new SRSListPanel(popupWindow.getContentId(),10) {
            @Override
            protected AbstractLink createLinkForCode(String linkId, final String epsgCode) {
                return new AjaxLink(linkId) {
                    @Override
                    public void onClick(AjaxRequestTarget target) {
                        popupWindow.close(target);
                        
                        String srs =  "EPSG:" + epsgCode ;
                        srsTextField.setModelObject( srs );
                        target.addComponent( srsTextField );
                        
                        CoordinateReferenceSystem crs = fromSRS( srs );
                        wktLabel.setModelObject( crs.getName().toString() );
                        target.addComponent( wktLabel );
                    }
                };
            }
        };
    }
    
    /*
     * Panel for displaying the well known text for a CRS.
     */
    static class WKTPanel extends Panel {

        public WKTPanel(String id, CoordinateReferenceSystem crs) {
            super(id);
            
            TextArea wktTextArea = new TextArea("wkt");
            wktTextArea.setOutputMarkupId(true);
            wktTextArea.setEnabled( false );
            
            add( wktTextArea );
            
            if ( crs != null ) {
                wktTextArea.setModel( new Model( crs.toWKT() ) );
            }
        }
    }
}
