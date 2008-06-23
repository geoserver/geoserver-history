/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.datastore.panel;

import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.web.util.MapModel;

/**
 * 
 * @author Gabriel Roldan
 */
public class CheckBoxParamPanel extends Panel {

    private static final long serialVersionUID = -8587266542399491587L;

    public CheckBoxParamPanel(final String id, final Map<String, ?> paramsMap,
            final String paramName, final String paramLabel) {

        super(id);
        Label label = new Label("paramName", paramLabel);
        CheckBox checkBox = new CheckBox("paramValue", new MapModel(paramsMap, paramName));
        add(label);
        add(checkBox);
    }
}
