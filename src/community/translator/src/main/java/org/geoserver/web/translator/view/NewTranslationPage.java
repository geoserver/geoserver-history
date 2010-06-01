/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.geoserver.web.GeoServerBasePage;

/**
 * A page to start the translation for a new {@link Locale}.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public class NewTranslationPage extends GeoServerBasePage {

    private Component newLanguageChoice;

    private Form newTranslationForm;

    private LocaleInputField localeInput;

    public NewTranslationPage() {
        super();

        add(newTranslationForm = new Form("newTranslationForm"));

        newLanguageChoice = newLanguageChoice();
        newTranslationForm.add(newLanguageChoice);
        // newTranslationForm.add(new CheckBox("showNativeLanguageNames"));

        newTranslationForm.add(createNewTranslationLink());
        newTranslationForm.add(cancelLink());
        localeInput = new LocaleInputField("localeInput");
        localeInput.setOutputMarkupId(true);
        newTranslationForm.add(localeInput);
    }

    private Link cancelLink() {
        return new Link("cancel") {
            private static final long serialVersionUID = -765151069378795644L;

            @Override
            public void onClick() {
                setResponsePage(TranslationPage.class);
            }
        };
    }

    private Component newLanguageChoice() {
        // GeoServerDialog dialog = new GeoServerDialog("dialog");
        // add(dialog);
        // return new NewLanguageLink("newLanguage", dialog, translateForm.getModel());

        IModel userInterfaceLocaleModel = new Model(getLocale());
        IModel selectedLocaleModel = new Model(getLocale());
        IModel choices = new NonTranslatedLocalesDetachableModel();
        final LocaleDropDown localeDropDown = new LocaleDropDown("newTranslationLanguage",
                userInterfaceLocaleModel, selectedLocaleModel, choices);

        localeDropDown.add(new OnChangeAjaxBehavior() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(final AjaxRequestTarget target) {
                Locale newTranslation = (Locale) newLanguageChoice.getModelObject();
                localeInput.setValue(newTranslation);
                target.addComponent(localeInput);
            }
        });

        // localeDropDown.add(new AjaxFormSubmitBehavior(newTranslationForm, "onchange") {
        // private static final long serialVersionUID = 1L;
        //
        // @Override
        // protected void onSubmit(final AjaxRequestTarget target) {
        // }
        //
        // @Override
        // protected void onError(AjaxRequestTarget arg0) {
        // // TODO Auto-generated method stub
        //
        // }
        // });
        return localeDropDown;
    }

    private Component createNewTranslationLink() {
        SubmitLink submitLink = new SubmitLink("createLanguage", newTranslationForm) {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                Locale locale = (Locale) newLanguageChoice.getModelObject();
                TranslationEditPage editPage = new TranslationEditPage(locale);
                setResponsePage(editPage);
            }
        };

        return submitLink;
    }

    private class NonTranslatedLocalesDetachableModel extends LoadableDetachableModel {

        private static final long serialVersionUID = 8363152206993598511L;

        @Override
        protected Object load() {
            // TranslateBean translateState = (TranslateBean)
            // NewLanguagePanel.this.getModelObject();
            // Set<Locale> availableTranslations = translateState.getAvailableTranslations();

            Set<Locale> nonTranslated = new HashSet<Locale>(Arrays.asList(Locale
                    .getAvailableLocales()));
            // nonTranslated.removeAll(availableTranslations);

            List<Locale> choices = new ArrayList<Locale>(nonTranslated);
            {
                final Locale userInterfaceLocale = NewTranslationPage.this.getLocale();
                Collections.sort(choices, new Comparator<Locale>() {
                    public int compare(Locale o1, Locale o2) {
                        return o1.getDisplayName(o1).compareTo(o2.getDisplayName(o2));
                    }
                });
            }
            return choices;
        }
    }

    public static class LocaleInputField extends Panel {
        private static final long serialVersionUID = 1L;

        private TextField language;

        private TextField country;

        private TextField variant;

        public LocaleInputField(final String id) {
            super(id);
            add(language = new TextField("language", new Model("")));
            add(country = new TextField("country", new Model("")));
            add(variant = new TextField("variant", new Model("")));

            language.setRequired(true);
        }

        public void setValue(final Locale locale) {
            if (locale == null) {
                language.setModelObject(null);
                country.setModelObject(null);
                variant.setModelObject(null);
            } else {
                language.setModelObject(locale.getLanguage());
                country.setModelObject(locale.getCountry());
                variant.setModelObject(locale.getVariant());
            }
        }

        public String getLanguage() {
            return language.getValue() == null ? "" : language.getValue();
        }

        public String getCountry() {
            return country.getValue() == null ? "" : country.getValue();
        }

        public String getVariant() {
            return variant.getValue() == null ? "" : variant.getValue();
        }
    }

}
