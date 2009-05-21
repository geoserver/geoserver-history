/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import org.apache.wicket.markup.html.form.Form;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.web.data.layer.NewLayerPage;

/**
 * Supports coverage store configuration
 * 
 * @author Andrea Aime
 */
public class CoverageStoreEditPage extends AbstractCoverageStorePage {
    Form paramsForm;

    /**
     * 
     * @param storeId
     *            the store id
     */
    public CoverageStoreEditPage(final String storeId) {
        Catalog catalog = getCatalog();
        CoverageStoreInfo store = catalog.getCoverageStore(storeId);
        if (store == null) {
            throw new IllegalArgumentException("Cannot find coverage store " + storeId);
        }

        initUI(store);
    }

    @Override
    protected final void onSave(final CoverageStoreInfo info) {
        final Catalog catalog = getCatalog();

        /**
         * Catalog does not have transactions so.... lets save the previous state just in case
         * saving fails, so we can restore it
         */
        final CoverageStoreInfo originalState = catalog.getFactory().createCoverageStore();
        clone(catalog.getCoverageStore(info.getId()), originalState);

        catalog.save(info);

        final NewLayerPage layerChooserPage;
        try {
            layerChooserPage = new NewLayerPage(info.getId());
        } catch (RuntimeException e) {
            // rollback!
            // save the currently modified state for later use
            CoverageStoreInfo modifiedState = catalog.getFactory().createCoverageStore();
            clone(info, modifiedState);

            // restore info to its original state to be saved... so bad as a rollback but...
            clone(originalState, info);
            // save info as it was before modifying it...
            catalog.save(info);

            // restore the info to its modified state, without saving it, so it can keep being
            // modified
            clone(modifiedState, info);

            // and finally notify the caller the reason of failure
            String originalMessage = "[" + e.getMessage() + "]";
            if (e.getCause() != null && e.getCause() != e && e.getCause().getMessage() != null) {
                originalMessage += ":[" + e.getCause().getMessage() + "]";
            }
            throw new IllegalArgumentException("Error saving the Store: " + originalMessage);
        }
        setResponsePage(layerChooserPage);
    }

}
