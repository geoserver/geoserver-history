package org.geoserver.web.data.datastore.panel;

import java.util.Map;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.web.data.datastore.ParamInfo;
import org.geoserver.web.util.MapModel;

/**
 * 
 * @author Gabriel Roldan
 */
public class TextParamPanel extends Panel {

    private static final long serialVersionUID = -1816280754831848070L;

    public TextParamPanel(final String id, final Map<String, ?> paramsMap, final ParamInfo parameter) {
        super(id);
        Label label = new Label("paramName", parameter.getName());
        TextField textField = new TextField("paramValue", new MapModel(paramsMap, parameter
                .getName()));
        textField.setRequired(parameter.isRequired());

        if (parameter.getValue() != null) {
            textField
                    .add(new SimpleAttributeModifier("value", String.valueOf(parameter.getValue())));
        }

        add(label);
        add(textField);
    }
}
