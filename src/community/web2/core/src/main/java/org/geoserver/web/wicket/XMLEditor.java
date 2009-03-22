/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.form.TextArea;

public class XMLEditor extends Panel{
    public XMLEditor(String id, IModel model){
        super(id, model);
        DelegatingModel myModel = new DelegatingModel(this);
        add(new TextArea("textarea", myModel));
    }
}
