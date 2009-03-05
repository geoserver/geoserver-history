package org.geoserver.web.data.table;

import static org.geoserver.web.data.table.StoreProvider.ENABLED_PROPERTY;
import static org.geoserver.web.data.table.StoreProvider.NAME_PROPERTY;
import static org.geoserver.web.data.table.StoreProvider.WORKSPACE_PROPERTY;

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
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.NamespaceEditPage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.geoserver.web.wicket.GeoServerPagingNavigator;

public class StorePage extends GeoServerBasePage {
    TextField filter;

    Label matched;

    StoreProvider stores = new StoreProvider();

    Set<String> selection = new HashSet<String>();

    GeoServerPagingNavigator navigator;

    DataView dataView;

    private ModalWindow popupWindow;

    private WebMarkupContainer tableContainer;

    private AjaxLink removeLink;

    public StorePage() {
        // the popup window used for messages
        popupWindow = new ModalWindow("popupWindow");
        add(popupWindow);

        // the container for the table (for ajax updates)
        tableContainer = new WebMarkupContainer("listContainer");

        // build the filter form
        Form form = new Form("filterForm");
        add(form);
        form.add(filter = new TextField("filter", new Model()));
        filter.setOutputMarkupId(true);
        form.add(filterSubmitButton());
        form.add(filterResetButton());

        // add the filter match label
        add(matched = new Label("filterMatch"));
        matched.setVisible(false);

        // add the link for removing stores
        removeLink = removeLink();
        removeLink.setEnabled(false);
        add(removeLink);

        // the workspaces drop down
        final DropDownChoice workspaces = workspacesDropDown();
        add(workspaces);

        // setup the table
        tableContainer.setOutputMarkupId(true);
        add(tableContainer);
        dataView = new DataView("storesList", stores) {

            @Override
            protected void populateItem(Item item) {
                final IModel model = item.getModel();

                // odd/even style
                item.add(new SimpleAttributeModifier("class", item.getIndex() % 2 == 0 ? "even"
                        : "odd"));

                // build an indirection so that we don't store the actual layer
                // but just the model the item is using, which is detachable
                final AjaxCheckBox selected = selectionCheckBox(model);
                item.add(selected);

                // the store icon (vector/raster)
                Label type = new Label("type", new StoreIconModel(model));
                item.add(type);

                // the workspace link
                PropertyModel wsProperty = new PropertyModel(model, WORKSPACE_PROPERTY);
                Link wsLink = workspaceLink(wsProperty);
                item.add(wsLink);
                wsLink.add(new Label("ws", wsProperty));

                // the store name link
                AjaxLink nameLink = storeNameLink(model);
                item.add(nameLink);
                nameLink.add(new Label("name", new PropertyModel(model, NAME_PROPERTY)));

                // is the store enabled?
                item.add(new Label("enabled", new PropertyModel(model, ENABLED_PROPERTY)));
            }

        };
        tableContainer.add(dataView);

        // add the sorting links
        add(new OrderByBorder("orderType", "type", stores));
        add(new OrderByBorder("orderWs", "workspace", stores));
        add(new OrderByBorder("orderName", "name", stores));
        add(new OrderByBorder("orderEnabled", "enabled", stores));

        // add the paging navigator
        dataView.setItemsPerPage(10);
        navigator = new GeoServerPagingNavigator("navigator", dataView);
        navigator.setOutputMarkupId(true);
        add(navigator);
    }

    private AjaxLink removeLink() {
        return new AjaxLink("remove") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                String content = popupWindow.getContentId();
                if (selection.isEmpty()) {
                    popupWindow.setContent(new Label(content, "Selection is empty, dude!"));
                    popupWindow.show(target);
                } else {
                    String msg = "Ok, so you wanted to remove " + selection
                            + " uh? Well, you have some code to implement before that!!";
                    popupWindow.setContent(new Label(content, msg));
                    popupWindow.show(target);
                }
            }

        };
    }

    private DropDownChoice workspacesDropDown() {
        final DropDownChoice workspaces;
        workspaces = new DropDownChoice("wsDropDown", new Model(), new WorkspacesModel());
        workspaces.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                popupWindow
                        .setContent(new Label(
                                popupWindow.getContentId(),
                                "Ah, so you wanted to add a store into "
                                        + workspaces.getModelObjectAsString()
                                        + " huh? Well, now go into the code and actually implement the 'add layer' workflow then!"));
                popupWindow.show(target);
            }
        });
        return workspaces;
    }

    private AjaxButton filterResetButton() {
        return new AjaxButton("resetFilter") {

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form form) {
                stores.setKeywords(null);
                filter.setModelObject("");
                dataView.setCurrentPage(0);
                target.addComponent(tableContainer);
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
                stores.setKeywords(keywords);
                dataView.setCurrentPage(0);
                target.addComponent(tableContainer);
                target.addComponent(navigator);
            }

        };
    }

    private final class StoreIconModel extends Model {
        private final IModel model;

        private StoreIconModel(IModel model) {
            this.model = model;
        }

        @Override
        public Object getObject() {
            StoreInfo li = (StoreInfo) model.getObject();
            if (li instanceof DataStoreInfo)
                return "vector";
            else
                return "raster";
        }
    }

    private AjaxLink storeNameLink(final IModel model) {
        return new AjaxLink("nameLink", new PropertyModel(model, NAME_PROPERTY)) {
            @Override
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

    private Link workspaceLink(PropertyModel wsProperty) {
        return new Link("wsLink", wsProperty) {
            public void onClick() {
                WorkspaceInfo info = getCatalog().getWorkspaceByName(getModelObjectAsString());
                if (info != null)
                    setResponsePage(new NamespaceEditPage(info.getId()));
            }
        };
    }

    private AjaxCheckBox selectionCheckBox(final IModel model) {
        return new AjaxCheckBox("selected", new Model() {
            @Override
            public Object getObject() {
                StoreInfo si = (StoreInfo) model.getObject();
                return selection.contains(si.getName());
            }
        }) {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                StoreInfo si = (StoreInfo) model.getObject();
                if (selection.contains(si.getName()))
                    selection.remove(si.getName());
                else
                    selection.add(si.getName());
                target.addComponent(tableContainer);

                // update the remove link, enabled, only if there
                // is some selection
                removeLink.setEnabled(selection.size() > 0);
                target.addComponent(removeLink);
            }
        };
    }

    protected class WorkspacesModel extends LoadableDetachableModel {
        @Override
        protected Object load() {
            List<WorkspaceInfo> workspaces = getCatalog().getWorkspaces();
            List<String> wsNames = new ArrayList<String>();
            for (WorkspaceInfo store : workspaces) {
                wsNames.add(store.getName());
            }
            return wsNames;
        }
    }
}
