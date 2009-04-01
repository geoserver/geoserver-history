/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web.wicket;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.model.IModel;

/**
 * A dropdwon subclass behaving like a menu: when a choice is made an
 * {@link #onChoice(AjaxRequestTarget)} event is triggered.
 * @author Andrea Aime - OpenGeo
 *
 */
@SuppressWarnings("serial")
public abstract class MenuDropDownChoice extends DropDownChoice {

    public MenuDropDownChoice(String id, IModel choices,
            IChoiceRenderer renderer) {
        super(id, choices, renderer);
        init();
    }

    public MenuDropDownChoice(String id, IModel model, IModel choices,
            IChoiceRenderer renderer) {
        super(id, model, choices, renderer);
        init();
    }

    public MenuDropDownChoice(String id, IModel model, IModel choices) {
        super(id, model, choices);
        init();
    }

    public MenuDropDownChoice(String id, IModel model, List data,
            IChoiceRenderer renderer) {
        super(id, model, data, renderer);
        init();
    }

    public MenuDropDownChoice(String id, IModel model, List choices) {
        super(id, model, choices);
        init();
    }

    public MenuDropDownChoice(String id, IModel choices) {
        super(id, choices);
        init();
    }

    public MenuDropDownChoice(String id, List data, IChoiceRenderer renderer) {
        super(id, data, renderer);
        init();
    }

    public MenuDropDownChoice(String id, List choices) {
        super(id, choices);
        init();        
    }

    public MenuDropDownChoice(String id) {
        super(id);
        init();   
    }
    
    protected void init() {
        // attach the event handler and make sure we have the drop down reset on reload
        add(new AjaxFormComponentUpdatingBehavior("onchange") {
            
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                onChoice(target);
            }
            
            @Override
            public void renderHead(IHeaderResponse response) {
                super.renderHead(response);
                
                // make sure the select element is reset on page reload
                String js = "document.getElementById(\"" + getComponent().getMarkupId() + "\")[0].selected = true";
                response.renderOnLoadJavascript(js);
            }
        });
        setOutputMarkupId(true);
    }

    /**
     * Listener method invoked when the user makes a choice
     * @param target
     */
    protected abstract void onChoice(AjaxRequestTarget target);

}
