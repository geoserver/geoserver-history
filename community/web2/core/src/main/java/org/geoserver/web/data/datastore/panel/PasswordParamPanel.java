package org.geoserver.web.data.datastore.panel;

import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.web.util.MapModel;

/**
 * 
 * @author Gabriel Roldan
 */
public class PasswordParamPanel extends Panel {

    private static final long serialVersionUID = -7801141820174575611L;

    public PasswordParamPanel(final String id, final Map<String, ?> paramsMap,
            final String paramName, final String paramLabel, final boolean required) {
        super(id);
        add(new Label("paramName", paramLabel));

        PasswordTextField passwordField;
        passwordField = new PasswordTextField("paramValue", new MapModel(paramsMap, paramName));
        passwordField.setRequired(required);
        //we want to password to stay there if already is
        passwordField.setResetPassword(false);

        FormComponentFeedbackBorder requiredFieldFeedback;
        requiredFieldFeedback = new FormComponentFeedbackBorder("border");

        requiredFieldFeedback.add(passwordField);

        add(requiredFieldFeedback);
    }

}
