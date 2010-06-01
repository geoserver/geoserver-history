/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.view;

import java.io.Serializable;
import java.util.Locale;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.model.StringResourceModel;
import org.geoserver.web.translator.controller.TranslationController;
import org.geoserver.web.translator.model.TranslationSession;

/**
 * Groups the base and target resource panels
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public class TranslationResourcePanel extends Panel {

    private static final long serialVersionUID = 4361785407531888335L;

    ResourcePanel baseLanguagePanel;

    ResourcePanel translatingResourcePanel;

    public TranslationResourcePanel(final String id, final Form translateForm) {
        super(id);
        setOutputMarkupId(true);

        // add(translateForm = new Form("translateForm", translateForm2));
        final IModel languageNameBeanModel = new Model(new LocaleNameBean());
        // model for the translate-from Locale i18n string
        final IModel baseLanguageMessageModel = new StringResourceModel(
                "TranslationResourcePanel.baseLanguage", languageNameBeanModel);

        // The model to update the current resource from the base language
        final IModel currentBaseResourceModel;
        // The model to update and set the translated resource for the current key
        final IModel currentTranslatingResourceModel;

        IModel translateStateModel = translateForm.getModel();
        setModel(translateStateModel);
        PropertyModel keyModel = new PropertyModel(translateStateModel, "currentKey");
        currentBaseResourceModel = new TranslationStringResourceModel(translateStateModel,
                new PropertyModel(translateStateModel, "baseLocale"), keyModel, null, true);

        currentTranslatingResourceModel = new TranslationStringResourceModel(translateStateModel,
                new PropertyModel(translateStateModel, "targetLanguage"), keyModel,
                new PropertyModel(translateStateModel, "currentResource"), false);

        baseLanguagePanel = new ResourcePanel.Base("baseLanguagePanel", currentBaseResourceModel,
                baseLanguageMessageModel);

        IModel targetLanguageModel = new PropertyModel(translateStateModel, "targetLanguage");
        translatingResourcePanel = new ResourcePanel.Target("targetLanguagePanel",
                currentTranslatingResourceModel, targetLanguageModel);

        add(baseLanguagePanel);
        add(translatingResourcePanel);
    }

    public String getTranslatedResource() {
        return translatingResourcePanel.textArea.getValue();
    }

    class LocaleNameBean implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * @return the locale name in the {@link #getBaseLocale() translate from locale}
         */
        public String getBaseLocaleName() {
            final Locale uiLocale = getLocale();
            final Locale baseLocale = getBaseLocale();
            if (baseLocale == null) {
                return (String) new ResourceModel("TranslationResourcePanel.defaultLocale")
                        .getObject();
            }
            return baseLocale.getDisplayName(uiLocale);
        }

        private Locale getBaseLocale() {
            return (Locale) new PropertyModel(getModel(), "baseLocale").getObject();
        }

        public String getTargetLanguageName() {
            final Locale uiLocale = getLocale();
            IModel targetLocaleModel = new PropertyModel(getModel(), "targetLanguage");
            final Locale targetLocale = (Locale) targetLocaleModel.getObject();
            if (targetLocale == null) {
                return (String) new ResourceModel("TranslationResourcePanel.defaultLocale")
                        .getObject();
            }
            if (uiLocale == null) {
                return targetLocale.getDisplayName();
            }
            return targetLocale.getDisplayName(uiLocale);
        }
    }

    public void refresh(final AjaxRequestTarget target) {
        TranslationSession session = (TranslationSession) getModel().getObject();
        TranslationController controller = TranslationController.get();
        String currentKey = session.getCurrentKey();

        String baseResource = controller.getResource(session.getBaseLocale(), currentKey, session);
        String currentResource = controller.getResource(session.getTargetLanguage(), currentKey,
                session);

        baseLanguagePanel.textArea.setModelObject(baseResource);
        translatingResourcePanel.textArea.setModelObject(currentResource);
        target.addComponent(baseLanguagePanel);
        target.addComponent(translatingResourcePanel);
    }

}
