/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.layergroup;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.LayerGroupInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * Lists layer groups, allows removal and editing
 */
@SuppressWarnings("serial")
public class LayerGroupPage extends GeoServerSecuredPage {

    public LayerGroupPage() {
        LayerGroupProvider provider = new LayerGroupProvider();
        
        add(new AjaxLink("add") {
            @Override
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(LayerGroupNewPage.class);
            }
        });

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
        
    }
    
    Component layerGroupLink(String id, IModel itemModel) {
        return new SimpleAjaxLink(id, LayerGroupProvider.NAME.getModel(itemModel)) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String lgName = getModelObjectAsString();
                LayerGroupInfo lg = getCatalog().getLayerGroupByName( lgName );
                setResponsePage( new LayerGroupEditPage( lg ) );
            }
        };
    }
    
    Component removeLayerGroupLink(String id, IModel itemModel) {
        final LayerGroupInfo layerGroup = (LayerGroupInfo) itemModel
                .getObject();

        ResourceModel resRemove = new ResourceModel(
                "removeLayerGroup","Remove");

        ParamResourceModel confirmRemove = new ParamResourceModel(
                "confirmRemoveLayerGroupX", this, layerGroup.getName());

        return new ConfirmationAjaxLink(id, null, resRemove, confirmRemove) {
            @Override
            protected void onClick(AjaxRequestTarget target) {
                try {
                    getCatalog().remove(layerGroup);
                    setResponsePage(LayerGroupPage.this);
                } catch (Exception e) {
                    LayerGroupPage.this.error(e);
                    target.addComponent(feedbackPanel);
                }
            }
        };
    }
}
