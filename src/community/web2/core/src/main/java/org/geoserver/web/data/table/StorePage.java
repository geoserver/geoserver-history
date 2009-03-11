/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.table;

import static org.geoserver.web.data.table.StoreProvider.ENABLED;
import static org.geoserver.web.data.table.StoreProvider.NAME;
import static org.geoserver.web.data.table.StoreProvider.TYPE;
import static org.geoserver.web.data.table.StoreProvider.WORKSPACE;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.NamespaceEditPage;
import org.geoserver.web.data.NewDataPage;
import org.geoserver.web.data.datastore.DataStoreConfiguration;
import org.geoserver.web.data.table.GSDataProvider.Property;

/**
 * Page listing all the available stores. Follows the usual filter/sort/page approach,
 * provides ways to bulk delete stores and to add new ones
 * @author Andrea Aime - OpenGeo
 */
public class StorePage extends GeoServerSecuredPage {
    StoreProvider provider = new StoreProvider();
    ModalWindow popupWindow;
    

    public StorePage() {
        // the popup window for messages
        popupWindow = new ModalWindow("popupWindow");
        add(popupWindow);
        
        add(new GSTablePanel<StoreInfo>("table", provider) {

            @Override
            protected Component getComponentForProperty(String id, IModel itemModel,
                    Property<StoreInfo> property) {
                if(property == TYPE) {
                    return new Label(id, TYPE.getModel(itemModel));
                } else if(property == WORKSPACE) {
                    return workspaceLink(id, itemModel);
                } else if(property == NAME) {
                    return storeNameLink(id, itemModel);
                } else if(property == ENABLED) {
                    return new Label(id, ENABLED.getModel(itemModel));
                } 
                throw new IllegalArgumentException("Don't know a property named " + property.getName());
            }
            
        });
        
        // the workspaces drop down
        add(workspacesDropDown());
    }

    private DropDownChoice workspacesDropDown() {
        final DropDownChoice workspaces;
        workspaces = new DropDownChoice("wsDropDown", new Model(), new WorkspacesModel());
        workspaces.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                String wsName = workspaces.getModelObjectAsString();
                setResponsePage(new NewDataPage(wsName));
            }
        });
        return workspaces;
    }


    private Component storeNameLink(String id, final IModel itemModel) {
        return new SimpleAjaxLink(id, NAME.getModel(itemModel)) {
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

    private Component workspaceLink(String id, IModel itemModel) {
        return new SimpleAjaxLink(id,WORKSPACE.getModel(itemModel)) {
            public void onClick(AjaxRequestTarget target) {
                WorkspaceInfo info = getCatalog().getWorkspaceByName(getModelObjectAsString());
                if (info != null)
                    setResponsePage(new NamespaceEditPage(info.getId()));
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
