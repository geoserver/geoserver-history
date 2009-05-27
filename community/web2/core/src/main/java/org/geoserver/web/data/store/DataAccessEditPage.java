/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import java.io.Serializable;
import java.util.logging.Level;

import org.apache.wicket.markup.html.form.Form;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geotools.data.DataAccess;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Provides a form to edit a geotools {@link DataAccess} that already exists in the {@link Catalog}
 * 
 * @author Gabriel Roldan
 */
public class DataAccessEditPage extends AbstractDataAccessPage implements Serializable {

    /**
     * Creates a new datastore configuration page to edit the properties of the given data store
     * 
     * @param dataStoreInfoId
     *            the datastore id to modify, as per {@link DataStoreInfo#getId()}
     */
    public DataAccessEditPage(final String dataStoreInfoId) throws IllegalArgumentException {
        final Catalog catalog = getCatalog();
        final DataStoreInfo dataStoreInfo = catalog.getDataStore(dataStoreInfoId);

        if (null == dataStoreInfo) {
            throw new IllegalArgumentException("DataStore " + dataStoreInfoId + " not found");
        }

        initUI(dataStoreInfo);
    }

    /**
     * Callback method called when the submit button have been hit and the parameters validation has
     * succeed.
     * 
     * @param paramsForm
     *            the form to report any error to
     * @see AbstractDataAccessPage#onSaveDataStore(Form)
     */
    protected final void onSaveDataStore(final DataStoreInfo info) {
        final Catalog catalog = getCatalog();

        final String dataStoreInfoId = info.getId();

        catalog.getResourcePool().clear(info);

        // get the original values to use as rollback...
        final DataStoreInfo original = catalog.getFactory().createDataStore();
        clone(catalog.getDataStore(dataStoreInfoId), original);

        try {
            catalog.save(info);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error saving data store to catalog", e);
            throw new IllegalArgumentException("Error saving data store:" + e.getMessage());
        }

        // try and grab the datastore with the new configuration
        // parameters...
        try {
            DataAccess<? extends FeatureType, ? extends Feature> dataStore;
            dataStore = info.getDataStore(new NullProgressListener());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error obtaining datastore with the modified values", e);
            catalog.getResourcePool().clear(info);

            // roll back..
            DataStoreInfo saved = catalog.getDataStore(dataStoreInfoId);
            clone(original, saved);
            catalog.save(saved);

            String message = e.getMessage();
            if (message == null && e.getCause() != null) {
                message = e.getCause().getMessage();
            }
            throw new IllegalArgumentException("Error updating data store parameters: " + message);
        }

        setResponsePage(StorePage.class);
    }

}
