/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.web.GeoServerApplication;
import org.springframework.core.io.Resource;

/**
 * Worker class to control the state of translations.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public class TranslationController {

    private final GeoServerResourceLoader resourceManager;

    private Map<Locale, Map<String, String>> resources;

    public TranslationController(GeoServerApplication app) {
        this.resourceManager = app.getResourceLoader();
        this.resources = loadResources();
    }

    /**
     * Returns the list of available translations.
     */
    public Set<Locale> getTranslatedLanguages() {
        Map<Locale, Map<String, String>> translatedResources = getTranslatedResources();
        Set<Locale> alreadyTranslatedLanguages = new HashSet<Locale>(translatedResources.keySet());
        // remove the default locale, which is keyed as null
        alreadyTranslatedLanguages.remove(null);

        return alreadyTranslatedLanguages;
    }

    /**
     * Returns the list of languages for which tranlations are being performed
     */
    public Set<Locale> getInprogressLanguages() {
        Set<Locale> inProgress = new HashSet<Locale>();

        Resource translationsFolder = resourceManager.getResource("translations");
        if (translationsFolder != null) {

        }
        return inProgress;
    }

    public Map<Locale, Map<String, String>> getTranslatedResources() {
        return resources;
    }

    private Map<Locale, Map<String, String>> loadResources() {

        Map<Locale, Map<String, String>> resources = new HashMap<Locale, Map<String, String>>();

        final ClassLoader classLoader = getClass().getClassLoader();

        final ArrayList<URL> locations;
        try {
            locations = Collections.list(classLoader
                    .getResources("GeoServerApplication.properties"));
            Map<String, String> defaultLocaleResources = loadResources(locations);
            resources.put(null, defaultLocaleResources);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        final Locale[] availableLocales = Locale.getAvailableLocales();

        String localeProgrammaticName;
        String resourceName;
        for (Locale locale : availableLocales) {
            localeProgrammaticName = locale.toString();
            resourceName = "GeoServerApplication_" + localeProgrammaticName + ".properties";
            ArrayList<URL> localeLocations;
            try {
                localeLocations = Collections.list(classLoader.getResources(resourceName));
                Map<String, String> localeResources = loadResources(localeLocations);
                if (localeResources.size() > 0) {
                    resources.put(locale, localeResources);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resources;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> loadResources(final List<URL> locations) {
        Map<String, String> resources = new HashMap<String, String>();

        for (URL url : locations) {
            InputStream in;
            try {
                in = url.openStream();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            Properties properties = new Properties();
            try {
                properties.load(in);
                resources.putAll((Map) properties);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return resources;
    }

    public Map<String, String> getResources(final Locale locale) {
        return resources.get(locale);
    }

    public int getTranslatedResourceCount(Locale locale) {
        Map<String, String> localeResources = getResources(locale);
        if (localeResources == null) {
            return 0;
        }
        return localeResources.size();
    }

    public int getTotalResoureCount() {
        int totalCount = getTranslatedResourceCount(null);
        if (totalCount == 0) {
            throw new NullPointerException("default resources!?");
        }
        return totalCount;
    }

    public double getPercentComplete(final Locale locale) {
        final double totalCount = getTotalResoureCount();
        final double localeCount = getTranslatedResourceCount(locale);

        return localeCount * 100 / totalCount;
    }

    /**
     * NOT YET IMPLEMENTED
     * 
     * @param locale
     * @return
     */
    public double getUncommittedPercent(Locale locale) {
        return 0;
    }
}
