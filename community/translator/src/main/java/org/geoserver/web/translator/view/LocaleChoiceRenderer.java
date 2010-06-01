/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.view;

import java.util.Locale;

import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

/**
 * An {@link IChoiceRenderer} for a {@link Locale} that displays the
 * {@link Locale#getDisplayName(Locale) locale name} in the user interface locale.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 */
class LocaleChoiceRenderer implements IChoiceRenderer {

    private static final long serialVersionUID = 1L;

    private final IModel userInterfaceLocaleModel;

    /**
     * @param userInterfaceLocaleModel
     *            the model for the user interface {@link Locale}
     */
    public LocaleChoiceRenderer(final IModel userInterfaceLocaleModel) {
        this.userInterfaceLocaleModel = userInterfaceLocaleModel;
    }

    /**
     * @return the locale display name in the user interface locale
     */
    public Object getDisplayValue(Object l) {
        final Locale uiLocale = (Locale) userInterfaceLocaleModel.getObject();
        Locale locale = (Locale) l;
        String displayName = locale.getDisplayName(locale);
        if (!uiLocale.getLanguage().equals(locale.getLanguage())) {
            displayName = displayName + " <" + locale.getDisplayName(uiLocale) + ">";
        }
        return displayName;
    }

    /**
     * @return the locale identifier
     */
    public String getIdValue(Object l, int index) {
        Locale locale = (Locale) l;
        return locale.toString();
    }
}