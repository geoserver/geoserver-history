package org.geoserver.gwc.web;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.catalog.MetadataMap;
import org.geoserver.config.GeoServer;
import org.geoserver.gwc.GWC;
import org.geoserver.web.GeoServerHomePage;
import org.geoserver.web.GeoServerSecuredPage;
import org.geoserver.web.util.MapModel;
import org.geoserver.wms.WMSInfo;
import org.geowebcache.diskquota.DiskQuotaConfig;
import org.geowebcache.diskquota.ExpirationPolicy;
import org.geowebcache.diskquota.Quota;
import org.geowebcache.diskquota.StorageUnit;

public class GWCSettingsPage extends GeoServerSecuredPage {

    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
    public GWCSettingsPage() {

        add(HeaderContributor.forCss(GWCSettingsPage.class, "statusbar.css"));
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
        PropertyModel<MetadataMap> metadataModel = new PropertyModel<MetadataMap>(wmsInfoModel,
                "metadata");
        IModel<Boolean> wmsIntegrationEnabledModel = new MapModel(metadataModel,
                GWC.WMS_INTEGRATION_ENABLED_KEY);

        CheckBox wmsIntegration = checkbox("enableWMSIntegration", wmsIntegrationEnabledModel,
                "GWCSettingsPage.enableWMSIntegration.title");
        form.add(wmsIntegration);

        addDiskQuotaPanel(form, diskQuotaModel);

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

    private void addDiskQuotaPanel(final Form form, final IModel<DiskQuotaConfig> diskQuotaModel) {
        // quotaConfig.getCacheCleanUpFrequency();
        // quotaConfig.getCacheCleanUpUnits();
        // quotaConfig.getDiskBlockSize();
        // quotaConfig.getGlobalExpirationPolicy();
        // quotaConfig.getGlobalExpirationPolicyName();
        // quotaConfig.getGlobalQuota();
        // quotaConfig.getGlobalUsedQuota();
        // quotaConfig.getMaxConcurrentCleanUps();
        // quotaConfig.getNumLayers();

        final DiskQuotaConfig diskQuotaConfig = diskQuotaModel.getObject();

        IModel<Boolean> quotaEnablementModel = new PropertyModel<Boolean>(diskQuotaModel, "enabled");
        CheckBox diskQuotaIntegration = checkbox("enableDiskQuota", quotaEnablementModel,
                "GWCSettingsPage.enableDiskQuota.title");
        form.add(diskQuotaIntegration);

        IModel<ExpirationPolicy> globalQuotaPolicyModel = new PropertyModel<ExpirationPolicy>(
                diskQuotaModel, "globalExpirationPolicy");

        RadioGroup<ExpirationPolicy> globalQuota = new RadioGroup<ExpirationPolicy>("globalQuota",
                globalQuotaPolicyModel);
        form.add(globalQuota);

        IModel<ExpirationPolicy> lfuModel = new LoadableDetachableModel<ExpirationPolicy>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected ExpirationPolicy load() {
                return getGWC().getExpirationPolicy("LFU");
            }
        };
        IModel<ExpirationPolicy> lruModel = new LoadableDetachableModel<ExpirationPolicy>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected ExpirationPolicy load() {
                return getGWC().getExpirationPolicy("LRU");
            }
        };

        Radio<ExpirationPolicy> globalQuotaPolicyLFU;
        Radio<ExpirationPolicy> globalQuotaPolicyLRU;
        globalQuotaPolicyLFU = new Radio<ExpirationPolicy>("globalQuotaPolicyLFU", lfuModel);
        globalQuotaPolicyLRU = new Radio<ExpirationPolicy>("globalQuotaPolicyLRU", lruModel);

        globalQuota.add(globalQuotaPolicyLFU);
        globalQuota.add(globalQuotaPolicyLRU);

        WebMarkupContainer green = new WebMarkupContainer("statusBarGreen");
        WebMarkupContainer red = new WebMarkupContainer("statusBarRed");

        final Quota limit = diskQuotaConfig.getGlobalQuota() == null ? new Quota(1, StorageUnit.MiB)
                : new Quota(diskQuotaConfig.getGlobalQuota());
        final Quota used = new Quota(diskQuotaConfig.getGlobalUsedQuota());
        final Quota excess = used.difference(limit);
        int usedPercentage;
        int excessPercentage;

        final int progressWidth = 200;

        StorageUnit unit;
        if (excess.getValue().doubleValue() > 0) {
            StorageUnit excessUnits = excess.getUnits();
            StorageUnit usedUnits = used.getUnits();
            double usedValue = used.getValue().doubleValue();
            double excessValue = excessUnits.convertTo(excess.getValue(), usedUnits).doubleValue();
            excessPercentage = (int) Math.round((excessValue * progressWidth) / usedValue);
            usedPercentage = progressWidth - excessPercentage;
        } else {
            unit = limit.getUnits();
            double usedValue = used.getUnits().convertTo(used.getValue(), unit).doubleValue();
            double limitValue = limit.getValue().doubleValue();
            usedPercentage = (int) Math.round(usedValue * progressWidth / limitValue);
            excessPercentage = 0;
        }

        green.add(new AttributeModifier("style", true, new Model<String>("width: " + usedPercentage
                + "px;")));

        String redStyle = "width: " + excessPercentage + "px; left: "
                + (1 + progressWidth - excessPercentage) + "px;";
        red.add(new AttributeModifier("style", true, new Model<String>(redStyle)));

        form.add(green);
        form.add(red);
    }

    private GWC getGWC() {
        final GWC gwc = (GWC) getGeoServerApplication().getBean("gwcFacade");
        return gwc;
    }

    protected Component headerPanel() {
        Fragment header = new Fragment(HEADER_PANEL, "header", this);

        return header;
    }

    private CheckBox checkbox(String id, IModel<Boolean> model, String titleKey) {
        CheckBox checkBox = new CheckBox(id, model);
        if (null != titleKey) {
            checkBox.add(new AttributeModifier("title", true, new StringResourceModel(titleKey,
                    (Component) null, null)));
        }
        return checkBox;
    }
}
