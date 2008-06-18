package org.geoserver.web.data.datastore.panel;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.geotools.data.DataAccessFactory.Param;

/**
 * 
 * @author Gabriel Roldan
 */
public class TextParamPanel extends Panel {

    private static final long serialVersionUID = -1816280754831848070L;

    public TextParamPanel(final String id, Param parameter) {
        super(id);
        Label label = new Label("paramName", parameter.key);
        if (parameter.title != null) {
            label.add(new SimpleAttributeModifier("title", String.valueOf(parameter.title)));
        }

        TextField textField = new TextField("paramValue");
        textField.setRequired(parameter.required);

        if (parameter.sample != null) {
            textField.add(new SimpleAttributeModifier("value", String.valueOf(parameter.sample)));
        }

        add(label);
        add(textField);
    }

}
