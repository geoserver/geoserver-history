package org.geoserver.web.data.datastore.panel;

import java.util.Map;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.web.data.datastore.ParamInfo;
import org.geoserver.web.util.MapModel;
import org.geotools.data.DataAccessFactory.Param;

/**
 * 
 * @author Gabriel Roldan
 */
public class CheckBoxParamPanel extends Panel {

    private static final long serialVersionUID = -8587266542399491587L;

    public CheckBoxParamPanel(final String id, final Map<String, ?> paramsMap,
            final ParamInfo parameter) {
        super(id);
        Label label = new Label("paramName", parameter.getName());
        CheckBox checkBox = new CheckBox("paramValue", new MapModel(paramsMap, parameter.getName()));
        final boolean checked = Boolean.valueOf(String.valueOf(parameter.getValue()));
        if (checked) {
            checkBox.add(new SimpleAttributeModifier("checked", "checked"));
        }
        add(label);
        add(checkBox);
    }
}
