package org.geoserver.gwc.web;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
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
import org.geotools.util.logging.Logging;
import org.geowebcache.diskquota.DiskQuotaConfig;
import org.geowebcache.diskquota.ExpirationPolicy;
import org.geowebcache.diskquota.Quota;
import org.geowebcache.diskquota.StorageUnit;

public class GWCSettingsPage extends GeoServerSecuredPage {

    private static final Logger LOGGER = Logging.getLogger(GWCSettingsPage.class);

    @SuppressWarnings({ "unchecked", "rawtypes", "deprecation" })
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

        IModel<Integer> blockSizeModel;
        blockSizeModel = new PropertyModel<Integer>(diskQuotaModel, "diskBlockSize");
        TextField<Integer> diskBlockSize = new TextField<Integer>("diskBlockSize", blockSizeModel);
        diskBlockSize.setRequired(true);
        diskBlockSize.add(new AttributeModifier("title", true, new StringResourceModel(
                "GWCSettingsPage.diskBlockSize.title", (Component) null, null)));
        form.add(diskBlockSize);

        IModel<ExpirationPolicy> globalQuotaPolicyModel = new PropertyModel<ExpirationPolicy>(
                diskQuotaModel, "globalExpirationPolicy");

        RadioGroup<ExpirationPolicy> globalQuotaPolicy;
        globalQuotaPolicy = new RadioGroup<ExpirationPolicy>("globalQuotaExpirationPolicy",
                globalQuotaPolicyModel);
        form.add(globalQuotaPolicy);

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

        globalQuotaPolicy.add(globalQuotaPolicyLFU);
        globalQuotaPolicy.add(globalQuotaPolicyLRU);

        if (diskQuotaConfig.getGlobalQuota() == null) {
            LOGGER.info("There's no GWC global disk quota configured, setting a default of 100MiB");
            diskQuotaConfig.setGlobalQuota(new Quota(100, StorageUnit.MiB));
        }

        final IModel<Quota> globalQuotaModel = new LoadableDetachableModel<Quota>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected Quota load() {
                return getGWC().getDisQuotaConfig().getGlobalQuota();
            }
        };
        final IModel<Quota> globalUsedQuotaModel = new LoadableDetachableModel<Quota>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected Quota load() {
                return getGWC().getDisQuotaConfig().getGlobalUsedQuota();
            }
        };

        addGlobalQuotaStatusBar(form, globalQuotaModel, globalUsedQuotaModel);

        Object[] params = { globalUsedQuotaModel.getObject().toNiceString(),
                globalQuotaModel.getObject().toNiceString() };
        IModel<String> usageModel = new StringResourceModel("GWCSettingsPage.usedQuotaMessage",
                null, params);
        form.add(new Label("globalQuotaLabel", usageModel));

        IModel<BigDecimal> quotaValueModel;
        quotaValueModel = new PropertyModel<BigDecimal>(globalQuotaModel, "value");
        TextField<BigDecimal> quotaValue = new TextField<BigDecimal>("globalQuota", quotaValueModel);
        quotaValue.setRequired(true);
        form.add(quotaValue);

        IModel<StorageUnit> unitModel = new PropertyModel<StorageUnit>(globalQuotaModel, "units");
        List<? extends StorageUnit> units = Arrays.asList(StorageUnit.MiB, StorageUnit.GiB,
                StorageUnit.TiB);
        DropDownChoice<StorageUnit> quotaUnitChoice;
        quotaUnitChoice = new DropDownChoice<StorageUnit>("globalQuotaUnits", unitModel, units);
        form.add(quotaUnitChoice);
    }

    private void addGlobalQuotaStatusBar(final Form form, final IModel<Quota> globalQuotaModel,
            final IModel<Quota> globalUsedQuotaModel) {

        Quota limit = globalQuotaModel.getObject();
        Quota used = globalUsedQuotaModel.getObject();

        BigDecimal usedValue;
        BigDecimal limitValue;
        if (limit.min(used) == limit) {// used > limit?, use used quota units
            limitValue = limit.getUnits().convertTo(limit.getValue(), used.getUnits());
            usedValue = used.getValue();
        } else {
            limitValue = limit.getValue();
            usedValue = used.getUnits().convertTo(used.getValue(), limit.getUnits());
        }

        final IModel<Number> usedModel = new Model<Number>(usedValue);
        final IModel<Number> limitModel = new Model<Number>(limitValue);

        StatusBar statusBar = new StatusBar("globalQuotaProgressBar", limitModel, usedModel);

        form.add(statusBar);
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
