/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Maintains the state of the ongoing translations.
 * <p>
 * REVISIT: this class oughta to be factored out in two, one for the session properties and another
 * for the actual translation state
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public class TranslateBean implements Serializable {

    private static final long serialVersionUID = -8138420553898567646L;

    private Locale baseLocale;

    private boolean showMissingOnly;

    private Locale targetLanguage;

    private String currentKey;

    private String filter;

    /**
     * Whether to show the tree as flat view. If false it'll be a tree.
     */
    private boolean flatView;

    private HashMap<Locale, HashMap<String, String>> availableResources;

    public TranslateBean(final Locale baseLocale,
            Map<Locale, Map<String, String>> availableResources) {
        this.baseLocale = baseLocale;
        this.availableResources = new HashMap<Locale, HashMap<String, String>>();
        for (Map.Entry<Locale, Map<String, String>> entry : availableResources.entrySet()) {
            Locale key = entry.getKey();
            Map<String, String> value = entry.getValue();
            this.availableResources.put(key, new HashMap<String, String>(value));
        }
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Locale getBaseLocale() {
        return baseLocale;
    }

    public void setBaseLocale(Locale baseLocale) {
        this.baseLocale = baseLocale;
    }

    public boolean isFlatView() {
        return flatView;
    }

    public void setFlatView(boolean flatView) {
        this.flatView = flatView;
    }

    public String getCurrentKey() {
        return currentKey;
    }

    public void setCurrentKey(String currentKey) {
        this.currentKey = currentKey;
    }

    public String getCurrentBaseResource() {
        String currentResource = getCurrentKeyResource(baseLocale);
        return currentResource;
    }

    public String getCurrentResource() {
        Locale targetLanguage = getTargetLanguage();
        String currentResource = getCurrentKeyResource(targetLanguage);
        return currentResource;
    }

    public void setCurrentResource(final String currentResource) {
        if (currentResource == null) {
            return;
        }
        final String currentKey = getCurrentKey();

        Locale targetLanguage = getTargetLanguage();
        Map<String, String> languageResources = getLanguageResources(targetLanguage);

        languageResources.put(currentKey, currentResource);
    }

    private String getCurrentKeyResource(final Locale targetLanguage) {
        final String currentKey = getCurrentKey();
        Map<String, String> languageResources = getLanguageResources(targetLanguage);
        String currentResource = languageResources.get(currentKey);
        return currentResource;
    }

    private Map<String, String> getLanguageResources(final Locale locale) {
        Map<String, String> languageResources;
        HashMap<Locale, HashMap<String, String>> resources = getResources();
        languageResources = resources.get(locale);
        if (languageResources == null) {
            throw new IllegalArgumentException("No translation for " + locale);
        }
        return languageResources;
    }

    private HashMap<Locale, HashMap<String, String>> getResources() {
        return availableResources;
    }

    public Set<String> getMissingKeys() {
        Set<String> allKeys = getResourceKeys();
        Set<String> translatedKeys = getLanguageResources(getTargetLanguage()).keySet();
        Set<String> missingKeys = new HashSet<String>(allKeys);
        missingKeys.removeAll(translatedKeys);
        return missingKeys;
    }

    public Set<String> getResourceKeys() {
        Set<String> keys = Collections.unmodifiableSet(availableResources.get(null).keySet());
        return keys;
    }

    public boolean isShowMissingOnly() {
        return showMissingOnly;
    }

    public void setShowMissingOnly(boolean showMissingOnly) {
        this.showMissingOnly = showMissingOnly;
    }

    public Locale getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(Locale targetLanguage) {
        this.targetLanguage = targetLanguage;
        if (targetLanguage != null) {
            HashMap<Locale, HashMap<String, String>> resources = getResources();
            if (!resources.containsKey(targetLanguage)) {
                resources.put(targetLanguage, new HashMap<String, String>());
            }
        }
    }

    public String getResource(final Locale locale, final String key) {
        if (!getResources().containsKey(locale)) {
            return null;
        }
        Map<String, String> languageResources = getLanguageResources(locale);
        String string = languageResources.get(key);
        return string;
    }

    public Set<Locale> getAvailableTranslations() {
        Set<Locale> locales = new HashSet<Locale>(getResources().keySet());
        locales.remove(null);// remove default
        return locales;
    }
}
