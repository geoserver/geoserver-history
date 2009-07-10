/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * A wrapper around the Javascript 
 * <a ref="http://www.cdolivet.com/index.php?page=editArea">EditArea</a> component  
 */
@SuppressWarnings("serial")
public class XMLEditor extends Panel {
    TextArea textArea;
    
    public XMLEditor(String id, IModel model){
        super(id, model);
        DelegatingModel myModel = new DelegatingModel(this);
        add(textArea = new TextArea("textarea", myModel));
    }
    
    public String getInput() {
        return textArea.getInput();
    }
}
