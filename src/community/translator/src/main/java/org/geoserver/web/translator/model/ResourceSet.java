package org.geoserver.web.translator.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Collection of translation resources on a per {@link Locale} basis
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @version $Id$
 * @since 2.0
 */
public class ResourceSet implements Serializable {

    private static final long serialVersionUID = -8223399140308098435L;

    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    private final Map<Locale, Map<String, String>> resources;

    public ResourceSet() {
        this(new HashMap<Locale, Map<String, String>>());
    }

    public ResourceSet(final Map<Locale, Map<String, String>> initialState) {
        this.resources = new HashMap<Locale, Map<String, String>>(initialState);
    }

    public boolean containsLocale(final Locale locale) {
        return resources.keySet().contains(locale);
    }

    public Set<Locale> getLocales() {
        return Collections.unmodifiableSet(resources.keySet());
    }

    public boolean containsKey(final Locale locale, final String key) {
        Map<String, String> langResources = resources.get(locale);
        if (langResources == null) {
            return false;
        }
        return langResources.containsKey(key);
    }

    public Set<String> getKeys(final Locale locale) {
        Map<String, String> localeResources = resources.get(locale);
        if (localeResources == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(localeResources.keySet());
    }

    public String get(final Locale locale, final String key) {
        Map<String, String> langResources = resources.get(locale);
        if (langResources == null) {
            return null;
        }
        return langResources.get(key);
    }

    public void put(final Locale locale, final String key, final String resource) {
        Map<String, String> langResources = resources.get(locale);
        if (langResources == null) {
            langResources = new HashMap<String, String>();
            resources.put(locale, langResources);
        }

        langResources.put(key, resource);
    }
}
