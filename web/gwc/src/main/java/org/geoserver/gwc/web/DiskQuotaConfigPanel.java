package org.geoserver.gwc.web;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.gwc.GWC;
import org.geotools.util.logging.Logging;
import org.geowebcache.diskquota.DiskQuotaConfig;
import org.geowebcache.diskquota.ExpirationPolicy;
import org.geowebcache.diskquota.Quota;
import org.geowebcache.diskquota.StorageUnit;

public class DiskQuotaConfigPanel extends Panel {
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logging.getLogger(DiskQuotaConfigPanel.class);

    private IModel<GWC> gwcModel;

    @SuppressWarnings("unchecked")
    public DiskQuotaConfigPanel(final String id, final Form form,
            final IModel<DiskQuotaConfig> diskQuotaModel, final IModel<GWC> gwcModel) {
        super(id);
        this.gwcModel = gwcModel;
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
        CheckBox diskQuotaIntegration = GWCSettingsPage.checkbox("enableDiskQuota",
                quotaEnablementModel, "GWCSettingsPage.enableDiskQuota.title");
        add(diskQuotaIntegration);

        IModel<Integer> blockSizeModel;
        blockSizeModel = new PropertyModel<Integer>(diskQuotaModel, "diskBlockSize");
        TextField<Integer> diskBlockSize = new TextField<Integer>("diskBlockSize", blockSizeModel);
        diskBlockSize.setRequired(true);
        diskBlockSize.add(new AttributeModifier("title", true, new StringResourceModel(
                "GWCSettingsPage.diskBlockSize.title", (Component) null, null)));
        add(diskBlockSize);

        int frequency = diskQuotaConfig.getCacheCleanUpFrequency();
        TimeUnit unit = diskQuotaConfig.getCacheCleanUpUnits();
        if (TimeUnit.SECONDS != unit) {
            frequency = (int) TimeUnit.SECONDS.convert(frequency, unit);
            diskQuotaConfig.setCacheCleanUpFrequency(frequency);
            diskQuotaConfig.setCacheCleanUpUnits(TimeUnit.SECONDS);
        }

        IModel<Integer> cleanUpFreqModel;
        cleanUpFreqModel = new PropertyModel<Integer>(diskQuotaModel, "cacheCleanUpFrequency");
        TextField<Integer> cleanUpFreq = new TextField<Integer>("cleanUpFreq", cleanUpFreqModel);
        cleanUpFreq.setRequired(true);
        cleanUpFreq.add(new AttributeModifier("title", true, new StringResourceModel(
                "GWCSettingsPage.cleanUpFreq.title", (Component) null, null)));
        add(cleanUpFreq);
        {
            Date lastRun = diskQuotaConfig.getLastCleanUpTime();
            String resourceId;
            HashMap<String, String> params = new HashMap<String, String>();
            if (lastRun == null) {
                resourceId = "GWCSettingsPage.cleanUpLastRunNever";
            } else {
                resourceId = "GWCSettingsPage.cleanUpLastRun";
                long timeAgo = (System.currentTimeMillis() - lastRun.getTime()) / 1000;
                String timeUnits = "s";
                if (timeAgo > 60 * 60 * 24) {
                    timeUnits = "d";
                    timeAgo /= 60 * 60 * 24;
                } else if (timeAgo > 60 * 60) {
                    timeUnits = "h";
                    timeAgo /= 60 * 60;
                } else if (timeAgo > 60) {
                    timeUnits = "m";
                    timeAgo /= 60;
                }
                params.put("x", String.valueOf(timeAgo));
                params.put("timeUnit", timeUnits);
            }
            IModel<String> lastRunModel = new StringResourceModel(resourceId, this, new Model(
                    params));
            add(new Label("GWCSettingsPage.cleanUpLastRun", lastRunModel));
        }
        IModel<ExpirationPolicy> globalQuotaPolicyModel = new PropertyModel<ExpirationPolicy>(
                diskQuotaModel, "globalExpirationPolicy");

        RadioGroup<ExpirationPolicy> globalQuotaPolicy;
        globalQuotaPolicy = new RadioGroup<ExpirationPolicy>("globalQuotaExpirationPolicy",
                globalQuotaPolicyModel);
        add(globalQuotaPolicy);

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
        add(new Label("globalQuotaLabel", usageModel));

        IModel<BigDecimal> quotaValueModel;
        quotaValueModel = new PropertyModel<BigDecimal>(globalQuotaModel, "value");
        TextField<BigDecimal> quotaValue = new TextField<BigDecimal>("globalQuota", quotaValueModel);
        quotaValue.setRequired(true);
        add(quotaValue);

        IModel<StorageUnit> unitModel = new PropertyModel<StorageUnit>(globalQuotaModel, "units");
        List<? extends StorageUnit> units = Arrays.asList(StorageUnit.MiB, StorageUnit.GiB,
                StorageUnit.TiB);
        DropDownChoice<StorageUnit> quotaUnitChoice;
        quotaUnitChoice = new DropDownChoice<StorageUnit>("globalQuotaUnits", unitModel, units);
        add(quotaUnitChoice);
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

        add(statusBar);
    }

    private GWC getGWC() {
        return gwcModel.getObject();
    }
}
