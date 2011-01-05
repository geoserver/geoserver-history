package org.geoserver.gwc.web;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.config.GeoServer;
import org.geoserver.gwc.GWC;
import org.geoserver.web.GeoServerHomePage;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.util.MapModel;
import org.geoserver.wms.WMSInfo;

public class GWCSettingsPage extends GeoServerSecuredPage {

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public GWCSettingsPage() {
        Form form = new Form("form");
        add(form);

        final IModel<WMSInfo> wmsInfoModel = new LoadableDetachableModel<WMSInfo>() {
            private static final long serialVersionUID = 1L;

            public WMSInfo load() {
                return getGeoServer().getService(WMSInfo.class);
            }
        };

        PropertyModel<MetadataMap> metadataModel = new PropertyModel<MetadataMap>(wmsInfoModel,
                "metadata");

        IModel<Boolean> wmsIntegrationEnabledModel = new MapModel(metadataModel,
                GWC.WMS_INTEGRATION_ENABLED_KEY);

        setHeaderPanel(headerPanel());

        CheckBox wmsIntegration = new CheckBox("enableWMSIntegration", wmsIntegrationEnabledModel);
        wmsIntegration.add(new AttributeModifier("title", true, new StringResourceModel(
                "GWCSettingsPage.enableWMSIntegration.title", (Component) null, null)));
        form.add(wmsIntegration);

        form.add(new Button("submit") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                GeoServer gs = getGeoServer();
                WMSInfo wmsInfo = wmsInfoModel.getObject();
                gs.save(wmsInfo);
                setResponsePage(GeoServerHomePage.class);
            }
        });
        form.add(new Button("cancel") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                setResponsePage(GeoServerHomePage.class);
            }
        });

    }

    protected Component headerPanel() {
        Fragment header = new Fragment(HEADER_PANEL, "header", this);

        return header;
    }

}
