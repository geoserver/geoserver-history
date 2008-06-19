package org.geoserver.web.data.datastore.panel;

import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.web.data.datastore.ParamInfo;
import org.geoserver.web.util.MapModel;

/**
 * 
 * @author Gabriel Roldan
 */
public class PasswordParamPanel extends Panel {

    private static final long serialVersionUID = -7801141820174575611L;

    public PasswordParamPanel(final String id, final Map<String, ?> paramsMap,
            final ParamInfo parameter) {
        super(id);
        add(new Label("paramName", parameter.getName()));
        add(new PasswordTextField("paramValue", new MapModel(paramsMap, parameter.getName())));
    }

}
