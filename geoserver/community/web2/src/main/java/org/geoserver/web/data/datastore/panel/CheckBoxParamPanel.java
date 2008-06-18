package org.geoserver.web.data.datastore.panel;

import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.geotools.data.DataAccessFactory.Param;

/**
 * 
 * @author Gabriel Roldan
 */
public class CheckBoxParamPanel extends Panel {

    private static final long serialVersionUID = -8587266542399491587L;

    public CheckBoxParamPanel(final String id, final Param parameter) {
        super(id);
        Label label = new Label("paramName", parameter.key);
        CheckBox checkBox = new CheckBox("paramValue");
        final boolean checked = Boolean.valueOf(String.valueOf(parameter.sample));
        if(checked){
            checkBox.add(new SimpleAttributeModifier("checked", "checked"));
        }
        add(label);
        add(checkBox);
    }
}
