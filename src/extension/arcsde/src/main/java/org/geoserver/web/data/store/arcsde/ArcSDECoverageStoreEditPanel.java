/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store.arcsde;

import static org.geotools.arcsde.session.ArcSDEConnectionConfig.CONNECTION_TIMEOUT_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.INSTANCE_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.MAX_CONNECTIONS_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.MIN_CONNECTIONS_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.PASSWORD_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.PORT_NUMBER_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.SERVER_NAME_PARAM_NAME;
import static org.geotools.arcsde.session.ArcSDEConnectionConfig.USER_NAME_PARAM_NAME;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
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
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.ISessionPoolFactory;
import org.geotools.arcsde.session.SessionPoolFactory;
import org.geotools.arcsde.session.UnavailableConnectionException;
import org.geotools.util.logging.Logging;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeRasterColumn;

/**
 * Provides the form components for the arcsde coverage edit page
 * 
 * @author Gabriel Roldan
 * 
 */
public final class ArcSDECoverageStoreEditPanel extends StoreEditPanel {

    /**
     * temporary parameter name used to hold the raster table selected by the drop down into the
     * store's connectionParameters
     */
    private static final String TABLE_NAME = "table";

    private static final long serialVersionUID = 4149587878405421797L;

    private static final Logger LOGGER = Logging.getLogger("org.geoserver.web.data.store.arcsde");

    public ArcSDECoverageStoreEditPanel(final String componentId, final Form storeEditForm) {
        super(componentId, storeEditForm);

        final IModel model = storeEditForm.getModel();
        setModel(model);

        {
            CoverageStoreInfo storeInfo = (CoverageStoreInfo) storeEditForm.getModelObject();
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
                    connectionParameters.put(TABLE_NAME, tableName);
                }
            } else {
                connectionParameters.put(PORT_NUMBER_PARAM_NAME, Integer.valueOf(5151));
            }
        }

        final IModel paramsModel = new PropertyModel(model, "connectionParameters");

        // server, port, instance, user, pwd
        final TextParamPanel serverPanel = addServerPanel(paramsModel);
        final TextParamPanel portPanel = addPortPanel(paramsModel);
        final TextParamPanel instancePanel = addInstancePanel(paramsModel);
        final TextParamPanel userPanel = addUserPanel(paramsModel);
        final PasswordParamPanel pwdPanel = addPasswordPanel(paramsModel);
        final DropDownChoice choice = addTableChoice(paramsModel);

        add(new AjaxSubmitLink("refresh", storeEditForm) {
            private static final long serialVersionUID = 1L;

            /**
             * We're not doing any validation here, just want to perform the same attempt to get to
             * the list of connection parameters than at {@link #onSumbit}
             */
            @Override
            protected void onError(AjaxRequestTarget target, Form form) {
                onSubmit(target, form);
            }

            @Override
            protected void onSubmit(final AjaxRequestTarget target, final Form form) {
                final String server = serverPanel.getFormComponent().getValue();
                final String port = portPanel.getFormComponent().getValue();
                final String instance = instancePanel.getFormComponent().getValue();
                final String user = userPanel.getFormComponent().getValue();
                final String password = pwdPanel.getFormComponent().getValue();

                final ISessionPoolFactory sessionFac = SessionPoolFactory.getInstance();

                List<String> rasterColumns;
                try {
                    rasterColumns = getRasterColumns(server, port, instance, user, password,
                            sessionFac);
                } catch (IllegalArgumentException e) {
                    rasterColumns = Collections.emptyList();
                    String message = e.getMessage();
                    storeEditForm.error(message);
                    target.addComponent(storeEditForm);// refresh
                }

                choice.setChoices(rasterColumns);
                target.addComponent(choice);
                // do nothing else, so we return to the same page...
            }
        });

        /*
         * Listen to form submission and update the model's URL
         */
        storeEditForm.add(new IFormValidator() {
            private static final long serialVersionUID = 1L;

            public FormComponent[] getDependentFormComponents() {
                return new FormComponent[] { serverPanel.getFormComponent(),
                        portPanel.getFormComponent(), instancePanel.getFormComponent(),
                        userPanel.getFormComponent(), pwdPanel.getFormComponent() };
            }

            public void validate(final Form form) {
                CoverageStoreInfo storeInfo = (CoverageStoreInfo) form.getModelObject();
                final String server = serverPanel.getFormComponent().getValue();
                final String port = portPanel.getFormComponent().getValue();
                final String instance = instancePanel.getFormComponent().getValue();
                final String user = userPanel.getFormComponent().getValue();
                final String password = pwdPanel.getFormComponent().getValue();
                final String table = choice.getValue();

                StringBuilder urlb = new StringBuilder("sde://");

                urlb.append(user).append(":").append(password).append("@");
                urlb.append(server).append(":").append(port).append("/");
                if (instance != null && instance.trim().length() > 0) {
                    urlb.append(instance);
                }
                urlb.append("#").append(table);
                String coverageUrl = urlb.toString();
                LOGGER.info("Coverage URL: '" + coverageUrl + "'");
                storeInfo.setURL(coverageUrl);
            }

        });
    }

    private DropDownChoice addTableChoice(final IModel paramsModel) {
        add(new Label("rasterTaleLabel", new ResourceModel("rasterTable", "Raster table:")));

        MapModel choiceModel = new MapModel(paramsModel, TABLE_NAME);

        List<String> choices = new ArrayList<String>();
        if (choiceModel.getObject() != null) {
            Object currentTableName = choiceModel.getObject();
            choices.add(String.valueOf(currentTableName));
        }
        final DropDownChoice choice = new DropDownChoice("rasterTable", choiceModel, choices);

        /*
         * Make table name match the option id
         */
        choice.setChoiceRenderer(new IChoiceRenderer() {
            private static final long serialVersionUID = 1L;

            public String getIdValue(Object tableName, int index) {
                return tableName.toString();
            }

            public Object getDisplayValue(Object tableName) {
                return tableName;
            }
        });
        choice.setOutputMarkupId(true);
        choice.setNullValid(false);
        choice.setRequired(true);

        final FormComponentFeedbackBorder feedback = new FormComponentFeedbackBorder("border");
        feedback.add(choice);
        add(feedback);

        return choice;
    }

    private PasswordParamPanel addPasswordPanel(final IModel paramsModel) {
        final PasswordParamPanel pwdPanel = new PasswordParamPanel("password", new MapModel(
                paramsModel, PASSWORD_PARAM_NAME), new ResourceModel(PASSWORD_PARAM_NAME,
                PASSWORD_PARAM_NAME), true);
        add(pwdPanel);
        return pwdPanel;
    }

    private TextParamPanel addUserPanel(final IModel paramsModel) {
        final TextParamPanel userPanel = new TextParamPanel("user", new MapModel(paramsModel,
                USER_NAME_PARAM_NAME),
                new ResourceModel(USER_NAME_PARAM_NAME, USER_NAME_PARAM_NAME), true);
        userPanel.getFormComponent().setType(String.class);
        add(userPanel);
        return userPanel;
    }

    private TextParamPanel addInstancePanel(final IModel paramsModel) {
        final TextParamPanel instancePanel = new TextParamPanel("instance", new MapModel(
                paramsModel, INSTANCE_NAME_PARAM_NAME), new ResourceModel(INSTANCE_NAME_PARAM_NAME,
                INSTANCE_NAME_PARAM_NAME), false);
        instancePanel.getFormComponent().setType(String.class);
        add(instancePanel);
        return instancePanel;
    }

    private TextParamPanel addPortPanel(final IModel paramsModel) {
        final TextParamPanel portPanel = new TextParamPanel("port", new MapModel(paramsModel,
                PORT_NUMBER_PARAM_NAME), new ResourceModel(PORT_NUMBER_PARAM_NAME,
                PORT_NUMBER_PARAM_NAME), true);
        portPanel.getFormComponent().setType(Integer.class);
        add(portPanel);
        return portPanel;
    }

    private TextParamPanel addServerPanel(final IModel paramsModel) {
        final TextParamPanel serverPanel = new TextParamPanel("server", new MapModel(paramsModel,
                SERVER_NAME_PARAM_NAME), new ResourceModel(SERVER_NAME_PARAM_NAME,
                SERVER_NAME_PARAM_NAME), true);
        serverPanel.getFormComponent().setType(String.class);
        add(serverPanel);
        return serverPanel;
    }

    /**
     * 
     * @param server
     * @param port
     * @param instance
     * @param user
     * @param password
     * @param sessionFac
     * @return
     * @throws IllegalArgumentException
     */
    private List<String> getRasterColumns(final String server, final String port,
            final String instance, final String user, final String password,
            final ISessionPoolFactory sessionFac) throws IllegalArgumentException {

        final ISessionPool pool;
        {
            final ArcSDEConnectionConfig connectionConfig;
            Map<String, String> params = new HashMap<String, String>();
            params.put(SERVER_NAME_PARAM_NAME, server);
            params.put(PORT_NUMBER_PARAM_NAME, port);
            params.put(INSTANCE_NAME_PARAM_NAME, instance);
            params.put(USER_NAME_PARAM_NAME, user);
            params.put(PASSWORD_PARAM_NAME, password);
            params.put(MIN_CONNECTIONS_PARAM_NAME, "1");
            params.put(MAX_CONNECTIONS_PARAM_NAME, "1");
            params.put(CONNECTION_TIMEOUT_PARAM_NAME, "1000");
            connectionConfig = ArcSDEConnectionConfig.fromMap(params);
            try {
                pool = sessionFac.createPool(connectionConfig);
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        ISession session;
        try {
            session = pool.getSession();
        } catch (IOException e) {
            throw new IllegalAccessError(e.getMessage());
        } catch (UnavailableConnectionException e) {
            throw new IllegalAccessError(e.getMessage());
        }

        final List<String> rasterTables;
        try {
            rasterTables = session.issue(new Command<List<String>>() {

                @SuppressWarnings("unchecked")
                @Override
                public List<String> execute(final ISession session, final SeConnection connection)
                        throws SeException, IOException {
                    Vector<SeRasterColumn> rasterColumns = connection.getRasterColumns();
                    List<String> rasterTables = new ArrayList<String>(rasterColumns.size());
                    for (SeRasterColumn col : rasterColumns) {
                        String qualifiedTableName = col.getQualifiedTableName();
                        rasterTables.add(qualifiedTableName.toUpperCase());
                    }
                    Collections.sort(rasterTables);
                    return rasterTables;
                }
            });
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            session.dispose();
            pool.close();
        }

        return rasterTables;
    }
}
