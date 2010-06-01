/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.controller;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.Session;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.acegi.GeoServerSession;
import org.geoserver.web.translator.model.ResourceSet;
import org.geoserver.web.translator.model.TranslationSession;

/**
 * Worker class to control the state of translations.
 * <p>
 * A single instance of this class shall exist on the application context and be accessed as an
 * extension point using this {@code Class} object as the lookup key for
 * {@link GeoServerApplication#getBeanOfType(Class)}.
 * </p>
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public class TranslationController {

    /**
     * The key to store the current translation progress in the user's {@link Session}
     * 
     * @see #getTranslationSession(GeoServerSession)
     */
    private static final MetaDataKey TRANSLATION_BEAN = new MetaDataKey(TranslationSession.class) {
        private static final long serialVersionUID = 1L;
    };

    private final GeoServerResourceLoader resourceManager;

    /**
     * The resources shipped with GeoServer inside each module {@code
     * GeoServerApplication[_<locale>].properties} file
     */
    private final ResourceSet bundledResources;

    /**
     * The resources for translations persisted on {@code <data dir>/translations}
     */
    private final ResourceSet persistedResources;

    private final TranslationPersister resourcePersister;

    public TranslationController(GeoServerApplication app) throws IOException {
        this.resourceManager = app.getResourceLoader();
        GeoServerResourceLoader resourceLoader = app.getResourceLoader();
        this.resourcePersister = new TranslationPersister(resourceLoader);
        this.bundledResources = resourcePersister.loadBundledResources();
        this.persistedResources = resourcePersister.loadPersistedResources();
    }

    /**
     * Returns the missing keys for the {@code session}'s target language, computing both the
     * session state keys, the already persisted translations, and the geoserver bundled
     * translations.
     * 
     * @param state
     * @return
     */
    public Set<String> getTargetLocaleMissingKeys(final TranslationSession session) {
        Set<String> allKeys = bundledResources.getKeys(ResourceSet.DEFAULT_LOCALE);
        Set<String> bundledTranslatedKeys = bundledResources.getKeys(session.getTargetLanguage());
        Set<String> translatedKeys = persistedResources.getKeys(session.getTargetLanguage());
        Set<String> sessionKeys = session.getTargetLanguageKeys();

        Set<String> missingKeys = new HashSet<String>(allKeys);

        missingKeys.removeAll(bundledTranslatedKeys);
        missingKeys.removeAll(translatedKeys);
        missingKeys.remove(sessionKeys);

        return missingKeys;
    }

    /**
     * Returns the list of languages for which translations are being performed
     */
    public Set<Locale> getInprogressLanguages() {
        Set<Locale> bundled = bundledResources.getLocales();
        Set<Locale> inProgress = persistedResources.getLocales();

        Set<Locale> available = new HashSet<Locale>(bundled);
        available.addAll(inProgress);

        return available;
    }

    /**
     * Returns the list of languages for which translations are being performed, including the ones
     * in session that are not yet persisted
     */
    public Set<Locale> getInprogressLanguages(final TranslationSession session) {
        Set<Locale> available = getInprogressLanguages();
        Set<Locale> sessionLocales = session.getLocales();

        Set<Locale> translations = new HashSet<Locale>(available);
        translations.addAll(sessionLocales);
        return translations;
    }

    public int getTranslatedResourceCount(Locale locale) {
        int size = bundledResources.getKeys(locale).size();
        size += persistedResources.getKeys(locale).size();
        return size;
    }

    public int getTotalResoureCount() {
        int totalCount = getTranslatedResourceCount(ResourceSet.DEFAULT_LOCALE);
        if (totalCount == 0) {
            throw new RuntimeException("default resources!?");
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

    public static TranslationController get() {
        GeoServerApplication application = GeoServerApplication.get();
        return get(application);
    }

    public static TranslationController get(final GeoServerApplication application) {
        TranslationController controller = application.getBeanOfType(TranslationController.class);
        if (controller == null) {
            throw new IllegalStateException("No bean of type "
                    + TranslationController.class.getName() + " registered in application context");
        }
        return controller;
    }

    public static TranslationSession getTranslationSession() {
        Session session = Session.get();
        return getTranslationSession(session);
    }

    public static TranslationSession getTranslationSession(final Session session) {
        TranslationSession translationSession = (TranslationSession) session
                .getMetaData(TRANSLATION_BEAN);
        if (translationSession == null) {
            translationSession = new TranslationSession();
            session.setMetaData(TRANSLATION_BEAN, translationSession);
        }
        return translationSession;
    }

    public Set<String> getAllKeys() {
        Set<String> allKeys = bundledResources.getKeys(ResourceSet.DEFAULT_LOCALE);
        return allKeys;
    }

    /**
     * Looks up the resource for the given key and locale in the following order: session, persisted
     * translations, bundled translations
     * 
     * @param locale
     * @param key
     * @param session
     * @return
     */
    public String getResource(final Locale locale, final String key,
            final TranslationSession session) {
        String resource = session.getResource(locale, key);
        if (resource == null) {
            resource = this.persistedResources.get(locale, key);
        }
        if (resource == null) {
            resource = this.bundledResources.get(locale, key);
        }
        return resource;
    }
}
