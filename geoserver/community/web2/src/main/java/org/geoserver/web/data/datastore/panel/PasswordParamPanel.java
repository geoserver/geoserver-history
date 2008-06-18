package org.geoserver.web.data.datastore.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.geotools.data.DataAccessFactory.Param;

/**
 * 
 * @author Gabriel Roldan
 */
public class PasswordParamPanel extends Panel {

    private static final long serialVersionUID = -7801141820174575611L;

    public PasswordParamPanel(final String id, final Param parameter) {
        super(id);
        add(new Label("paramName", parameter.key));
        add(new PasswordTextField("paramValue"));
    }

}
