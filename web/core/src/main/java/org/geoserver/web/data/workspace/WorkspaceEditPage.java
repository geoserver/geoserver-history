/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.workspace;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.UrlValidator;
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
import org.geoserver.web.wicket.URIValidator;
import org.geotools.util.logging.Logging;

/**
 * Allows editing a specific workspace
 */
@SuppressWarnings("serial")
public class WorkspaceEditPage extends GeoServerSecuredPage {

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.data.workspace");
    
    IModel wsModel;
    IModel nsModel;
    
    public WorkspaceEditPage( WorkspaceInfo ws ) {
        
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
        //name.setEnabled(false);
        form.add(name);
        TextField uri = new TextField("uri", new PropertyModel(nsModel, "uRI"));
        uri.add(new URIValidator());
        form.add(uri);
        
        //stores
//        StorePanel storePanel = new StorePanel("storeTable", new StoreProvider(ws), false);
//        form.add(storePanel);
        
        form.add(new SubmitLink("save"));
        form.add(new BookmarkablePageLink("cancel", WorkspacePage.class));
        
     
    }
    
    private void saveWorkspace() {
        final Catalog catalog = getCatalog();

        NamespaceInfo namespaceInfo = (NamespaceInfo) nsModel.getObject();
        WorkspaceInfo workspaceInfo = (WorkspaceInfo) wsModel.getObject();
        
        //sync up workspace name with namespace prefix, temp measure
        namespaceInfo.setPrefix(workspaceInfo.getName());
        
        DataStoreNamespaceUpdatingListener listener = new DataStoreNamespaceUpdatingListener(
                workspaceInfo, catalog);
        catalog.addListener(listener);

        try {
            catalog.save(workspaceInfo);
            catalog.save(namespaceInfo);
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
