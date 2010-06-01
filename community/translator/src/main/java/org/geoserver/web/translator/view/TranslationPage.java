/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.view;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.geoserver.web.translator.controller.TranslationController;
import org.geoserver.web.translator.model.TranslationSession;
import org.geoserver.web.wicket.GeoServerDataProvider;
import org.geoserver.web.wicket.GeoServerTablePanel;
import org.geoserver.web.wicket.SimpleAjaxLink;
import org.geoserver.web.wicket.GeoServerDataProvider.Property;

/**
 * Presents the list of current translation and some useful information about each translation
 * state.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public class TranslationPage extends TranslationBasePage {

    public TranslationPage() {
        super();
        setHeaderPanel(headerPanel());

        TranslationInfoProvider provider = new TranslationInfoProvider(new Model(getLocale()));
        // the table, and wire up selection change
        TranslationListPanel table = new TranslationListPanel("table", provider) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onSelectionUpdate(AjaxRequestTarget target) {
            }
        };
        table.setOutputMarkupId(true);
        add(table);
    }

    protected Component headerPanel() {
        Fragment header = new Fragment(HEADER_PANEL, "header", this);

        // the add button
        header.add(new BookmarkablePageLink("addTranslation", NewTranslationPage.class));

        return header;
    }

    public static class TranslationListPanel extends GeoServerTablePanel<TranslationInfo> {

        private static final long serialVersionUID = 2169030557551650227L;

        private transient NumberFormat numberFormat;

        public TranslationListPanel(final String id, final TranslationInfoProvider provider) {
            super(id, provider);
        }

        private NumberFormat getNumberFormat() {
            if (numberFormat == null) {
                Locale uiLocale = getLocale();
                numberFormat = NumberFormat.getPercentInstance(uiLocale);
                numberFormat.setMaximumFractionDigits(1);
            }
            return numberFormat;
        }

        @Override
        protected Component getComponentForProperty(final String id, final IModel itemModel,
                final Property<TranslationInfo> property) {

            final IModel valueModel = property.getModel(itemModel);

            if (property == TranslationInfoProvider.LOCALE) {
                final Locale uiLocale = getLocale();
                final Locale locale = (Locale) valueModel.getObject();

                String linkLabel = locale.getDisplayName(uiLocale);
                if (linkLabel == null) {
                    linkLabel = locale.getDisplayName();
                }
                linkLabel = linkLabel == null ? "" : linkLabel + (" <" + locale.toString() + ">");

                return new SimpleAjaxLink(id, new Model(locale), new Model(linkLabel)) {
                    private static final long serialVersionUID = 1L;

                    @Override
                    public void onClick(final AjaxRequestTarget target) {
                        Locale locale = (Locale) getModelObject();
                        setResponsePage(new TranslationEditPage(locale));
                    }
                };
            } else if (property == TranslationInfoProvider.COMPLETE) {

                Double percent = (Double) valueModel.getObject();
                String percentLabel = getNumberFormat().format(percent / 100);
                return new Label(id, percentLabel);

            } else if (property == TranslationInfoProvider.UNCOMMITTED) {

                Double percent = (Double) valueModel.getObject();
                String percentLabel = getNumberFormat().format(percent / 100);
                return new Label(id, percentLabel);

            } else {
                throw new IllegalArgumentException("Unknown property: " + property);
            }
        }

    }

    private static class TranslationInfoProvider extends GeoServerDataProvider<TranslationInfo> {

        private static final long serialVersionUID = 1435687072659656308L;

        private static final Property<TranslationInfo> LOCALE = new BeanProperty<TranslationInfo>(
                "locale", "locale");

        private static final Property<TranslationInfo> COMPLETE = new BeanProperty<TranslationInfo>(
                "percentComplete", "percentComplete");

        private static final Property<TranslationInfo> UNCOMMITTED = new BeanProperty<TranslationInfo>(
                "uncommittedPercent", "uncommittedPercent");

        private IModel uiLocaleModel;

        private transient Comparator<TranslationInfo> localeNameComparator;

        /**
         * @param uiLocaleModel
         *            needed so the locale comparator can work against the current UI locale
         */
        public TranslationInfoProvider(final IModel uiLocaleModel) {
            this.uiLocaleModel = uiLocaleModel;
        }

        @Override
        protected List<TranslationInfo> getItems() {
            TranslationController controller = TranslationController.get();
            TranslationSession session = TranslationController.getTranslationSession();
            Set<Locale> translatedLanguages = controller.getInprogressLanguages(session);
            ArrayList<TranslationInfo> list = new ArrayList<TranslationInfo>(translatedLanguages
                    .size());

            for (Locale locale : translatedLanguages) {
                TranslationInfo info = new TranslationInfo();
                info.setLocale(locale);
                double percentComplete = controller.getPercentComplete(locale);
                double uncommittedPercent = controller.getUncommittedPercent(locale);
                info.setPercentComplete(percentComplete);
                info.setUncommittedPercent(uncommittedPercent);
                list.add(info);
            }
            return list;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected List<Property<TranslationInfo>> getProperties() {
            return Arrays.asList(LOCALE, COMPLETE, UNCOMMITTED);
        }

        public IModel model(Object translationInfo) {
            return new Model((TranslationInfo) translationInfo);
        }

        @Override
        protected Comparator<TranslationInfo> getComparator(SortParam sort) {
            if (sort == null || sort.getProperty() == null) {
                return null;
            }
            if (sort.getProperty().equals(LOCALE.getName())) {
                if (localeNameComparator == null) {
                    localeNameComparator = new Comparator<TranslationInfo>() {

                        public int compare(TranslationInfo o1, TranslationInfo o2) {
                            Locale uiLocale = (Locale) uiLocaleModel.getObject();
                            Locale l1 = o1.getLocale();
                            Locale l2 = o2.getLocale();
                            String dn1 = l1.getDisplayName(uiLocale);
                            String dn2 = l2.getDisplayName(uiLocale);
                            return dn1.compareTo(dn2);
                        }
                    };
                }
                if (sort.isAscending()) {
                    return localeNameComparator;
                } else {
                    return new org.apache.commons.collections.comparators.ReverseComparator(
                            localeNameComparator);
                }
            }
            return super.getComparator(sort);
        }
    }

    public static class TranslationInfo implements Serializable {

        private static final long serialVersionUID = 56794286983639144L;

        private Locale locale;

        private double percentComplete;

        private double uncommittedPercent;

        public Locale getLocale() {
            return locale;
        }

        public double getUncommittedPercent() {
            return uncommittedPercent;
        }

        public void setUncommittedPercent(double uncommittedPercent) {
            this.uncommittedPercent = uncommittedPercent;
        }

        public void setLocale(Locale locale) {
            this.locale = locale;
        }

        public double getPercentComplete() {
            return percentComplete;
        }

        public void setPercentComplete(double percentComplete) {
            this.percentComplete = percentComplete;
        }
    }
}
