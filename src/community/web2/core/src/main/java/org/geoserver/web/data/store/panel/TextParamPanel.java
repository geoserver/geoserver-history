/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidator;

/**
 * A label with a text field. Can receive custom validators for the text field.
 * @author Gabriel Roldan
 */
@SuppressWarnings("serial")
public class TextParamPanel extends Panel {

    /**
     * 
     * @param id
     * @param paramsMap
     * @param paramName
     * @param paramLabel
     * @param required
     * @param validators
     *            any extra validator that should be added to the input field,
     *            or {@code null}
     */
    public TextParamPanel(final String id, IModel paramVale, String paramLabel, 
                          final boolean required, IValidator... validators) {
        // make the value of the text field the model of this panel, for easy value retriaval
        super(id, paramVale);

        // the label
        Label label = new Label("paramName", paramLabel);
        add(label);

        // the text field, with a decorator for validations
        TextField textField = new TextField("paramValue", paramVale);
        textField.setRequired(required);
        if(validators != null) {
            for(IValidator validator : validators){
                textField.add(validator);
            }
        }
        FormComponentFeedbackBorder feedback = new FormComponentFeedbackBorder(
                "border");
        feedback.add(textField);
        add(feedback);
    }
}
