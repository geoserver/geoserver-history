package org.geoserver.web.translator;

import java.util.Locale;

import org.apache.wicket.Component;
import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerStringResourceLoader;

public final class TranslationStringResourceLoader extends GeoServerStringResourceLoader {
    private TranslateBean translateLiveState;

    @SuppressWarnings("unchecked")
    public String loadStringResource(final Class clazz, final String key, final Locale locale,
            final String style) {
        final TranslateBean state = getState();
        if (state == null) {
            return null;
        }
        String resource = state.getResource(locale, key);
        if(key.equals("category.extras")){
            resource = state.getResource(locale, key);
            return resource;
        }
        if (resource != null) {
            System.err.println("found " + key + ": " + resource);
            return resource;
        }
        return resource;
    }

    public String loadStringResource(final Component component, final String key) {
        if (getState() == null) {
            return null;
        }
        return super.loadStringResource(component, key);
    }

    private TranslateBean getState() {
        if (translateLiveState == null) {
            translateLiveState = (TranslateBean) GeoServerApplication.get().getMetaData(
                    TranslationEditPage.TRANSLATION_BEAN);
        }
        return translateLiveState;
    }
}