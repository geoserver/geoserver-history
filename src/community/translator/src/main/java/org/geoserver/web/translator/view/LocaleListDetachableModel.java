/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.web.translator.controller.TranslationController;
import org.geoserver.web.translator.model.ResourceSet;
import org.geoserver.web.translator.model.TranslationSession;

/**
 * A model for the list of available translations
 */
public class LocaleListDetachableModel extends LoadableDetachableModel {

    private static final long serialVersionUID = 1L;

    private boolean includeDefault;

    private IModel uiLocaleModel;

    public LocaleListDetachableModel(final boolean includeDefault, final IModel uiLocaleModel) {
        this.includeDefault = includeDefault;
        this.uiLocaleModel = uiLocaleModel;
    }

    @Override
    protected Object load() {
        Set<Locale> availableTranslations;
        {
            TranslationSession translationSession = TranslationController.getTranslationSession();
            TranslationController controller = TranslationController.get();
            availableTranslations = controller.getInprogressLanguages(translationSession);
            availableTranslations = new HashSet<Locale>(availableTranslations);
            // add target language in case its a new translation and hence there aren't still
            // resources for it
            if (null != translationSession.getTargetLanguage()) {
                availableTranslations.add(translationSession.getTargetLanguage());
            }
        }
        List<Locale> sorted = new ArrayList<Locale>(availableTranslations);
        if (!includeDefault) {
            sorted.remove(ResourceSet.DEFAULT_LOCALE);
        }
        // Collections.sort(sorted, new Comparator<Locale>() {
        // public int compare(Locale o1, Locale o2) {
        // Locale uiLocale = (Locale) uiLocaleModel.getObject();
        // String displayName1 = o1.getDisplayName(uiLocale);
        // String displayName2 = o2.getDisplayName(uiLocale);
        // return displayName1.compareTo(displayName2);
        // }
        // });
        return sorted;
    }
}