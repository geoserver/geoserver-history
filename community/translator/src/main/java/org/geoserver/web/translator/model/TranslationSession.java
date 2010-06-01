/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.model;

import java.io.Serializable;
import java.util.Locale;
import java.util.Set;

/**
 * Maintains the state of the ongoing translations.
 * <p>
 * REVISIT: this class oughta be factored out in two, one for the session properties and another for
 * the actual translation state
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public class TranslationSession implements Serializable {

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

    private ResourceSet translatingResources;

    public TranslationSession() {
        translatingResources = new ResourceSet();
        baseLocale = ResourceSet.DEFAULT_LOCALE;
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

    public String getCurrentResource() {
        Locale targetLanguage = getTargetLanguage();
        String currentResource = getCurrentKeyResource(targetLanguage);
        return currentResource;
    }

    public void setCurrentResource(final String currentResource) {
        final Locale targetLanguage = getTargetLanguage();
        final String currentKey = getCurrentKey();

        translatingResources.put(targetLanguage, currentKey, currentResource);
    }

    private String getCurrentKeyResource(final Locale targetLanguage) {
        final String currentKey = getCurrentKey();
        return translatingResources.get(targetLanguage, currentKey);
    }

    /**
     * @return the current target language resource keys for this session
     */
    public Set<String> getTargetLanguageKeys() {
        return translatingResources.getKeys(getTargetLanguage());
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
    }

    public String getResource(final Locale locale, final String key) {
        return translatingResources.get(locale, key);
    }

    public Set<Locale> getLocales() {
        return translatingResources.getLocales();
    }
}
