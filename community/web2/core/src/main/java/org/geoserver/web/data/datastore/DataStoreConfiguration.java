package org.geoserver.web.data.datastore;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogFactory;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.data.DataPage;
import org.geoserver.web.data.datastore.panel.CheckBoxParamPanel;
import org.geoserver.web.data.datastore.panel.LabelParamPanel;
import org.geoserver.web.data.datastore.panel.PasswordParamPanel;
import org.geoserver.web.data.datastore.panel.TextParamPanel;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataAccessFactory.Param;
import org.vfny.geoserver.util.DataStoreUtils;

/**
 * Provides a form to configure a geotools DataStore
 * 
 * @author Gabriel Roldan
 */
public class DataStoreConfiguration extends GeoServerBasePage {

    private static final String DATASTORE_ID_PROPERTY_NAME = "Wicket_Data_Source_Name";

    /**
     * Holds datastore parameters. Properties will be settled by the form input
     * fields.
     */
    private final Map<String, Serializable> parametersMap;

    /**
     * Creates a new datastore configuration page to edit the properties of the
     * given data store
     * 
     * @param dataStoreInfoId
     *            the datastore id to modify
     */
    public DataStoreConfiguration(final String workspaceId, final String dataStoreInfoId,
            final Map<String, Serializable> connectionParameters) {

        final Catalog catalog = getCatalog();
        if (null == catalog.getWorkspace(workspaceId)) {
            throw new IllegalArgumentException("Workspace " + workspaceId + " not found");
        }
        if (null == catalog.getDataStore(dataStoreInfoId)) {
            throw new IllegalArgumentException("DataStore " + dataStoreInfoId + " not found");
        }

        final DataStoreFactorySpi dsFactory = DataStoreUtils.aquireFactory(connectionParameters);
        if (null == dsFactory) {
            throw new IllegalArgumentException(
                    "Can't get the DataStoreFactory for the given connection parameters");
        }

        parametersMap = new HashMap<String, Serializable>(connectionParameters);
        parametersMap.put(DATASTORE_ID_PROPERTY_NAME, dataStoreInfoId);

        init(workspaceId, dataStoreInfoId, dsFactory);
    }

    /**
     * Creates a new datastore configuration page to create a new datastore of
     * the given type
     * 
     * @param dataStoreFactDisplayName
     *            the type of datastore to create, given by its factory display
     *            name
     */
    public DataStoreConfiguration(final String workspaceId, final String dataStoreFactDisplayName) {
        final DataStoreFactorySpi dsFact = DataStoreUtils.aquireFactory(dataStoreFactDisplayName);
        if (dsFact == null) {
            throw new IllegalArgumentException("Can't locate a datastore factory named '"
                    + dataStoreFactDisplayName + "'");
        }

        parametersMap = new HashMap<String, Serializable>();
        // pre-populate map with default values

        Param[] parametersInfo = dsFact.getParametersInfo();
        for (int i = 0; i < parametersInfo.length; i++) {
            Serializable value;
            if (parametersInfo[i].sample == null
                    || parametersInfo[i].sample instanceof Serializable) {
                value = (Serializable) parametersInfo[i].sample;
            } else {
                value = String.valueOf(parametersInfo[i].sample);
            }
            parametersMap.put(parametersInfo[i].key, value);
        }
        init(workspaceId, null, dsFact);
    }

    /**
     * 
     * @param dataStoreInfoId
     *            the id of the data store to edit, or {@code null} if about to
     *            configure a new data store
     * @param workspaceId
     *            the id for the workspace to attach the new datastore or the
     *            current datastore is attached to
     * @param connectionParameters
     *            the set of connection parameters, may be empty
     * @param dsFactory
     *            the datastore factory to use
     */
    private void init(final String dataStoreInfoId, final String workspaceId,
            final DataStoreFactorySpi dsFactory) {

        final List<ParamInfo> paramsInfo = new ArrayList<ParamInfo>();
        {
            Param[] dsParams = dsFactory.getParametersInfo();
            for (Param p : dsParams) {
                paramsInfo.add(new ParamInfo(p));
            }
        }

        final Form paramsForm = new Form("dataStoreForm");

        add(paramsForm);

        Panel dataStoreIdPanel;
        if (dataStoreInfoId == null) {
            IValidator dsIdValidator = new AbstractValidator() {
                @Override
                protected void onValidate(IValidatable validatable) {
                    String value = (String) validatable.getValue();
                    if (value == null || value.trim().length() == 0) {
                        ValidationError error = new ValidationError();
                        error.setMessage("The Data Source name is mandatory");
                        validatable.error(error);
                        return;
                    }

                    value = value.trim();

                    List<DataStoreInfo> dataStores = getCatalog().getDataStores();
                    for (DataStoreInfo dsi : dataStores) {
                        String name = dsi.getName();
                        if (name.equals(value)) {
                            ValidationError error = new ValidationError();
                            error
                                    .setMessage("The Data Source name '" + value
                                            + "' is already used");
                            validatable.error(error);
                            break;
                        }
                    }
                }
            };
            dataStoreIdPanel = new TextParamPanel("dataStoreIdPanel", parametersMap,
                    DATASTORE_ID_PROPERTY_NAME, "Data Source Name", true, Collections
                            .singletonList(dsIdValidator));
        } else {
            dataStoreIdPanel = new LabelParamPanel("dataStoreIdPanel", parametersMap,
                    DATASTORE_ID_PROPERTY_NAME);
        }

        paramsForm.add(dataStoreIdPanel);

        ListView paramsList = new ListView("parameters", paramsInfo) {
            @Override
            protected void populateItem(ListItem item) {
                ParamInfo parameter = (ParamInfo) item.getModelObject();
                Component inputComponent = getInputComponent("parameterPanel", parametersMap,
                        parameter);
                if (parameter.getTitle() != null) {
                    inputComponent.add(new SimpleAttributeModifier("title", parameter.getTitle()));
                }
                item.add(inputComponent);
            }
        };
        // needed for form components not to loose state
        paramsList.setReuseItems(true);

        paramsForm.add(paramsList);

        paramsForm.add(new BookmarkablePageLink("cancel", DataPage.class));

        paramsForm.add(new Button("submit") {
            @Override
            public void onSubmit() {
                final Catalog catalog = getCatalog();
                final Map<String, Serializable> dsParams = parametersMap;
                // dataStoreId already validated, so its safe to use
                final String dataStoreId = (String) dsParams.get(DATASTORE_ID_PROPERTY_NAME);

                DataStore dataStore = null;
                try {
                    dataStore = DataStoreUtils.getDataStore(parametersMap);
                } catch (IOException e) {
                    paramsForm.error("Error instantiating data source with the provided parameters:\n" + e.getMessage());
                    return;
                }
                if (dataStore == null) {
                    paramsForm
                            .error("Can't create a Vector Data Source with the provided parameters");
                    return;
                }
                CatalogFactory factory = catalog.getFactory();
                DataStoreInfo dataStoreInfo = factory.createDataStore();
                setResponsePage(DataPage.class);
            }
        });

        paramsForm.add(new FeedbackPanel("feedback"));
    }

    /**
     * Creates a form input component for the given datastore param based on its
     * type and metadata properties.
     * 
     * @param param
     * @return
     */
    private Panel getInputComponent(final String componentId, final Map<String, ?> paramsMap,
            final ParamInfo param) {

        final String paramName = param.getName();
        final String paramLabel = param.getName();
        final boolean required = param.isRequired();
        final Class binding = param.getBinding();

        Panel parameterPanel;
        if (Boolean.class == binding) {
            parameterPanel = new CheckBoxParamPanel(componentId, paramsMap, paramName, paramLabel);
        } else if (String.class == binding && param.isPassword()) {
            parameterPanel = new PasswordParamPanel(componentId, paramsMap, paramName, paramLabel);
        } else {
            parameterPanel = new TextParamPanel(componentId, paramsMap, paramName, paramLabel,
                    required, null);
        }
        return parameterPanel;
    }
}
