/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.controller;

import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.geoserver.web.GeoServerStringResourceLoader;
import org.geoserver.web.translator.model.TranslationSession;

/**
 * An {@link IStringResourceLoader} that engages through application context and provides live
 * resource strings for the GeoServer UI based on the ongoing translations.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
public final class TranslationStringResourceLoader extends GeoServerStringResourceLoader {
    private TranslationSession translateLiveState;

    @Override
    @SuppressWarnings("unchecked")
    public String loadStringResource(final Class clazz, final String key, final Locale locale,
            final String style) {
        final TranslationSession state = getState();
        if (state == null) {
            return null;
        }
        String resource = state.getResource(locale, key);
        if (key.equals("category.extras")) {
            resource = state.getResource(locale, key);
            return resource;
        }
        if (resource != null) {
            System.err.println("found " + key + ": " + resource);
            return resource;
        }
        return resource;
    }

    @Override
    public String loadStringResource(final Component component, final String key) {
        if (getState() == null) {
            return null;
        }
        return super.loadStringResource(component, key);
    }

    private TranslationSession getState() {
        return TranslationController.getTranslationSession();
    }
}