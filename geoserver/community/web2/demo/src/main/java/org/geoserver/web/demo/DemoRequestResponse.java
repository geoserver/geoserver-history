/**
 * 
 */
package org.geoserver.web.demo;

import org.apache.wicket.PageMap;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.web.demo.DemoRequestsPage.DemoRequestsModel;

public class DemoRequestResponse extends WebPage {

    public DemoRequestResponse(DemoRequestsModel model) {
        // this page being in an IFrame needs to grap its own PageMap
        // in order not to share it with the parent page and thus be
        // marked as expired
        super(PageMap.forName("demoRequestResponse"));
        Form form = new Form("form");
        add(form);
        form.add(new HiddenField("url", new PropertyModel(model, "requestUrl")));
        form.add(new TextArea("body", new PropertyModel(model, "requestBody")));
        form.add(new HiddenField("username", new PropertyModel(model, "userName")));
        form.add(new HiddenField("password", new PropertyModel(model, "password")));

        // override the action property of the form to submit to the TestWfsPost
        // servlet
        form.add(new SimpleAttributeModifier("action", "../TestWfsPost"));

        // Set the same markup is as in the html page so wicket does not
        // generates
        // its own and the javascript code in the onLoad event for the <body>
        // element
        // finds out the form by id
        form.setMarkupId("form");
    }

}