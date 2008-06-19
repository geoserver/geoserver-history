package org.geoserver.web.data.datastore.panel;

import java.util.List;
import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.FormComponentFeedbackBorder;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.validation.IValidator;
import org.geoserver.web.util.MapModel;

/**
 * 
 * @author Gabriel Roldan
 */
public class TextParamPanel extends Panel {

    private static final long serialVersionUID = -1816280754831848070L;

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
    public TextParamPanel(final String id, final Map<String, ?> paramsMap, final String paramName,
            final String paramLabel, final boolean required, final List<IValidator> validators) {
        super(id);
        Label label = new Label("paramName", paramLabel);

        TextField textField = new TextField("paramValue", new MapModel(paramsMap, paramName));
        textField.setRequired(required);
        if(validators != null){
            for(IValidator validator : validators){
                textField.add(validator);
            }
        }
        
        FormComponentFeedbackBorder requiredFieldFeedback = new FormComponentFeedbackBorder(
                "border");
        requiredFieldFeedback.add(textField);

        add(label);
        add(requiredFieldFeedback);
    }
}
