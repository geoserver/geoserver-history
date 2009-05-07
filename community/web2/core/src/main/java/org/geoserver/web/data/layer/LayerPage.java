/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.layer;

import static org.geoserver.web.data.layer.LayerProvider.ENABLED;
import static org.geoserver.web.data.layer.LayerProvider.NAME;
import static org.geoserver.web.data.layer.LayerProvider.REMOVE;
import static org.geoserver.web.data.layer.LayerProvider.SRS;
import static org.geoserver.web.data.layer.LayerProvider.STORE;
import static org.geoserver.web.data.layer.LayerProvider.TYPE;
import static org.geoserver.web.data.layer.LayerProvider.WORKSPACE;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.CatalogIconFactory;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.resource.ResourceConfigurationPage;
import org.geoserver.web.data.store.CoverageStoreEditPage;
import org.geoserver.web.data.store.DataAccessEditPage;
import org.geoserver.web.data.workspace.WorkspaceEditPage;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * Page listing all the available layers. Follows the usual filter/sort/page approach,
 * provides ways to bulk delete layers and to add new ones
 */
@SuppressWarnings("serial")
public class LayerPage extends GeoServerSecuredPage {
    LayerProvider provider = new LayerProvider();
    ModalWindow popupWindow;
    GeoServerTablePanel<LayerInfo> table;

    public LayerPage() {
        // the popup window for messages
        popupWindow = new ModalWindow("popupWindow");
        add(popupWindow);
        
        // the action buttons
        add(new BookmarkablePageLink("addNew", NewLayerPage.class));
        add(removeSelectedLink());
        
        final CatalogIconFactory icons = CatalogIconFactory.get();
        table = new GeoServerTablePanel<LayerInfo>("table", provider, true) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<LayerInfo> property) {
                if(property == TYPE) {
                    Fragment f = new Fragment(id, "iconFragment", LayerPage.this);
                    f.add(new Image("layerIcon", icons.getLayerIcon((LayerInfo) itemModel.getObject())));
                    return f;
                } else if(property == WORKSPACE) {
                    return workspaceLink(id, itemModel);
                } else if(property == STORE) {
                    return storeLink(id, itemModel);
                } else if(property == NAME) {
                    return layerLink(id, itemModel);
                } else if(property == ENABLED) {
                    LayerInfo layerInfo = (LayerInfo) itemModel.getObject();
                    ResourceReference icon = layerInfo.isEnabled()? icons.getEnabledIcon() : icons.getDisabledIcon();
                    Fragment f = new Fragment(id, "iconFragment", LayerPage.this);
                    f.add(new Image("layerIcon", icon));
                    return f;
                } else if(property == SRS) {
                    return new Label(id, SRS.getModel(itemModel));
                } else if(property == REMOVE) {
                    return removeLink(id, itemModel);
                }
                throw new IllegalArgumentException("Don't know a property named " + property.getName());
            }
            
        };
        table.setOutputMarkupId(true);
        add(table);
    }

    Component removeSelectedLink() {
        return new AjaxLink("removeSelected") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                System.out.println("IMPLEMENT ME!");
                
            }
            
        };
    }

    private Component layerLink(String id, final IModel model) {
        return new SimpleAjaxLink(id, NAME.getModel(model)) {
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new ResourceConfigurationPage(getModelObjectAsString()));
            }
        };
    }

    private Component storeLink(String id, final IModel model) {
        return new SimpleAjaxLink(id, STORE.getModel(model)) {
            public void onClick(AjaxRequestTarget target) {
                String storeName = getModelObjectAsString();
                StoreInfo store = getCatalog().getStoreByName(storeName, StoreInfo.class);
                if (store instanceof DataStoreInfo)
                    setResponsePage(new DataAccessEditPage(store.getId()));
                else if(store instanceof CoverageStoreInfo)
                    setResponsePage(new CoverageStoreEditPage(store.getId()));
                else
                    throw new RuntimeException("Don't know how to deal with store " + store);
            }
        };
    }

    private Component workspaceLink(String id, final IModel model) {
    	
    	
        return new SimpleAjaxLink(id, WORKSPACE.getModel(model)) {
            public void onClick(AjaxRequestTarget target) {
                WorkspaceInfo ws = getCatalog().getWorkspaceByName(getModelObjectAsString());
                setResponsePage(new WorkspaceEditPage(ws));
            }
        };
    }
    
    protected Component removeLink(String id, final IModel itemModel) {
        LayerInfo info = (LayerInfo) itemModel.getObject();
        
        ResourceModel resRemove = new ResourceModel("removeLayer", "Remove");
        ParamResourceModel confirmRemove = new ParamResourceModel(
                "confirmRemoveLayerX", this, info.getName());
        SimpleAjaxLink linkPanel = new ConfirmationAjaxLink(id, null,
                resRemove, confirmRemove) {
            public void onClick(AjaxRequestTarget target) {
                LayerInfo layer = (LayerInfo) itemModel.getObject();
                // at the moment both layer and resource need to go, 
                // they are always created and removed togheter.
                // When the resource/publish split is done, here we'll
                // delete the layer only
                getCatalog().remove(layer);
                getCatalog().remove(layer.getResource());
                target.addComponent(table);
            }
        };
        return linkPanel;
    }

}
