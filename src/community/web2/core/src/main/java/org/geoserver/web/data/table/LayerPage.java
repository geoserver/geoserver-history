/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.table;

import static org.geoserver.web.data.table.LayerProvider.TYPE;
import static org.geoserver.web.data.table.LayerProvider.ENABLED;
import static org.geoserver.web.data.table.LayerProvider.NAME;
import static org.geoserver.web.data.table.LayerProvider.SRS;
import static org.geoserver.web.data.table.LayerProvider.STORE;
import static org.geoserver.web.data.table.LayerProvider.WORKSPACE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.NamespaceEditPage;
import org.geoserver.web.data.ResourceConfigurationPage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.geoserver.web.wicket.GeoServerPagingNavigator;

public class LayerPage extends GeoServerBasePage {

    TextField filter;
    Label matched;
    LayerProvider layers = new LayerProvider();
	GeoServerPagingNavigator navigator;
	DataView dataView;
    private ModalWindow popupWindow;
    private WebMarkupContainer layerContainer;

    public LayerPage() {
        // the popup window for messages
        popupWindow = new ModalWindow("popupWindow");
        add(popupWindow);
        
        // layer container used for ajax-y udpates of the table
        layerContainer = new WebMarkupContainer("listContainer");
        
        // build the filter form
        Form form = new Form("filterForm");
        add(form);
        form.add(filter = new TextField("filter", new Model()));
        filter.setOutputMarkupId(true);
        AjaxButton filterSubmit = filterSubmitButton();
        form.add(filterSubmit);
        AjaxButton filterResetButton = filterResetButton();
        AjaxButton filterReset = filterResetButton;
        form.add(filterReset);

        // add the filter match label
        add(matched = new Label("filterMatch"));
        matched.setVisible(false);
        
        // the stores drop down
        final DropDownChoice stores = storesDropDown();
        add(stores);
        
        // setup the table
        layerContainer.setOutputMarkupId(true);
        add(layerContainer);
        dataView = new DataView("layerList", layers) {

            @Override
            protected void populateItem(Item item) {
                final IModel model = item.getModel();
                
                // odd/even style
                item.add(new SimpleAttributeModifier("class", item.getIndex() % 2 == 0 ? "even"
                        : "odd"));
                
                // the layer type
                Label type = new Label("type", TYPE.getModel(model));
                item.add(type);
                
                // link to workspace
                Link wsLink = workspaceLink(model);
                item.add(wsLink);
                wsLink.add(new Label("ws", WORKSPACE.getModel(model)));
                
                // link to container store
                AjaxLink storeLink = storeLink(model);
                item.add(storeLink);
                storeLink.add(new Label("store", STORE.getModel(model)));
                
                // link to the layer
                Link nameLink = layerLink(model);
                item.add(nameLink);
                nameLink.add(new Label("name", NAME.getModel(model)));
                
                // the srs and enabled properties
                item.add(new Label("enabled", ENABLED.getModel(model)));
                item.add(new Label("SRS", SRS.getModel(model)));
            }

                        
        };
        layerContainer.add(dataView);
        
        // add the sorting links
        layerContainer.add(new OrderByBorder("orderType", "type", layers));
        layerContainer.add(new OrderByBorder("orderWorkspace", "workspace", layers));
        layerContainer.add(new OrderByBorder("orderStore", "store", layers));
        layerContainer.add(new OrderByBorder("orderName", "name", layers));
        layerContainer.add(new OrderByBorder("orderEnabled", "enabled", layers));
        layerContainer.add(new OrderByBorder("orderSRS", "SRS", layers));
        
        // add the paging navigator
        dataView.setItemsPerPage(10);
        navigator = new GeoServerPagingNavigator("navigator", dataView);
        navigator.setOutputMarkupId(true);
		add(navigator);
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

    private AjaxButton filterResetButton() {
        return new AjaxButton("resetFilter") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
            	layers.setKeywords(null);
            	filter.setModelObject("");
                dataView.setCurrentPage(0);
                target.addComponent(layerContainer);
                target.addComponent(navigator);
                target.addComponent(filter);
            }
            
        };
    }

    private AjaxButton filterSubmitButton() {
        return new AjaxButton("applyFilter") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
            	String[] keywords = filter.getModelObjectAsString().split("\\s+");
                layers.setKeywords(keywords);
                dataView.setCurrentPage(0);
                target.addComponent(layerContainer);
                target.addComponent(navigator);
            }
            
        };
    }
    
    private Link layerLink(final IModel model) {
        return new Link("nameLink", NAME.getModel(model)) {
            public void onClick() {
                setResponsePage(new ResourceConfigurationPage(getModelObjectAsString()));
            }
        };
    }

    private AjaxLink storeLink(final IModel model) {
        return new AjaxLink("storeLink", STORE.getModel(model)) {
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

    private Link workspaceLink(final IModel model) {
        return new Link("wsLink", WORKSPACE.getModel(model)) {
            public void onClick() {
                setResponsePage(new NamespaceEditPage(getModelObjectAsString()));
            }
        };
    }

    private final class LayerIconModel extends Model {
        private final IModel model;

        private LayerIconModel(IModel model) {
            this.model = model;
        }

        @Override
        public Object getObject() {
            LayerInfo li = (LayerInfo) model.getObject();
            return li.getType().toString().toLowerCase(); 
        }
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
