/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geotools.data.DataAccess;
import org.geotools.data.DataAccessFactory;
import org.geotools.util.NullProgressListener;
import org.vfny.geoserver.util.DataStoreUtils;

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
    public DataAccessEditPage(final String dataStoreInfoId) {
        final Catalog catalog = getCatalog();
        final DataStoreInfo dataStoreInfo = catalog.getDataStore(dataStoreInfoId);

        if (null == dataStoreInfo) {
            throw new IllegalArgumentException("DataStore " + dataStoreInfoId + " not found");
        }

        Map<String, Serializable> connectionParameters;
        connectionParameters = new HashMap<String, Serializable>(dataStoreInfo
                .getConnectionParameters());
        connectionParameters = DataStoreUtils.getParams(connectionParameters);
        final DataAccessFactory dsFactory = DataStoreUtils.aquireFactory(connectionParameters);

        parametersMap.putAll(connectionParameters);

        parametersMap.put(DATASTORE_ID_PROPERTY, dataStoreInfoId);
        parametersMap.put(WORKSPACE_PROPERTY, dataStoreInfo.getWorkspace());
        parametersMap.put(DATASTORE_NAME_PROPERTY_NAME, dataStoreInfo.getName());
        parametersMap.put(DATASTORE_DESCRIPTION_PROPERTY_NAME, dataStoreInfo.getDescription());
        parametersMap.put(DATASTORE_ENABLED_PROPERTY_NAME, Boolean.valueOf(dataStoreInfo
                .isEnabled()));

        Serializable initError = null;
        if (null == dsFactory) {
            String msg = "Can't get the DataStoreFactory for the given connection parameters";
            initError = new ResourceModel("DataAccessEditPage.cantConnectToDataStore", msg);
        }
        initUI(dsFactory, false, initError);
    }

    /**
     * Callback method called when the submit button have been hit and the parameters validation has
     * succeed.
     * 
     * @param paramsForm
     *            the form to report any error to
     * @see AbstractDataAccessPage#onSaveDataStore(Form)
     */
    protected final void onSaveDataStore(final Form paramsForm) {
        final Catalog catalog = getCatalog();
        final Map<String, Serializable> dsParams = new HashMap<String, Serializable>(parametersMap);
        // may the "namespace" parameter have been handled as a NamespaceInfo instead of a plain
        // string?
        if (dsParams.get(NAMESPACE_PROPERTY) != null) {
            NamespaceInfo ns = (NamespaceInfo) dsParams.get(NAMESPACE_PROPERTY);
            dsParams.put("namespace", ns.getURI());
        }

        DataStoreInfo dataStoreInfo;

        // dataStoreId already validated, so its safe to use
        final String dataStoreInfoId = (String) dsParams.get(DATASTORE_ID_PROPERTY);
        final WorkspaceInfo workspace = (WorkspaceInfo) dsParams.get(WORKSPACE_PROPERTY);
        final String dataStoreUniqueName = (String) dsParams.get(DATASTORE_NAME_PROPERTY_NAME);
        final String description = (String) dsParams.get(DATASTORE_DESCRIPTION_PROPERTY_NAME);
        final Boolean enabled = (Boolean) dsParams.get(DATASTORE_ENABLED_PROPERTY_NAME);

        // it is an existing datastore that's being modified
        dataStoreInfo = catalog.getDataStore(dataStoreInfoId);
        dataStoreInfo.setWorkspace(workspace);
        dataStoreInfo.setName(dataStoreUniqueName);
        dataStoreInfo.setDescription(description);
        dataStoreInfo.setEnabled(enabled.booleanValue());

        Map<String, Serializable> connectionParameters;
        connectionParameters = dataStoreInfo.getConnectionParameters();
        final Map<String, Serializable> oldParams = new HashMap<String, Serializable>(
                connectionParameters);

        connectionParameters.clear();
        connectionParameters.putAll(dsParams);
        connectionParameters.remove(DATASTORE_ID_PROPERTY);
        connectionParameters.remove(WORKSPACE_PROPERTY);
        connectionParameters.remove(DATASTORE_NAME_PROPERTY_NAME);
        connectionParameters.remove(DATASTORE_DESCRIPTION_PROPERTY_NAME);
        connectionParameters.remove(DATASTORE_ENABLED_PROPERTY_NAME);

        catalog.getResourcePool().clear(dataStoreInfo);

        // try and grab the datastore with the new configuration
        // parameters...
        try {
            dataStoreInfo.getDataStore(new NullProgressListener());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error obtaining datastore with the modified values", e);
            catalog.getResourcePool().clear(dataStoreInfo);
            connectionParameters.clear();
            connectionParameters.putAll(oldParams);
            String message = e.getMessage();
            if (message == null && e.getCause() != null) {
                message = e.getCause().getMessage();
            }
            paramsForm.error("Error updating data store parameters: " + message);
            return;
        }

        // it worked, save it
        try {
            catalog.save(dataStoreInfo);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error saving data store to catalog", e);
            paramsForm.error("Error saving data store:" + e.getMessage());
            return;
        }

        setResponsePage(StorePage.class);
    }

}
