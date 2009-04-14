/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.IValidator;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.data.store.panel.CheckBoxParamPanel;
import org.geoserver.web.data.store.panel.LabelParamPanel;
import org.geoserver.web.data.store.panel.PasswordParamPanel;
import org.geoserver.web.data.store.panel.TextParamPanel;
import org.geoserver.web.util.MapModel;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataAccessFactory.Param;

/**
 * Abstract base class for adding/editing a {@link DataStoreInfo}, provides the UI components and a
 * template method {@link #onSaveDataStore(Form)} for the subclasses to perform the insertion or
 * update of the object.
 * 
 * @author Gabriel Roldan
 * @see DataAccessNewPage
 * @see DataAccessEditPage
 */
public abstract class AbstractDataAccessPage extends GeoServerSecuredPage {

    /**
     * Key used to store the name assigned to the datastore in {@code parametersMap} as its a
     * DataStoreInfo property and not a DataAccess one
     */
    protected static final String DATASTORE_NAME_PROPERTY_NAME = "Wicket_Data_Source_Name";

    /**
     * Key used to store the description assigned to the datastore in {@code parametersMap} as its a
     * DataStoreInfo property and not a DataAccess one
     */
    protected static final String DATASTORE_DESCRIPTION_PROPERTY_NAME = "Wicket_Data_Source_Description";

    /**
     * Key used to store the enabled property assigned to the datastore in {@code parametersMap} as
     * its a DataStoreInfo property and not a DataAccess one
     */
    protected static final String DATASTORE_ENABLED_PROPERTY_NAME = "Wicket_Data_Source_Enabled";

    /**
     * Holds datastore parameters. Properties will be settled by the form input fields.
     */
    protected final Map<String, Serializable> parametersMap;

    /**
     * Id of the workspace the datastore is or is going to be attached to
     */
    protected String workspaceId;

    public AbstractDataAccessPage() {
        parametersMap = new HashMap<String, Serializable>();
    }

    /**
     * 
     * @param workspaceId
     *            the id for the workspace to attach the new datastore or the current datastore is
     *            attached to
     * 
     * @param dsFactory
     *            the datastore factory to use
     * @param isNew
     *            wheter to set up the UI for a new dataaccess or an existing one, some properties
     *            may need not to be editable if not a new one.
     */
    protected final void initUI(final DataStoreFactorySpi dsFactory, final boolean isNew) {
        WorkspaceInfo workspace = getWorkspace();
        if (workspace == null) {
            throw new IllegalArgumentException("Can't locate workspace with id " + workspaceId);
        }

        final List<ParamInfo> paramsInfo = new ArrayList<ParamInfo>();
        {
            Param[] dsParams = dsFactory.getParametersInfo();
            for (Param p : dsParams) {
                paramsInfo.add(new ParamInfo(p));
            }
        }

        add(new Label("storeType", dsFactory.getDisplayName()));
        add(new Label("storeTypeDescription", dsFactory.getDescription()));
        add(new Label("workspaceName", workspace.getName()));

        final Form paramsForm = new Form("dataStoreForm");

        add(paramsForm);

        Panel dataStoreNamePanel;
        if (isNew) {
            IValidator dsIdValidator = new StoreNameValidator(DataStoreInfo.class);
            dataStoreNamePanel = new TextParamPanel("dataStoreNamePanel", new MapModel(
                    parametersMap, DATASTORE_NAME_PROPERTY_NAME), "Data Source Name", true,
                    dsIdValidator);
        } else {
            dataStoreNamePanel = new LabelParamPanel("dataStoreNamePanel", new MapModel(
                    parametersMap, DATASTORE_NAME_PROPERTY_NAME), "Data Source Name");
        }

        paramsForm.add(dataStoreNamePanel);

        paramsForm.add(new TextParamPanel("dataStoreDescriptionPanel", new MapModel(parametersMap,
                DATASTORE_DESCRIPTION_PROPERTY_NAME), "Description", false, (IValidator[]) null));

        paramsForm.add(new CheckBoxParamPanel("dataStoreEnabledPanel", new MapModel(parametersMap,
                DATASTORE_ENABLED_PROPERTY_NAME), "Enabled"));

        ListView paramsList = new ListView("parameters", paramsInfo) {
            private static final long serialVersionUID = 1L;

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

        paramsForm.add(new BookmarkablePageLink("cancel", StorePage.class));

        paramsForm.add(new SubmitLink("save") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                onSaveDataStore(paramsForm);
            }
        });

        paramsForm.add(new FeedbackPanel("feedback"));
    }

    private WorkspaceInfo getWorkspace() {
        Catalog catalog = getCatalog();
        WorkspaceInfo workspace = catalog.getWorkspace(workspaceId);
        return workspace;
    }

    /**
     * Call back method called when the save button is hit. Subclasses shall override in order to
     * perform the action over the catalog, whether it is adding a new {@link DataStoreInfo} or
     * saving the edits to an existing one
     * 
     * @param paramsForm
     *            the form containing the parameter values
     */
    protected abstract void onSaveDataStore(final Form paramsForm);

    /**
     * Creates a form input component for the given datastore param based on its type and metadata
     * properties.
     * 
     * @param param
     * @return
     */
    private Panel getInputComponent(final String componentId, final Map<String, ?> paramsMap,
            final ParamInfo param) {

        final String paramName = param.getName();
        final String paramLabel = param.getName();
        final boolean required = param.isRequired();
        final Class<?> binding = param.getBinding();

        Panel parameterPanel;
        if (Boolean.class == binding) {
            parameterPanel = new CheckBoxParamPanel(componentId,
                    new MapModel(paramsMap, paramName), paramLabel);
        } else if (String.class == binding && param.isPassword()) {
            parameterPanel = new PasswordParamPanel(componentId,
                    new MapModel(paramsMap, paramName), paramLabel, required);
        } else {
            parameterPanel = new TextParamPanel(componentId, new MapModel(paramsMap, paramName),
                    paramLabel, required, null);
        }
        return parameterPanel;
    }

}