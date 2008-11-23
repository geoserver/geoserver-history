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
