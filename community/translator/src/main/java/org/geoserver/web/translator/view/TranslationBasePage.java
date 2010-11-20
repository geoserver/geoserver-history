package org.geoserver.web.translator.view;

import org.geoserver.web.GeoServerApplication;
import org.geoserver.web.GeoServerBasePage;
import org.geoserver.web.spring.security.GeoServerSession;
import org.geoserver.web.translator.controller.TranslationController;
import org.geoserver.web.translator.model.TranslationSession;

public class TranslationBasePage extends GeoServerBasePage {

    private transient TranslationController controller;

    protected final TranslationSession getTranslationSession() {
        GeoServerSession session = getSession();
        return TranslationController.getTranslationSession(session);
    }

    protected final TranslationController getController() {
        if (controller == null) {
            GeoServerApplication application = getGeoServerApplication();
            controller = TranslationController.get(application);
        }
        return controller;
    }
}
