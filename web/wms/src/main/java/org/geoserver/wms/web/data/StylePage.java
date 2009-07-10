/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.wms.web.data;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.CatalogInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.SelectionRemovalLink;
import org.geoserver.web.data.layer.NewLayerPage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerDialog;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * Page listing all the styles, allows to edit, add, remove styles
 */
@SuppressWarnings("serial")
public class StylePage extends GeoServerSecuredPage {
    
    GeoServerTablePanel<StyleInfo> table;
    
    SelectionRemovalLink removal;

    GeoServerDialog dialog;


    public StylePage() {
        StyleProvider provider = new StyleProvider();
        add(table = new GeoServerTablePanel<StyleInfo>("table", provider, true) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<StyleInfo> property) {
                
                if ( property == StyleProvider.NAME ) {
                    return styleLink( id, itemModel );
                }
                
                return null;
            }
            
            @Override
            protected void onSelectionUpdate(AjaxRequestTarget target) {
                removal.setEnabled(table.getSelection().size() > 0);
                target.addComponent(removal);
            }  
            
        });
        table.setOutputMarkupId(true);
        
        // the confirm dialog
        add(dialog = new GeoServerDialog("dialog"));
        setHeaderPanel(headerPanel());
        
    }
    
    protected Component headerPanel() {
        Fragment header = new Fragment(HEADER_PANEL, "header", this);
        
        // the add button
        header.add(new BookmarkablePageLink("addNew", StyleNewPage.class));
        
        // the removal button
        header.add(removal = new SelectionRemovalLink("removeSelected", table, dialog){
            @Override
            protected StringResourceModel canRemove(CatalogInfo object) {
                StyleInfo s = (StyleInfo) object;
                if ( StyleInfo.DEFAULT_POINT.equals( s.getName() ) || 
                    StyleInfo.DEFAULT_LINE.equals( s.getName() ) || 
                    StyleInfo.DEFAULT_POLYGON.equals( s.getName() ) || 
                    StyleInfo.DEFAULT_RASTER.equals( s.getName() ) ) {
                    return new StringResourceModel("cantRemoveDefaultStyle", StylePage.this, null );
                }
                return null;
            }
        });
        removal.setOutputMarkupId(true);
        removal.setEnabled(false);
        
        return header;
    }

    Component styleLink( String id, IModel model ) {
        return new SimpleAjaxLink( id, StyleProvider.NAME.getModel(model) ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String sid = getModelObjectAsString();
                StyleInfo style = getCatalog().getStyleByName( sid );
                setResponsePage( new StyleEditPage( style ) );
            }
        };
    }
    
    Component removeStyleLink(String id, IModel model) {
        final StyleInfo style = (StyleInfo) model.getObject();

        ResourceModel resRemove = new ResourceModel("removeStyle", "Remove");

        ParamResourceModel confirmRemove = new ParamResourceModel(
                "confirmRemoveStyleX", this, style.getName());

        return new ConfirmationAjaxLink(id, null, resRemove, confirmRemove) {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                try {
                    getCatalog().remove( style );
                    setResponsePage( StylePage.this );    
                }
                catch( Exception e ) {
                    StylePage.this.error( e );
                    target.addComponent( feedbackPanel );
                }
            }
        };
    }
}
