package org.geoserver.web.admin;

import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.LoggingInfo;
import org.geoserver.web.GeoServerHomePage;

public class GlobalSettingsPage extends ServerAdminPage {
    private static final long serialVersionUID = 4716657682337915996L;

    public GlobalSettingsPage() {
        final IModel globalInfoModel = getGlobalInfoModel();
        final IModel loggingInfoModel = getLoggingInfoModel();
        
        Form form = new Form("form", new CompoundPropertyModel(globalInfoModel));

        add(form);

        form.add(new CheckBox("verbose"));
        form.add(new CheckBox("verboseExceptions"));
        form.add(new CheckBox("globalServices"));
        form.add(new TextField("numDecimals"));
        form.add(new TextField("charset"));
        form.add(new TextField("proxyBaseUrl"));
        
        logLevelsAppend(form, loggingInfoModel);
        form.add(new CheckBox("stdOutLogging", new PropertyModel( loggingInfoModel, "stdOutLogging")));
        form.add(new TextField("loggingLocation", new PropertyModel( loggingInfoModel, "location")) );
        
        form.add(new TextField("featureTypeCacheSize"));
        
        Button submit = new Button("submit", new StringResourceModel("submit", this, null)) {
            @Override
            public void onSubmit() {
                GeoServer gs = getGeoServer();
                gs.save( (GeoServerInfo) globalInfoModel.getObject() );
                gs.save( (LoggingInfo) loggingInfoModel.getObject() );
                setResponsePage(GeoServerHomePage.class);
            }
        };
        form.add(submit);
        
        Button cancel = new Button("cancel") {
            @Override
            public void onSubmit() {
                setResponsePage(GeoServerHomePage.class);
            }
        };
        form.add(cancel);
    }

    private void logLevelsAppend(Form form, IModel loggingInfoModel) {
        List<String> logProfiles = Arrays.asList("DEFAULT_LOGGING.properties",
                "VERBOSE_LOGGING.properties", "PRODUCTION_LOGGING.properties",
                "GEOTOOLS_DEVELOPER_LOGGING.properties", "GEOSERVER_DEVELOPER_LOGGING.properties");

        form.add(new ListChoice("log4jConfigFile", new PropertyModel(loggingInfoModel,
                "level"), logProfiles));
    }
};
