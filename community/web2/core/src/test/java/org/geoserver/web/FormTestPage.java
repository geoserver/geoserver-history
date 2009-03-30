package org.geoserver.web;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;

/**
 * Helper class to test components that need a form around them to be tested
 * (typically custom panels with form components inside)
 */
public class FormTestPage extends WebPage {

    public FormTestPage(ComponentBuilder builder) {
        Form form = new Form("form");
        form.add(builder.buildComponent("content"));
        add(form);
    }

    public interface ComponentBuilder extends Serializable {
        Component buildComponent(String id);
    }
}
