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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.geoserver.web.util.MapModel;
import org.geotools.arcsde.session.ArcSDEConnectionConfig;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.session.ISessionPool;
import org.geotools.arcsde.session.ISessionPoolFactory;
import org.geotools.arcsde.session.SessionPoolFactory;
import org.geotools.arcsde.session.UnavailableConnectionException;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeRasterColumn;

public class RasterTableSelectionPanel extends Panel {

    private static final long serialVersionUID = 343924350476584166L;

    /**
     * temporary parameter name used to hold the raster table selected by the drop down into the
     * store's connectionParameters
     */
    public static final String TABLE_NAME = "table";

    private final DropDownChoice choice;

    private FormComponent serverComponent;

    private FormComponent portComponent;

    private FormComponent instanceComponent;

    private FormComponent userComponent;

    private FormComponent passwordComponent;

    public RasterTableSelectionPanel(final String id, final IModel paramsModel,
            final Form storeEditForm, FormComponent server, FormComponent port,
            FormComponent instance, FormComponent user, FormComponent password) {

        super(id);
        this.serverComponent = server;
        this.portComponent = port;
        this.instanceComponent = instance;
        this.userComponent = user;
        this.passwordComponent = password;

        final MapModel tableNameModel = new MapModel(paramsModel, TABLE_NAME);

        List<String> choices = new ArrayList<String>();
        if (tableNameModel.getObject() != null) {
            Object currentTableName = tableNameModel.getObject();
            choices.add(String.valueOf(currentTableName));
        }

        choice = new DropDownChoice("rasterTable", tableNameModel, choices);

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

                final String server = serverComponent.getValue();
                final String port = portComponent.getValue();
                final String instance = instanceComponent.getValue();
                final String user = userComponent.getValue();
                final String password = passwordComponent.getValue();

                final ISessionPoolFactory sessionFac = SessionPoolFactory.getInstance();

                List<String> rasterColumns;
                try {
                    rasterColumns = getRasterColumns(server, port, instance, user, password,
                            sessionFac);
                } catch (IllegalArgumentException e) {
                    rasterColumns = Collections.emptyList();
                    String message = "Refreshing raster tables list: " + e.getMessage();
                    storeEditForm.error(message);
                    target.addComponent(storeEditForm);// refresh
                }

                choice.setChoices(rasterColumns);
                target.addComponent(choice);
                // do nothing else, so we return to the same page...
            }
        });

    }

    public DropDownChoice getFormComponent() {
        return choice;
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
    List<String> getRasterColumns(final String server, final String port, final String instance,
            final String user, final String password, final ISessionPoolFactory sessionFac)
            throws IllegalArgumentException {

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
