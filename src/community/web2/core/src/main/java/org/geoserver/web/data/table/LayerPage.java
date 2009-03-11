/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.table;

import static org.geoserver.web.data.table.LayerProvider.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.NamespaceEditPage;
import org.geoserver.web.data.ResourceConfigurationPage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GSTablePanel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GSDataProvider.Property;

/**
 * Page listing all the available layers. Follows the usual filter/sort/page approach,
 * provides ways to bulk delete layers and to add new ones
 * @author Andrea Aime - OpenGeo
 */
public class LayerPage extends GeoServerSecuredPage {

    LayerProvider provider = new LayerProvider();
    ModalWindow popupWindow;
    GSTablePanel<LayerInfo> table;

    public LayerPage() {
        // the popup window for messages
        popupWindow = new ModalWindow("popupWindow");
        add(popupWindow);
        
        table = new GSTablePanel<LayerInfo>("table", provider) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<LayerInfo> property) {
                if(property == TYPE) {
                    return new Label(id, TYPE.getModel(itemModel));
                } else if(property == WORKSPACE) {
                    return workspaceLink(id, itemModel);
                } else if(property == STORE) {
                    return storeLink(id, itemModel);
                } else if(property == NAME) {
                    return layerLink(id, itemModel);
                } else if(property == ENABLED) {
                    return new Label(id, ENABLED.getModel(itemModel));
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
        
        // the stores drop down
        final DropDownChoice stores = storesDropDown();
        add(stores);
    }

    private DropDownChoice storesDropDown() {
        final DropDownChoice stores;
        stores = new DropDownChoice("storesDropDown", new Model(), new StoreListModel()); 
        stores.add(new AjaxFormComponentUpdatingBehavior("onchange") {
        
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                popupWindow.setContent(new Label(popupWindow.getContentId(), "Ah, so you wanted to add a layer from " + stores.getModelObjectAsString() + " huh? Well, now go into the code and actually implement the 'add layer' workflow then!"));
                popupWindow.show(target);
            }
        });
        return stores;
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
                DataStoreInfo store = getCatalog().getDataStoreByName(storeName);
                if (store != null)
                    setResponsePage(new DataStoreConfiguration(store.getId()));
                else {
                    popupWindow.setContent(new Label(popupWindow.getContentId(),
                            "Sorry bud, no coverage store editor so far"));
                    popupWindow.show(target);
                }
            }
        };
    }

    private Component workspaceLink(String id, final IModel model) {
        return new SimpleAjaxLink(id, WORKSPACE.getModel(model)) {
            public void onClick(AjaxRequestTarget target) {
                setResponsePage(new NamespaceEditPage(getModelObjectAsString()));
            }
        };
    }
    
    protected Component removeLink(String id, final IModel itemModel) {
        LayerInfo info = (LayerInfo) itemModel.getObject();
        // TODO: i18n this!
        SimpleAjaxLink linkPanel = new ConfirmationAjaxLink(id, null, new Model("remove"),
                new Model("About to remove \"" + info.getName() + "\". Are you sure?")) {
            public void onClick(AjaxRequestTarget target) {
                getCatalog().remove((LayerInfo) itemModel.getObject());
                target.addComponent(table);
            }
        };
        return linkPanel;
    }

    private final class StoreListModel extends LoadableDetachableModel {
        @Override
        protected Object load() {
            List<DataStoreInfo> stores = getCatalog().getDataStores();
            List<String> storeNames = new ArrayList<String>();
            for (DataStoreInfo store : stores) {
                storeNames.add(store.getName());
            }
            return storeNames;
        }
    }


}
