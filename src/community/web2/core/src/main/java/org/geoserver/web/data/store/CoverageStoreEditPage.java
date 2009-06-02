/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import java.io.IOException;
import java.util.logging.Level;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.catalog.ResourcePool;
import org.geoserver.web.wicket.GeoServerDialog;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * Supports coverage store configuration
 * 
 * @author Andrea Aime
 */
public class CoverageStoreEditPage extends AbstractCoverageStorePage {

    /**
     * Dialog to ask for save confirmation in case the store can't be reached
     */
    private GeoServerDialog dialog;

    /**
     * 
     * @param storeId
     *            the store id
     */
    public CoverageStoreEditPage(final String storeId) throws IllegalArgumentException {
        Catalog catalog = getCatalog();
        CoverageStoreInfo store = catalog.getCoverageStore(storeId);
        if (store == null) {
            throw new IllegalArgumentException("Cannot find coverage store " + storeId);
        }

        dialog = new GeoServerDialog("dialog");
        add(dialog);
        initUI(store);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected final void onSave(final CoverageStoreInfo info, final AjaxRequestTarget requestTarget)
            throws IllegalArgumentException {

        if (null == info.getType()) {
            throw new IllegalArgumentException("Coverage type has not been set");
        }

        final Catalog catalog = getCatalog();
        final ResourcePool resourcePool = catalog.getResourcePool();
        resourcePool.clear(info);

        // Map<String, Serializable> connectionParameters = info.getConnectionParameters();

        if (info.isEnabled()) {
            // store's enabled, make sure it works
            LOGGER.finer("Store " + info.getName() + " is enabled, verifying factory availability "
                    + "before saving it...");
            AbstractGridFormat gridFormat = resourcePool.getGridCoverageFormat(info);

            if (gridFormat == null) {
                throw new IllegalArgumentException(
                        "No grid format found capable of connecting to the provided URL."
                                + " To save the store disable it, and check the required libraries are in place");
            }
            try {
                // get the reader through ResourcePool so it resolves relative URL's for us
                GridCoverageReader reader = resourcePool.getGridCoverageReader(info, null);
                LOGGER.info("Connection to store " + info.getName() + " validated. Got a "
                        + reader.getClass().getName() + ". Saving store");
                doSaveStore(info);
                setResponsePage(StorePage.class);
            } catch (IOException e) {
                confirmSaveOnConnectionFailure(info, requestTarget, e);
            } catch (RuntimeException e) {
                confirmSaveOnConnectionFailure(info, requestTarget, e);
            }
        } else {
            // store's disabled, no need to check for availability
            doSaveStore(info);
            setResponsePage(StorePage.class);
        }
    }

    @SuppressWarnings("serial")
    private void confirmSaveOnConnectionFailure(final CoverageStoreInfo info,
            final AjaxRequestTarget requestTarget, final Exception error) {
        final String exceptionMessage = error.getMessage();

        dialog.showOkCancel(requestTarget, new GeoServerDialog.DialogDelegate() {

            boolean accepted = false;

            @Override
            protected Component getContents(String id) {
                return new StoreConnectionFailedInformationPanel(id, info.getName(),
                        exceptionMessage);
            }

            @Override
            protected boolean onSubmit(AjaxRequestTarget target) {
                doSaveStore(info);
                accepted = true;
                return true;
            }

            @Override
            protected boolean onCancel(AjaxRequestTarget target) {
                return true;
            }

            @Override
            public void onClose(AjaxRequestTarget target) {
                if (accepted) {
                    setResponsePage(StorePage.class);
                }
            }
        });
    }

    private void doSaveStore(final CoverageStoreInfo info) {
        try {
            Catalog catalog = getCatalog();
            ResourcePool resourcePool = catalog.getResourcePool();
            resourcePool.clear(info);
            catalog.save(info);
            LOGGER.finer("Saved store " + info.getName());
        } catch (RuntimeException e) {
            LOGGER.log(Level.WARNING, "Saving the store for " + info.getURL(), e);
            throw new IllegalArgumentException("Unable to save the store: " + e.getMessage());
        }
    }
}
