package org.geoserver.web.data.layergroup;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

public class LayerGroupPage extends GeoServerSecuredPage {

    public LayerGroupPage() {
        LayerGroupProvider provider = new LayerGroupProvider();
        add( new GeoServerTablePanel<LayerGroupInfo>( "table", provider ) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<LayerGroupInfo> property) {
                
                if ( property == LayerGroupProvider.NAME ) {
                    return layerGroupLink( id, itemModel ); 
                }
                if ( property == LayerGroupProvider.REMOVE ) {
                    return removeLayerGroupLink( id, itemModel );
                }
                
                return null;
            }
        });
        
        add(new AjaxLink("add") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(LayerGroupNewPage.class);
            }
        });
    }
    
    Component layerGroupLink(String id, IModel itemModel) {
        return new SimpleAjaxLink(id, LayerGroupProvider.NAME.getModel(itemModel)) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String lgid = getModelObjectAsString();
                LayerGroupInfo lg = getCatalog().getLayerGroup( lgid );
                setResponsePage( new LayerGroupEditPage( lg ) );
            }
        };
    }
    
    Component removeLayerGroupLink(String id, IModel itemModel) {
        final LayerGroupInfo layerGroup = (LayerGroupInfo) itemModel.getObject();
        
        StringBuilder sb = new StringBuilder();
        sb.append( "Are sure you sure want to remove the layer group " )
            .append( layerGroup.getName() ).append( "?");
        
        return new ConfirmationAjaxLink( id, null, "remove", sb.toString() ) {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                try {
                    getCatalog().remove( layerGroup );
                    setResponsePage( LayerGroupPage.this );
                }
                catch( Exception e ) {
                    LayerGroupPage.this.error( e );
                    target.addComponent( feedbackPanel );
                }
            }
        };
    }
}
