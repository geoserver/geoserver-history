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

    public LayerPage() {
        // setup the dialog
        final ModalWindow popupWindow = new ModalWindow("popupWindow");
        add(popupWindow);
        
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
                wsLink.add(new Label("ws", new PropertyModel(model, WORKSPACE_PROPERTY)));
                AjaxLink storeLink = new AjaxLink("storeLink", new PropertyModel(model, "resource.store.id")) {
                    public void onClick(AjaxRequestTarget target) {
                        String storeId = getModelObjectAsString();
                        if(getCatalog().getDataStore(storeId) != null)
                            setResponsePage(new DataStoreConfiguration(storeId));
                        else {
                            popupWindow.setContent(new Label(popupWindow.getContentId(), "Sorry bud, no coverage store editor so far"));
                            popupWindow.show(target);
                        }
                    }
                };
                item.add(storeLink);
                storeLink.add(new Label("store", new PropertyModel(model, STORE_PROPERTY)));
                Link nameLink = new Link("nameLink", new PropertyModel(model, "resource.name")) {
                    public void onClick() {
                        setResponsePage(new ResourceConfigurationPage(getModelObjectAsString()));
                    }
                };
                item.add(nameLink);
                nameLink.add(new Label("name", new PropertyModel(model, NAME_PROPERTY)));
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
        add(removeLink);
        final DropDownChoice stores = new DropDownChoice("storesDropDown", new Model(), new LoadableDetachableModel() {
        
            @Override
            protected Object load() {
                List<DataStoreInfo> stores = getCatalog().getDataStores();
                List<String> storeNames = new ArrayList<String>();
                for (DataStoreInfo store : stores) {
                    storeNames.add(store.getName());
                }
                return storeNames;
            }
        }); 
        add(stores);
        stores.add(new AjaxFormComponentUpdatingBehavior("onchange") {
        
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                popupWindow.setContent(new Label(popupWindow.getContentId(), "Ah, so you wanted to add a layer from " + stores.getModelObjectAsString() + " huh? Well, now go into the code and actually implement the 'add layer' workflow then!"));
                popupWindow.show(target);
            }
        });
    }
}
