/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
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

    /** the form */
    Form form;
    
    /** pop-up window for WKT and SRS list */
    ModalWindow popupWindow;
    
    /** srs/epsg code text field */
    TextField srsTextField;
    
    /** lookup link */
    AjaxLink lookupLink;
    
    /** find link */
    AjaxLink findLink;
    
    public CRSPanel(String id, CoordinateReferenceSystem crs) {
        super(id, new CRSModel( crs ) );
        
        popupWindow = new ModalWindow("popup");
        add( popupWindow );
        
        form = new Form("form");
        add(form);
        
        srsTextField = new TextField( "srs", new Model() );
        form.add( srsTextField );
        srsTextField.setOutputMarkupId( true );
        
        AjaxLink wktLink = new AjaxLink( "wkt" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                //popupWindow.setContent(new Label(popupWindow.getContentId(), crs.toWKT()));
                popupWindow.setInitialHeight( 275 );
                popupWindow.setInitialWidth( 425 );
                popupWindow.setContent(new WKTPanel( popupWindow.getContentId(), getCRS()));
                popupWindow.show(target);
            }
        };
        form.add(wktLink);
        
        lookupLink = new AjaxLink( "lookup" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String srs = toSRS(getCRS(), true);
                if ( srs != null ) {
                    srsTextField.setModelObject( srs );
                    target.addComponent( srsTextField );
                }
            }
        };
        form.add( lookupLink );
        
        findLink = new AjaxLink( "find" ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                popupWindow.setInitialHeight( 375 );
                popupWindow.setInitialWidth( 525 );
                popupWindow.setContent(srsListPanel());
                popupWindow.show(target);
            }
        };
        form.add(findLink);
        
        updateInternal(toSRS(crs, false), false);
    }
    
    /**
     * Sets the panel to be read only.
     */
    public CRSPanel setReadOnly( boolean readOnly ) {
        srsTextField.setEnabled( !readOnly );
        lookupLink.setVisible( !readOnly );
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
        return (CoordinateReferenceSystem) getModelObject(); 
    }
    
    @Override
    public void updateModel() {
        form.process();
        String srs = srsTextField.getValue();
        if ( !( srs == null || "".equals( srs ) || "UNKNOWN".equals( srs ) ) ) {
            updateInternal( srs, true );
        }
    }
    
    /*
     * Updates the text field and optionally the internal model. 
     */
    void updateInternal(String srs, boolean updateCRS) {
        if ( srs == null ) {
            srs = "UNKNOWN";
        }
        srsTextField.setModelObject( srs );
        
        if ( updateCRS ) {
            if ( !"UNKNOWN".equals( srs ) ) {
                setModelObject(fromSRS( srs ));
            }
            else {
                setModelObject(null); 
            }
            
        }
    }
    
    /*
     * Goes from CRS to SRS. 
     */
    String toSRS( CoordinateReferenceSystem crs, boolean fullScan ) {
        try {
            Integer epsgCode = CRS.lookupEpsgCode(crs, fullScan);
            return epsgCode != null ? "EPSG:" + epsgCode : null;
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
                        
                        updateInternal("EPSG:" + epsgCode,true);
                        target.addComponent( srsTextField );
                    }
                };
            }
        };
    }
    
    /*
     * Panel for displaying the well known text for a CRS.
     */
    static class WKTPanel extends Panel {

        CoordinateReferenceSystem crs;
        TextArea wktTextArea;
        
        public WKTPanel(String id, CoordinateReferenceSystem crs) {
            super(id);
            this.crs = crs;
            
            wktTextArea = new TextArea("wkt",new Model(crs.toWKT()));
            wktTextArea.setOutputMarkupId(true);
            
            add( wktTextArea );
        }
    }
}
