package org.geoserver.wms.web.data;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.workspace.WorkspacePage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

public class StylePage extends GeoServerSecuredPage {

    //FeedbackPanel feedbackPanel;
    
    public StylePage() {
        //add( feedbackPanel = new FeedbackPanel( "feedback") );
        //feedbackPanel.setOutputMarkupId( true );
        
        StyleProvider provider = new StyleProvider();
        add( new GeoServerTablePanel<StyleInfo>("table", provider ) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<StyleInfo> property) {
                
                if ( property == StyleProvider.NAME ) {
                    return styleLink( id, itemModel );
                }
                if ( property == StyleProvider.REMOVE ) {
                    return removeStyleLink( id, itemModel );
                }
                
                return null;
            }
            
        });
        
        add( new AjaxLink( "new" ) {
            @Override
            public void onClick(AjaxRequestTarget target) { 
                setResponsePage(StyleNewPage.class);
            }
        });
    }
    
    Component styleLink( String id, IModel model ) {
        return new SimpleAjaxLink( id, StyleProvider.NAME.getModel(model) ) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String sid = getModelObjectAsString();
                StyleInfo style = getCatalog().getStyle( sid );
                setResponsePage( new StyleEditPage( style ) );
            }
        };
    }
    
    Component removeStyleLink( String id, IModel model ) {
        final StyleInfo style = (StyleInfo) model.getObject();
        StringBuilder sb = new StringBuilder();
        sb.append( "Are sure you sure you want to remove style " ).append( style.getName() ).append("?");
        
        return new ConfirmationAjaxLink( id, null, "remove", sb.toString() ) {
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
