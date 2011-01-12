package org.geoserver.gwc.web;

import java.util.logging.Logger;

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
import org.geotools.util.logging.Logging;
import org.geowebcache.diskquota.DiskQuotaConfig;

public class GWCSettingsPage extends GeoServerSecuredPage {

    private static final Logger LOGGER = Logging.getLogger(GWCSettingsPage.class);

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public GWCSettingsPage() {
        setHeaderPanel(headerPanel());

        Form form = new Form("form");
        add(form);

        final IModel<WMSInfo> wmsInfoModel = new LoadableDetachableModel<WMSInfo>() {
            private static final long serialVersionUID = 1L;

            public WMSInfo load() {
                return getGeoServer().getService(WMSInfo.class);
            }
        };

        final IModel<DiskQuotaConfig> diskQuotaModel = new LoadableDetachableModel<DiskQuotaConfig>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected DiskQuotaConfig load() {
                final GWC gwc = getGWC();
                DiskQuotaConfig quotaConfig = gwc.getDisQuotaConfig();
                return quotaConfig;
            }
        };

        final IModel<GWC> gwcModel = new LoadableDetachableModel<GWC>() {

            private static final long serialVersionUID = 1L;

            @Override
            protected GWC load() {
                return getGWC();
            }
        };

        PropertyModel<MetadataMap> metadataModel = new PropertyModel<MetadataMap>(wmsInfoModel,
                "metadata");
        IModel<Boolean> wmsIntegrationEnabledModel = new MapModel(metadataModel,
                GWC.WMS_INTEGRATION_ENABLED_KEY);

        CheckBox wmsIntegration = checkbox("enableWMSIntegration", wmsIntegrationEnabledModel,
                "GWCSettingsPage.enableWMSIntegration.title");
        form.add(wmsIntegration);

        form.add(new DiskQuotaConfigPanel("diskQuotaConfigPanel", form, diskQuotaModel, gwcModel));

        form.add(new Button("submit") {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                GeoServer gs = getGeoServer();
                WMSInfo wmsInfo = wmsInfoModel.getObject();
                gs.save(wmsInfo);

                // DiskQuotaConfig diskQuotaConfig = diskQuotaModel.getObject();
                GWC gwc = getGWC();
                gwc.saveDiskQuotaConfig();

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

    private GWC getGWC() {
        final GWC gwc = (GWC) getGeoServerApplication().getBean("gwcFacade");
        return gwc;
    }

    protected Component headerPanel() {
        Fragment header = new Fragment(HEADER_PANEL, "header", this);

        return header;
    }

    static CheckBox checkbox(String id, IModel<Boolean> model, String titleKey) {
        CheckBox checkBox = new CheckBox(id, model);
        if (null != titleKey) {
            checkBox.add(new AttributeModifier("title", true, new StringResourceModel(titleKey,
                    (Component) null, null)));
        }
        return checkBox;
    }
}
