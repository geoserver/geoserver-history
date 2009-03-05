package org.geoserver.web.data.table;

import static org.geoserver.web.data.table.LayerProvider.ENABLED_PROPERTY;
import static org.geoserver.web.data.table.LayerProvider.NAME_PROPERTY;
import static org.geoserver.web.data.table.LayerProvider.SRS_PROPERTY;
import static org.geoserver.web.data.table.LayerProvider.STORE_PROPERTY;
import static org.geoserver.web.data.table.LayerProvider.WORKSPACE_PROPERTY;

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
import org.apache.wicket.model.PropertyModel;
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
    Set<String> selection = new HashSet<String>();
	GeoServerPagingNavigator navigator;
	DataView dataView;
    private ModalWindow popupWindow;
    private WebMarkupContainer layerContainer;
    private AjaxLink removeLink;

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
        
        removeLink = removeLink();
        add(removeLink);
        removeLink.setEnabled(false);
        
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
                
                // build an indirection so that we don't store the actual layer
                // but just the model the item is using, which is detachable
                final AjaxCheckBox selected = selectionCheckbox(model);
                item.add(selected);
                
                // build an indirection so that we don't store the actual layer
                // but just the model the item is using, which is detachable
                Label type = new Label("type", new LayerIconModel(model));
                item.add(type);
                
                // link to workspace
                Link wsLink = workspaceLink(model);
                item.add(wsLink);
                wsLink.add(new Label("ws", new PropertyModel(model, WORKSPACE_PROPERTY)));
                
                // link to container store
                AjaxLink storeLink = storeLink(model);
                item.add(storeLink);
                storeLink.add(new Label("store", new PropertyModel(model, STORE_PROPERTY)));
                
                // link to the layer
                Link nameLink = layerLink(model);
                item.add(nameLink);
                nameLink.add(new Label("name", new PropertyModel(model, NAME_PROPERTY)));
                
                // the srs and enabled properties
                item.add(new Label("enabled", new PropertyModel(model, ENABLED_PROPERTY)));
                item.add(new Label("SRS", new PropertyModel(model, SRS_PROPERTY)));
            }

                        
        };
        layerContainer.add(dataView);
        
        // add the sorting links
        add(new OrderByBorder("orderType", "type", layers));
        add(new OrderByBorder("orderWs", "workspace", layers));
        add(new OrderByBorder("orderStore", "store", layers));
        add(new OrderByBorder("orderName", "name", layers));
        add(new OrderByBorder("orderEnabled", "enabled", layers));
        add(new OrderByBorder("orderSRS", "SRS", layers));
        
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

    private AjaxLink removeLink() {
        return new AjaxLink("remove") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                String content = popupWindow.getContentId();
                if(selection.isEmpty()) {
                    popupWindow.setContent(new Label(content, "Selection is empty, dude!"));
                    popupWindow.show(target);
                } else {
                    String msg = "Ok, so you wanted to remove " + selection + " uh? Well, you have some code to implement before that!!";
                    popupWindow.setContent(new Label(content, msg));
                    popupWindow.show(target);
                }
            }
            
        };
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
        return new Link("nameLink", new PropertyModel(model, NAME_PROPERTY)) {
            public void onClick() {
                setResponsePage(new ResourceConfigurationPage(getModelObjectAsString()));
            }
        };
    }

    private AjaxLink storeLink(final IModel model) {
        return new AjaxLink("storeLink", new PropertyModel(model, STORE_PROPERTY)) {
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
        return new Link("wsLink", new PropertyModel(model, WORKSPACE_PROPERTY)) {
            public void onClick() {
                setResponsePage(new NamespaceEditPage(getModelObjectAsString()));
            }
        };
    }

    private AjaxCheckBox selectionCheckbox(final IModel model) {
        return new AjaxCheckBox("selected", new SelectionModel(model)) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                LayerInfo li = (LayerInfo) model.getObject();
                if(selection.contains(li.getName()))
                    selection.remove(li.getName());
                else
                    selection.add(li.getName());
                target.addComponent(layerContainer);
                
                // update the remove link, enabled, only if there 
                // is some selection
                removeLink.setEnabled(selection.size() > 0);
                target.addComponent(removeLink);
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

    private final class SelectionModel extends Model {
        private final IModel model;

        private SelectionModel(IModel model) {
            this.model = model;
        }

        @Override
        public Object getObject() {
            LayerInfo li = (LayerInfo) model.getObject();
            return selection.contains(li.getName());
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
