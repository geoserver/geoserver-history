/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.markup.html.form.Form;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.data.layer.NewLayerPage;
import org.geotools.data.DataAccess;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.util.NullProgressListener;
import org.vfny.geoserver.util.DataStoreUtils;

/**
 * Provides a form to configure a new geotools {@link DataAccess}
 * 
 * @author Gabriel Roldan
 */
public class DataAccessNewPage extends AbstractDataAccessPage {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new datastore configuration page to create a new datastore of the given type
     * 
     * @param the
     *            workspace to attach the new datastore to, like in {@link WorkspaceInfo#getId()}
     * 
     * @param dataStoreFactDisplayName
     *            the type of datastore to create, given by its factory display name
     */
    public DataAccessNewPage(final String dataStoreFactDisplayName) {
        super();

        final DataStoreFactorySpi dsFact = DataStoreUtils.aquireFactory(dataStoreFactDisplayName);
        if (dsFact == null) {
            throw new IllegalArgumentException("Can't locate a datastore factory named '"
                    + dataStoreFactDisplayName + "'");
        }

        // pre-populate map with default values

        final WorkspaceInfo defaultWs = getCatalog().getDefaultWorkspace();
        if (defaultWs == null) {
            throw new IllegalStateException("No default Workspace configured");
        }
        final NamespaceInfo defaultNs = getCatalog().getDefaultNamespace();
        if (defaultNs == null) {
            throw new IllegalStateException("No default Namespace configured");
        }

        Param[] parametersInfo = dsFact.getParametersInfo();
        for (int i = 0; i < parametersInfo.length; i++) {
            Serializable value;
            final Param param = parametersInfo[i];
            if (param.sample == null || param.sample instanceof Serializable) {
                value = (Serializable) param.sample;
            } else {
                value = String.valueOf(param.sample);
            }

            parametersMap.put(param.key, value);
        }

        parametersMap.put(WORKSPACE_PROPERTY, defaultWs);
        parametersMap.put(DATASTORE_NAME_PROPERTY_NAME, null);
        parametersMap.put(DATASTORE_DESCRIPTION_PROPERTY_NAME, null);
        parametersMap.put(DATASTORE_ENABLED_PROPERTY_NAME, Boolean.TRUE);

        initUI(dsFact, true);
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
        final WorkspaceInfo workspace = (WorkspaceInfo) dsParams.get(WORKSPACE_PROPERTY);
        final String dataStoreUniqueName = (String) dsParams.get(DATASTORE_NAME_PROPERTY_NAME);
        final String description = (String) dsParams.get(DATASTORE_DESCRIPTION_PROPERTY_NAME);
        final Boolean enabled = (Boolean) dsParams.get(DATASTORE_ENABLED_PROPERTY_NAME);

        CatalogFactory factory = catalog.getFactory();
        dataStoreInfo = factory.createDataStore();
        dataStoreInfo.setName(dataStoreUniqueName);
        dataStoreInfo.setWorkspace(workspace);
        dataStoreInfo.setDescription(description);
        dataStoreInfo.setEnabled(enabled.booleanValue());

        Map<String, Serializable> connectionParameters;
        connectionParameters = dataStoreInfo.getConnectionParameters();
        connectionParameters.clear();
        connectionParameters.putAll(dsParams);
        connectionParameters.remove(DATASTORE_NAME_PROPERTY_NAME);
        connectionParameters.remove(DATASTORE_DESCRIPTION_PROPERTY_NAME);
        connectionParameters.remove(DATASTORE_ENABLED_PROPERTY_NAME);

        try {
            dataStoreInfo.getDataStore(new NullProgressListener());
        } catch (IOException e) {
            paramsForm.error("Error creating data store, check the parameters. Error message: "
                    + e.getMessage());
            return;
        }
        try {
            catalog.add(dataStoreInfo);
        } catch (Exception e) {
            paramsForm.error("Error creating data store with the provided parameters: "
                    + e.getMessage());
            return;
        }
        setResponsePage(new NewLayerPage(dataStoreInfo.getId()));
    }

}
