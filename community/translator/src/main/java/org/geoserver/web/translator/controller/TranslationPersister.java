package org.geoserver.web.translator.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.web.translator.model.ResourceSet;

/**
 * Handles precision and packaging for ongoing translations
 * 
 * @author Gabriel Roldan
 */
class TranslationPersister {

    private final GeoServerResourceLoader resourceLoader;

    public TranslationPersister(final GeoServerResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public ResourceSet loadBundledResources() throws IOException {

        Map<Locale, Map<String, String>> resources = new HashMap<Locale, Map<String, String>>();

        final ClassLoader classLoader = resourceLoader.getClassLoader();

        final ArrayList<URL> locations;
        try {
            locations = Collections.list(classLoader
                    .getResources("GeoServerApplication.properties"));
            Map<String, String> defaultLocaleResources = loadResources(locations);
            resources.put(ResourceSet.DEFAULT_LOCALE, defaultLocaleResources);
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
            localeLocations = Collections.list(classLoader.getResources(resourceName));
            Map<String, String> localeResources = loadResources(localeLocations);
            if (localeResources.size() > 0) {
                resources.put(locale, localeResources);
            }
        }

        ResourceSet bundled = new ResourceSet(resources);
        return bundled;
    }

    public ResourceSet loadPersistedResources() throws IOException {
        ResourceSet persistedTranslations = new ResourceSet();

        final File translationsFolder = resourceLoader.find("translations");
        if (translationsFolder == null) {
            return persistedTranslations;
        }
        if (!translationsFolder.exists()) {
            return persistedTranslations;
        }
        if (!translationsFolder.isDirectory() || !translationsFolder.canRead()
                || !translationsFolder.canWrite()) {
            throw new RuntimeException(translationsFolder.getAbsolutePath()
                    + " is not a readable and writable directory");
        }

        File[] bundles = translationsFolder.listFiles(new FilenameFilter() {
            public boolean accept(final File dir, final String name) {
                return name.matches("GeoServerApplication.*\\.properties");
            }
        });

        // This time there's only a single GeoServerApplication_<locale>.properties per locale
        for (File bundle : bundles) {
            Locale locale = parseLocaleFromFileName(bundle.getName());
            Map<String, String> resources = loadResources(bundle.toURI().toURL());
            for (Map.Entry<String, String> entry : resources.entrySet()) {
                persistedTranslations.put(locale, entry.getKey(), entry.getValue());
            }
        }

        return persistedTranslations;
    }

    private Locale parseLocaleFromFileName(final String fileName) {
        // fileName shall be of the form "GeoServerApplication_<locale>.properties"
        final String localeStr = fileName.substring(1 + fileName.indexOf('_'), fileName
                .indexOf('.'));
        String[] localeParts = localeStr.split("_");
        String language = localeParts[0];
        String country = localeParts.length > 1 ? localeParts[1] : "";
        String variant = localeParts.length > 2 ? localeParts[2] : "";

        Locale locale = new Locale(language, country, variant);
        return locale;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> loadResources(final List<URL> locations) {
        Map<String, String> resources = new HashMap<String, String>();

        for (URL url : locations) {
            Map<String, String> properties;
            try {
                properties = loadResources(url);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            resources.putAll((Map) properties);
        }

        return resources;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> loadResources(URL url) throws IOException {
        InputStream in;

        in = url.openStream();

        Properties properties = new Properties();
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Map<String, String> props = new HashMap<String, String>();

        props.putAll((Map) properties);

        return props;
    }
}
