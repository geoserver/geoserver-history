/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.datastore.panel;

import java.util.Map;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.geoserver.web.util.MapModel;

/**
 * Panel for a parameter that can't be edited and thus its presented as a label
 * text instead of an input field.
 * 
 * @author Gabriel Roldan
 */
public class LabelParamPanel extends Panel {

    private static final long serialVersionUID = -1816280754831848070L;

    public LabelParamPanel(final String id, final Map<String, ?> paramsMap, final String paramName,
            final String paramLabel) {
        super(id);
        Label label = new Label("paramName", paramLabel);
        TextField textField = new TextField("paramValue", new MapModel(paramsMap, paramName));

        add(label);
        add(textField);
    }
}
