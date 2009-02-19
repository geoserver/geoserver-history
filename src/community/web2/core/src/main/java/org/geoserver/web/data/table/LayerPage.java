package org.geoserver.web.data.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.behavior.SimpleAttributeModifier;
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

    public LayerPage() {
        // setup the table
        final WebMarkupContainer layerContainer = new WebMarkupContainer("listContainer");
        layerContainer.setOutputMarkupId(true);
        add(layerContainer);
        DataView dataView = new DataView("layerList", layers) {

            @Override
            protected void populateItem(Item item) {
                final IModel model = item.getModel();
                
                // odd/even style
                item.add(new SimpleAttributeModifier("class", item.getIndex() % 2 == 0 ? "even"
                        : "odd"));
                
                // build an indirection so that we don't store the actual layer
                // but just the model the item is using, which is detachable
                final AjaxCheckBox selected = new AjaxCheckBox("selected", new Model() {
                    @Override
                    public Object getObject() {
                        LayerInfo li = (LayerInfo) model.getObject();
                        return selection.contains(li.getName());
                    }
                }) {
                    @Override
                    protected void onUpdate(AjaxRequestTarget target) {
                        LayerInfo li = (LayerInfo) model.getObject();
                        if(selection.contains(li.getName()))
                            selection.remove(li.getName());
                        else
                            selection.add(li.getName());
                        target.addComponent(layerContainer);
                    }
                };
                item.add(selected);
                
                // build an indirection so that we don't store the actual layer
                // but just the model the item is using, which is detachable
                Label type = new Label("type", new Model() {
                    @Override
                    public Object getObject() {
                        LayerInfo li = (LayerInfo) model.getObject();
                        return li.getType().toString().toLowerCase(); 
                    }
                });
                item.add(type);
                
                Link wsLink = new Link("wsLink", new PropertyModel(model, "resource.store.workspace.name")) {
                    public void onClick() {
                        setResponsePage(new NamespaceEditPage(getModelObjectAsString()));
                    }
                };
                item.add(wsLink);
                wsLink.add(new Label("ws", new PropertyModel(model, "resource.store.workspace.name")));
                Link storeLink = new Link("storeLink", new PropertyModel(model, "resource.store.id")) {
                    public void onClick() {
                        setResponsePage(new DataStoreConfiguration(getModelObjectAsString()));
                    }
                };
                item.add(storeLink);
                storeLink.add(new Label("store", new PropertyModel(model, "resource.store.name")));
                Link nameLink = new Link("nameLink", new PropertyModel(model, "resource.name")) {
                    public void onClick() {
                        setResponsePage(new ResourceConfigurationPage(getModelObjectAsString()));
                    }
                };
                item.add(nameLink);
                nameLink.add(new Label("name", new PropertyModel(model, "name")));
                item.add(new Label("enabled", new PropertyModel(model, "enabled")));
                item.add(new Label("SRS", new PropertyModel(model, "resource.SRS")));
            }
            
        };
        layerContainer.add(dataView);
        
        // build the filter form
        Form form = new Form("filterForm");
        add(form);
        form.add(filter = new TextField("filter"));
        AjaxButton filterSubmit = new AjaxButton("applyFilter") {

            @Override
            protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
                // do the actual filtering
            }
            
        };
        form.add(filterSubmit);
        AjaxButton filterReset = new AjaxButton("resetFilter") {

            @Override
            protected void onSubmit(AjaxRequestTarget arg0, Form arg1) {
                // do the actual filtering
            }
            
        };
        form.add(filterReset);

        // add the filter match label
        add(matched = new Label("filterMatch"));
        matched.setVisible(false);
        
        // add the paging navigator
        dataView.setItemsPerPage(10);
        add(new GeoServerPagingNavigator("navigator", dataView));
     
        // add the remove link
        AjaxLink removeLink = new AjaxLink("remove") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                // do something
            }
            
        };
        add(removeLink);
        add(new DropDownChoice("storesDropDown", new LoadableDetachableModel() {
        
            @Override
            protected Object load() {
                List<DataStoreInfo> stores = getCatalog().getDataStores();
                List<String> storeNames = new ArrayList<String>();
                for (DataStoreInfo store : stores) {
                    storeNames.add(store.getName());
                }
                return storeNames;
            }
        }) );
    }
}
