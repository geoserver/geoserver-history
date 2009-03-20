/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import java.util.Collection;
import java.util.List;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

/**
 * Form component to edit a List<String> that makes up the keywords field of
 * various catalog objects.
 */
@SuppressWarnings("serial")
public class KeywordsEditor extends Panel {

    String myNewKeyword = "";

    List<String> mySelectedKeywords;

    /**
     * Creates a new keywords editor. 
     * @param id
     * @param keywords The module should return a non null collection of strings.
     */
    public KeywordsEditor(String id, final IModel keywords) {
        super(id, keywords);

        add(new ListMultipleChoice("keywords", new PropertyModel(this,
                "mySelectedKeywords"), keywords));
        add(new Button("removeKeywords") {
            @Override
            public void onSubmit() {
                ((Collection) keywords.getObject()).clear();
            }
        });
        add(new TextField("newKeyword", new PropertyModel(this, "myNewKeyword")));
        add(new Button("addKeyword") {
            @Override
            public void onSubmit() {
                ((Collection) keywords.getObject()).add(myNewKeyword);
                myNewKeyword = "";
            }
        });
    }
}
