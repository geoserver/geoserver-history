/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.table;

import static org.geoserver.web.data.table.StoreProvider.*;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.NamespaceEditPage;
import org.geoserver.web.data.NewDataPage;
import org.geoserver.web.data.coverage.CoverageStoreConfiguration;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.geoserver.web.wicket.ConfirmationAjaxLink;
import org.geoserver.web.wicket.GSTablePanel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.WorkspaceChoice;
import org.geoserver.web.wicket.GSDataProvider.Property;

/**
 * Page listing all the available stores. Follows the usual filter/sort/page
 * approach, provides ways to bulk delete stores and to add new ones
 * 
 * @author Andrea Aime - OpenGeo
 */
public class StorePage extends GeoServerSecuredPage {
    StoreProvider provider = new StoreProvider();

    ModalWindow popupWindow;

    GSTablePanel<StoreInfo> table;

    public StorePage() {
        // the popup window for messages
        popupWindow = new ModalWindow("popupWindow");
        add(popupWindow);

        table = new GSTablePanel<StoreInfo>("table", provider) {

            @Override
            protected Component getComponentForProperty(String id,
                    IModel itemModel, Property<StoreInfo> property) {
                if (property == TYPE) {
                    return new Label(id, TYPE.getModel(itemModel));
                } else if (property == WORKSPACE) {
                    return workspaceLink(id, itemModel);
                } else if (property == NAME) {
                    return storeNameLink(id, itemModel);
                } else if (property == ENABLED) {
                    return new Label(id, ENABLED.getModel(itemModel));
                } else if (property == REMOVE) {
                    return removeLink(id, itemModel);
                }
                throw new IllegalArgumentException(
                        "Don't know a property named " + property.getName());
            }

        };
        table.setOutputMarkupId(true);
        add(table);

        // the workspaces drop down
        add(workspacesDropDown());
    }

    private DropDownChoice workspacesDropDown() {
        final DropDownChoice workspaces;
        workspaces = new WorkspaceChoice("wsDropDown", new Model(null));
        workspaces.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                WorkspaceInfo ws = (WorkspaceInfo) workspaces.getModelObject();
                setResponsePage(new NewDataPage(ws.getName()));
            }
        });
        return workspaces;
    }

    private Component storeNameLink(String id, final IModel itemModel) {
        return new SimpleAjaxLink(id, NAME.getModel(itemModel)) {
            @Override
            public void onClick(AjaxRequestTarget target) {
                String storeName = getLink().getModelObjectAsString();
                StoreInfo store = getCatalog().getStoreByName(storeName,
                        StoreInfo.class);
                if (store == null) {
                    popupWindow.setContent(new Label(
                            popupWindow.getContentId(),
                            "Cannot find the store " + store
                                    + " anymore, I guess it has been removed. "
                                    + "Will refresh the store list."));
                    popupWindow.show(target);
                    target.addComponent(table);
                } else if (store instanceof DataStoreInfo) {
                    setResponsePage(new DataStoreConfiguration(store.getId()));
                } else {
                    setResponsePage(new CoverageStoreConfiguration(store
                            .getId()));
                }
            }
        };
    }

    private Component workspaceLink(String id, IModel itemModel) {
        return new SimpleAjaxLink(id, WORKSPACE.getModel(itemModel)) {
            public void onClick(AjaxRequestTarget target) {
                WorkspaceInfo info = getCatalog().getWorkspaceByName(
                        getModelObjectAsString());
                if (info != null)
                    setResponsePage(new NamespaceEditPage(info.getId()));
            }
        };
    }

    protected Component removeLink(String id, final IModel itemModel) {
        StoreInfo info = (StoreInfo) itemModel.getObject();
        // TODO: i18n this!
        SimpleAjaxLink linkPanel = new ConfirmationAjaxLink(
                id,
                null,
                new Model("remove"),
                new Model(
                        "About to remove \""
                                + info.getName()
                                + "\". Are you sure? All contained layers will be removed as well")) {
            public void onClick(AjaxRequestTarget target) {
                getCatalog().remove((StoreInfo) itemModel.getObject());
                target.addComponent(table);
            }
        };
        return linkPanel;
    }
}
