package org.geoserver.web.translator.view;

import java.util.Locale;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.geoserver.web.translator.controller.TranslationController;
import org.geoserver.web.translator.model.TranslationSession;

public class TranslationStringResourceModel implements IModel {

    private static final long serialVersionUID = -8629227559840607L;

    private final ResourceDetachableModel readModel;

    private final boolean readOnly;

    public TranslationStringResourceModel(final IModel translationSessionModel,
            final IModel localeModel, final IModel keyModel, final IModel resourceModel,
            final boolean readOnly) {
        this.readModel = new ResourceDetachableModel(translationSessionModel, localeModel,
                keyModel, resourceModel);
        this.readOnly = readOnly;
    }

    public Object getObject() {
        return readModel.getObject();
    }

    /**
     * Sets the value, or ignores it if {@code #readOnly}
     */
    public void setObject(Object value) {
        if (readOnly) {
            return;
        }

        readModel.resourceModel.setObject(value);
    }

    public void detach() {
        readModel.detach();
    }

    private static class ResourceDetachableModel extends LoadableDetachableModel {

        private static final long serialVersionUID = -675380807934599690L;

        final IModel keyModel;

        final IModel localeModel;

        final IModel translationSessionModel;

        final IModel resourceModel;

        public ResourceDetachableModel(IModel translationSessionModel, IModel localeModel,
                IModel keyModel, IModel resourceModel) {
            this.translationSessionModel = translationSessionModel;
            this.localeModel = localeModel;
            this.keyModel = keyModel;
            this.resourceModel = resourceModel;
        }

        @Override
        protected Object load() {
            TranslationSession session = (TranslationSession) translationSessionModel.getObject();
            String key = (String) keyModel.getObject();
            Locale locale = (Locale) localeModel.getObject();

            TranslationController controller = TranslationController.get();
            String resource = controller.getResource(locale, key, session);
            return resource;
        }
    }
}
