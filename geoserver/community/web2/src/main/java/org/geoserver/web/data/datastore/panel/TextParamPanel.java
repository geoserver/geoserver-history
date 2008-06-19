package org.geoserver.web.data.datastore.panel;

import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.web.util.MapModel;

/**
 * 
 * @author Gabriel Roldan
 */
public class TextParamPanel extends Panel {

    private static final long serialVersionUID = -1816280754831848070L;

    public TextParamPanel(final String id, final Map<String, ?> paramsMap, final String paramName,
            final String paramLabel, final boolean required) {
        super(id);
        Label label = new Label("paramName", paramLabel);
        TextField textField = new TextField("paramValue", new MapModel(paramsMap, paramName));
        textField.setRequired(required);

        add(label);
        add(textField);
    }
}
