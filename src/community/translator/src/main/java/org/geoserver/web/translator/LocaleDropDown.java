package org.geoserver.web.translator;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

public class LocaleDropDown extends DropDownChoice {

    private static final long serialVersionUID = -5798266373824053771L;

    public LocaleDropDown(final String id, final IModel userInterfaceLocaleModel,
            final IModel selectedLocaleModel, final IModel choices) {

        super(id, selectedLocaleModel, choices);

        setChoiceRenderer(new LocaleChoiceRenderer(userInterfaceLocaleModel));
    }

    public LocaleDropDown(final String id, final IModel userInterfaceLocaleModel,
            final IModel selectedLocaleModel, final List<Locale> choices) {

        super(id, selectedLocaleModel, choices);

        setChoiceRenderer(new LocaleChoiceRenderer(userInterfaceLocaleModel));
    }
}
