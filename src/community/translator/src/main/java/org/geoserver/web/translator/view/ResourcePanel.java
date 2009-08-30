/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.view;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.web.translator.view.TranslationEditPage.LocaleListDetachableModel;

/**
 * A base panel with a text area whose model is the translated resource for the current key.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 * @see Base
 * @see Target
 */
abstract class ResourcePanel extends Panel {

    private static final long serialVersionUID = 5645666151936813854L;

    public ResourcePanel(final String id, final IModel resourceModel, final boolean enabled) {
        super(id);
        setOutputMarkupId(true);
        TextArea textArea = new TextArea("textArea", resourceModel);
        textArea.setEnabled(enabled);
        add(textArea);
    }

    /**
     * The panel for the base language resource string
     */
    public static class Base extends ResourcePanel {

        private static final long serialVersionUID = 2766414564542792829L;

        public Base(final String id, final IModel resourceModel, final IModel localeStringModel) {
            super(id, resourceModel, false);
            add(new Label("language", localeStringModel));
        }

    }

    /**
     * The panel to edit the target language resource string
     */
    public static class Target extends ResourcePanel {

        private static final long serialVersionUID = -6616465412272222343L;

        public Target(final String id, final IModel resourceModel, final IModel localeSelectionModel) {
            super(id, resourceModel, true);
            setModel(localeSelectionModel);
            add(targetLanguageChoice());
        }

        private Component targetLanguageChoice() {
            final IModel selectionModel = getModel();
            IModel choices = new LocaleListDetachableModel(false, new Model(getLocale()));
            final IModel sessionModel = new Model(getSession());
            final IModel uiLocaleModel = new PropertyModel(sessionModel, "locale");
            final Component targetLanguageChoice = new LocaleDropDown("targetLanguage",
                    uiLocaleModel, selectionModel, choices);

            /*
             * Use a different form for the target language drop down so when submitting it doesn't
             * set the current resource key value to the one of the old language
             */
            final Form targetLanguageForm = new Form("targetLanguageForm", selectionModel);

            targetLanguageChoice.add(new AjaxFormSubmitBehavior(targetLanguageForm, "onChange") {

                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(final AjaxRequestTarget target) {
                    // System.out.println(targetLanguageChoice.getModelObject());
                    setResponsePage(TranslationEditPage.class);
                }

                @Override
                protected void onError(final AjaxRequestTarget target) {
                    // TODO Auto-generated method stub
                }
            });

            targetLanguageForm.add(targetLanguageChoice);
            return targetLanguageForm;
        }
    }
}
