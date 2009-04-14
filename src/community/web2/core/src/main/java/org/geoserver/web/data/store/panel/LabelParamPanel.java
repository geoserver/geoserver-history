/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.data.store.panel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Panel for a parameter that can't be edited and thus its presented as a label
 * text instead of an input field.
 * 
 * @author Gabriel Roldan
 */
@SuppressWarnings("serial")
public class LabelParamPanel extends Panel {

    public LabelParamPanel(final String id, final IModel labelModel,
            final String paramLabel) {
        super(id, labelModel);
        Label label = new Label("paramName", paramLabel);
        TextField textField = new TextField("paramValue", labelModel);

        add(label);
        add(textField);
    }
}
