package org.geoserver.web.admin;

import org.geoserver.web.GeoServerBasePage;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.GeoServer;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;

import java.util.Arrays;
import java.util.List;

public class GlobalSettingsPage extends ServerAdminPage {
    private static final long serialVersionUID = 4716657682337915996L;

    public GlobalSettingsPage() {
        final IModel geoServerModel = getGeoServerModel();
        final IModel globalInfoModel = getGlobalInfoModel();
        final IModel globalConfigModel = getGlobalConfigModel();

        Form form = new Form("form", new CompoundPropertyModel(globalInfoModel)) {
            protected void onSubmit() {
                ((GeoServer)geoServerModel.getObject())
                    .save((GeoServerInfo)globalInfoModel.getObject());
            }
        };

        add( form );

        form.add( new TextField( "maxFeatures", new PropertyModel(globalConfigModel,"maxFeatures") ) );
        form.add( new CheckBox( "verbose" ) );
        form.add( new CheckBox( "verboseExceptions" ) );
        form.add( new TextField( "numDecimals" ) );
        form.add( new TextField( "charset" ) );
        form.add( new TextField( "proxyBaseUrl" ) );
        logLevelsAppend(form, globalConfigModel);
        form.add( new CheckBox( "stdOutLogging" ) );
        form.add( new TextField("loggingLocation") );

        Button submit = new Button("submit",new StringResourceModel( "submit", this, null) );
        form.add(submit);
    }

    private void logLevelsAppend(Form form, IModel globalConfigModel) {
        List<String> logProfiles = Arrays.asList(
                "DEFAULT_LOGGING.properties",
                "VERBOSE_LOGGING.properties",
                "PRODUCTION_LOGGING.properties",
                "GEOTOOLS_DEVELOPER_LOGGING.properties",
                "GEOSERVER_DEVELOPER_LOGGING.properties");

        form.add(new ListChoice("log4jConfigFile",
                    new PropertyModel(globalConfigModel, "log4jConfigFile"), logProfiles ));
    }
};


