/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.catalog.event.CatalogAddEvent;
import org.geoserver.catalog.event.CatalogListener;
import org.geoserver.catalog.event.CatalogModifyEvent;
import org.geoserver.catalog.event.CatalogPostModifyEvent;
import org.geoserver.catalog.event.CatalogRemoveEvent;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.namespace.NamespaceDetachableModel;
import org.geoserver.web.wicket.ParamResourceModel;
import org.geoserver.web.wicket.URIValidator;
import org.geoserver.web.wicket.XMLNameValidator;
import org.geotools.util.logging.Logging;

/**
 * Allows editing a specific workspace
 */
@SuppressWarnings("serial")
public class WorkspaceEditPage extends GeoServerSecuredPage {

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.data.workspace");
    
    IModel wsModel;
    IModel nsModel;
    boolean defaultWs;
    
    /**
     * Uses a "name" parameter to locate the workspace
     * @param parameters
     */
    public WorkspaceEditPage(PageParameters parameters) {
        String wsName = parameters.getString("name");
        WorkspaceInfo wsi = getCatalog().getWorkspaceByName(wsName);
        
        if(wsi == null) {
            error(new ParamResourceModel("WorkspaceEditPage.notFound", this, wsName).getString());
            setResponsePage(WorkspacePage.class);
            return;
        }
        
        init(wsi);
    }
    
    public WorkspaceEditPage(WorkspaceInfo ws) {
        init(ws);
    }
    
    private void init(WorkspaceInfo ws) {
        defaultWs = ws.getId().equals(getCatalog().getDefaultWorkspace().getId());
        
        wsModel = new WorkspaceDetachableModel( ws );

        NamespaceInfo ns = getCatalog().getNamespaceByPrefix( ws.getName() );
        nsModel = new NamespaceDetachableModel(ns);
        
        Form form = new Form( "form", new CompoundPropertyModel( nsModel ) ) {
            protected void onSubmit() {
                try {
                    saveWorkspace();
                } catch (RuntimeException e) {
                    error(e.getMessage());
                }
            }
        };
        add(form);
        TextField name = new TextField("name", new PropertyModel(wsModel, "name"));
        name.setRequired(true);
        name.add(new XMLNameValidator());
        form.add(name);
        TextField uri = new TextField("uri", new PropertyModel(nsModel, "uRI"), String.class);
        uri.setRequired(true);
        uri.add(new URIValidator());
        form.add(uri);
        CheckBox defaultChk = new CheckBox("default", new PropertyModel(this, "defaultWs"));
        form.add(defaultChk);
        
        //stores
//        StorePanel storePanel = new StorePanel("storeTable", new StoreProvider(ws), false);
//        form.add(storePanel);
        
        SubmitLink submit = new SubmitLink("save");
        form.add(submit);
        form.setDefaultButton(submit);
        form.add(new BookmarkablePageLink("cancel", WorkspacePage.class));
    }

    private void saveWorkspace() {
        final Catalog catalog = getCatalog();

        NamespaceInfo namespaceInfo = (NamespaceInfo) nsModel.getObject();
        WorkspaceInfo workspaceInfo = (WorkspaceInfo) wsModel.getObject();
        
        //sync up workspace name with namespace prefix, temp measure
        namespaceInfo.setPrefix(workspaceInfo.getName());
        
        // this will ensure all datastore namespaces are updated when the workspace is modified
        DataStoreNamespaceUpdatingListener listener = new DataStoreNamespaceUpdatingListener(
                workspaceInfo, catalog);
        catalog.addListener(listener);

        try {
            catalog.save(workspaceInfo);
            catalog.save(namespaceInfo);
            if(defaultWs)
                catalog.setDefaultWorkspace(workspaceInfo);
        } finally {
            catalog.removeListener(listener);
        }
        setResponsePage(WorkspacePage.class);
    }

    /**
     * Listens to Catalog's namespace URI modifications and updates the "namespace"
     * {@link DataStoreInfo#getConnectionParameters() connection parameter} for all the datastores
     * configured for the workspace
     */
    private static final class DataStoreNamespaceUpdatingListener implements CatalogListener {

        private final WorkspaceInfo workspaceInfo;

        private final Catalog catalog;

        public DataStoreNamespaceUpdatingListener(final WorkspaceInfo workspaceInfo,
                final Catalog catalog) {
            this.workspaceInfo = workspaceInfo;
            this.catalog = catalog;
        }

        /**
         * @see CatalogListener#handlePostModifyEvent(CatalogPostModifyEvent)
         */
        public void handlePostModifyEvent(CatalogPostModifyEvent event) {
            if (event.getSource() instanceof NamespaceInfo) {
                LOGGER.info("Updating namespace parameter for all DataStoreInfo"
                        + " objects in workspace " + workspaceInfo.getName());

                final NamespaceInfo nsInfo = (NamespaceInfo) event.getSource();
                String namespaceURI = nsInfo.getURI();

                List<DataStoreInfo> stores = catalog.getDataStoresByWorkspace(workspaceInfo);
                if (stores.size() > 0) {
                    for (DataStoreInfo store : stores) {
                        if (store.getConnectionParameters().containsKey("namespace")) {
                            store.getConnectionParameters().put("namespace", namespaceURI);
                            LOGGER.info("Setting namespace for store " + store.getName()
                                    + " to be " + namespaceURI);
                            catalog.save(store);
                        }
                    }
                    LOGGER.info("namespace parameter for " + stores.size()
                            + " stores in workspace " + workspaceInfo.getName()
                            + " successfully updated");
                } else {
                    LOGGER.info("No stores in workspace " + workspaceInfo.getName());
                }
            }
        }

        /**
         * Ignored event
         * 
         * @see CatalogListener#handleModifyEvent(CatalogModifyEvent)
         */
        public void handleModifyEvent(CatalogModifyEvent event) {
            // ignore
        }

        /**
         * Ignored event
         * 
         * @see CatalogListener#handleRemoveEvent(CatalogRemoveEvent)
         */
        public void reloaded() {
            // ignore
        }

        /**
         * Ignored event
         * 
         * @see CatalogListener#handleRemoveEvent(CatalogRemoveEvent)
         */
        public void handleRemoveEvent(CatalogRemoveEvent event) {
            // ignore
        }

        /**
         * Ignored event
         * 
         * @see CatalogListener#handleAddEvent(CatalogAddEvent)
         */
        public void handleAddEvent(CatalogAddEvent event) {
            // ignore
        }
    }

}
