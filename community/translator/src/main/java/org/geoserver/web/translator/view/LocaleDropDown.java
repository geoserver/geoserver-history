/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.translator.view;

import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

/**
 * A {@link DropDownChoice} to select one of the provided {@link Locale locales}.
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.0
 * @see LocaleChoiceRenderer
 */
public class LocaleDropDown extends DropDownChoice {

    private static final long serialVersionUID = -5798266373824053771L;

    /**
     * 
     * @param id
     *            the component id
     * @param userInterfaceLocaleModel
     *            the model for the user interface {@link Locale}, used to present the locale names
     * @param selectedLocaleModel
     *            the model to update upon a locale selection
     * @param choices
     *            the model over the list of {@link Locale}s to present
     */
    public LocaleDropDown(final String id, final IModel userInterfaceLocaleModel,
            final IModel selectedLocaleModel, final IModel choices) {

        super(id, selectedLocaleModel, choices);

        setChoiceRenderer(new LocaleChoiceRenderer(userInterfaceLocaleModel));
    }
}
