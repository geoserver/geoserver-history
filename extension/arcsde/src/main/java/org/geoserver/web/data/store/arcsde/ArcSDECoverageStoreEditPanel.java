/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store.arcsde;

import static org.geoserver.web.data.store.arcsde.RasterTableSelectionPanel.TABLE_NAME;
import static org.geotools.arcsde.ArcSDEDataStoreFactory.INSTANCE_PARAM;
import static org.geotools.arcsde.ArcSDEDataStoreFactory.PASSWORD_PARAM;
import static org.geotools.arcsde.ArcSDEDataStoreFactory.PORT_PARAM;
import static org.geotools.arcsde.ArcSDEDataStoreFactory.SERVER_PARAM;
import static org.geotools.arcsde.ArcSDEDataStoreFactory.USER_PARAM;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.INSTANCE_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.PASSWORD_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.PORT_NUMBER_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.SERVER_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.USER_NAME_PARAM_NAME;

import java.io.Serializable;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.web.data.store.StoreEditPanel;
import org.geoserver.web.data.store.panel.PasswordParamPanel;
import org.geoserver.web.data.store.panel.TextParamPanel;
import org.geoserver.web.util.MapModel;
import org.geotools.arcsde.data.ArcSDEDataStoreConfig;
import org.geotools.arcsde.gce.ArcSDERasterFormat;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.util.logging.Logging;

/**
 * Provides the form components for the arcsde coverage edit page
 * 
 * @author Gabriel Roldan
 * 
 */
public final class ArcSDECoverageStoreEditPanel extends StoreEditPanel {

    private static final long serialVersionUID = 4149587878405421797L;

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.data.store.arcsde");

    final FormComponent server;

    final FormComponent port;

    final FormComponent instance;

    final FormComponent user;

    final FormComponent password;

    final FormComponent table;

    public ArcSDECoverageStoreEditPanel(final String componentId, final Form storeEditForm) {
        super(componentId, storeEditForm);

        final IModel model = storeEditForm.getModel();
        setModel(model);
        final CoverageStoreInfo storeInfo = (CoverageStoreInfo) storeEditForm.getModelObject();
        {
            String url = storeInfo.getURL();
            Map<String, Serializable> connectionParameters = storeInfo.getConnectionParameters();
            if (null != url && url.startsWith("sde:")) {
                ArcSDEDataStoreConfig connectionConfig;
                connectionConfig = ArcSDERasterFormat
                        .sdeURLToConnectionConfig(new StringBuffer(url));
                connectionParameters.put(SERVER_NAME_PARAM_NAME, connectionConfig.getServerName());
                connectionParameters.put(PORT_NUMBER_PARAM_NAME, connectionConfig.getPortNumber());
                connectionParameters.put(INSTANCE_NAME_PARAM_NAME, connectionConfig
                        .getDatabaseName());
                connectionParameters.put(USER_NAME_PARAM_NAME, connectionConfig.getUserName());
                connectionParameters.put(PASSWORD_PARAM_NAME, connectionConfig.getUserPassword());

                // parse table name
                int idx = url.lastIndexOf('#');
                if (idx > 0) {
                    String tableName = url.substring(idx + 1);
                    connectionParameters.put(RasterTableSelectionPanel.TABLE_NAME, tableName);
                }
            } else {
                connectionParameters.put(PORT_NUMBER_PARAM_NAME, Integer.valueOf(5151));
            }
        }

        final IModel paramsModel = new PropertyModel(model, "connectionParameters");

        // server, port, instance, user, pwd
        server = addTextPanel(paramsModel, SERVER_PARAM);
        port = addTextPanel(paramsModel, PORT_PARAM);
        instance = addTextPanel(paramsModel, INSTANCE_PARAM);
        user = addTextPanel(paramsModel, USER_PARAM);
        password = addPasswordPanel(paramsModel);

        add(new Label("rasterTaleLabel", new ResourceModel("rasterTable", "Raster table:")));
        boolean isNew = storeInfo.getId() == null;
        table = addTableNameComponent(paramsModel, isNew);

        /*
         * Listen to form submission and update the model's URL
         */
        storeEditForm.add(new IFormValidator() {
            private static final long serialVersionUID = 1L;

            public FormComponent[] getDependentFormComponents() {
                return new FormComponent[] { server, port, instance, user, password, table };
            }

            public void validate(final Form form) {
                CoverageStoreInfo storeInfo = (CoverageStoreInfo) form.getModelObject();
                final String serverVal = server.getValue();
                final String portVal = port.getValue();
                final String instanceVal = instance.getValue();
                final String userVal = user.getValue();
                final String passwordVal = password.getValue();
                final String tableVal = table.getValue();

                StringBuilder urlb = new StringBuilder("sde://");

                urlb.append(userVal).append(":").append(passwordVal).append("@");
                urlb.append(serverVal).append(":").append(portVal).append("/");
                if (instanceVal != null && instanceVal.trim().length() > 0) {
                    urlb.append(instanceVal);
                }
                urlb.append("#").append(tableVal);
                String coverageUrl = urlb.toString();
                LOGGER.info("Coverage URL: '" + coverageUrl + "'");
                storeInfo.setURL(coverageUrl);
            }

        });
    }

    /**
     * 
     * @param paramsModel
     * @param isNew
     * @return a combobox set up to display the list of available raster tables if the StoreInfo is
     *         new, or a non editable text box if we're editing an existing StoreInfo
     */
    private FormComponent addTableNameComponent(final IModel paramsModel, final boolean isNew) {

        final FormComponent tableNameComponent;
        final String panelId = "tableNamePanel";

        if (isNew) {
            RasterTableSelectionPanel selectionPanel;
            selectionPanel = new RasterTableSelectionPanel(panelId, paramsModel, storeEditForm,
                    server, port, instance, user, password);
            add(selectionPanel);

            DropDownChoice tableDropDown = selectionPanel.getFormComponent();
            tableNameComponent = tableDropDown;
        } else {
            /*
             * We're editing an existing store. Don't allow to change the table name, it could
             * catastrophic for the Catalog/ResourcePool as ability to get to the coverage is really
             * based on the Store's URL and the CoverageInfo is tied to it
             */
            final IModel paramValue = new MapModel(paramsModel, TABLE_NAME);
            final IModel paramLabelModel = new ResourceModel(TABLE_NAME, TABLE_NAME);
            final boolean required = true;
            TextParamPanel tableNamePanel;
            tableNamePanel = new TextParamPanel(panelId, paramValue, paramLabelModel, required);
            add(tableNamePanel);

            tableNameComponent = tableNamePanel.getFormComponent();
            tableNameComponent.setEnabled(false);
        }
        return tableNameComponent;
    }

    private FormComponent addPasswordPanel(final IModel paramsModel) {

        final String paramName = PASSWORD_PARAM.key;
        final String resourceKey = getClass().getSimpleName() + "." + paramName;

        final PasswordParamPanel pwdPanel = new PasswordParamPanel(paramName, new MapModel(
                paramsModel, paramName), new ResourceModel(resourceKey, paramName), true);
        add(pwdPanel);

        String defaultTitle = String.valueOf(PASSWORD_PARAM.title);

        ResourceModel titleModel = new ResourceModel(resourceKey + ".title", defaultTitle);
        String title = String.valueOf(titleModel.getObject());

        pwdPanel.getFormComponent().add(new SimpleAttributeModifier("title", title));

        return pwdPanel.getFormComponent();
    }

    private FormComponent addTextPanel(final IModel paramsModel, final Param param) {

        final String paramName = param.key;
        final String resourceKey = getClass().getSimpleName() + "." + paramName;

        final boolean required = param.required;

        final TextParamPanel textParamPanel = new TextParamPanel(paramName, new MapModel(
                paramsModel, paramName), new ResourceModel(resourceKey, paramName), required);
        textParamPanel.getFormComponent().setType(param.type);

        String defaultTitle = String.valueOf(param.title);

        ResourceModel titleModel = new ResourceModel(resourceKey + ".title", defaultTitle);
        String title = String.valueOf(titleModel.getObject());

        textParamPanel.getFormComponent().add(new SimpleAttributeModifier("title", title));

        add(textParamPanel);
        return textParamPanel.getFormComponent();
    }

}
