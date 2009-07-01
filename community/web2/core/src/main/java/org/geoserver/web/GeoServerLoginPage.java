/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.web;

import javax.servlet.http.HttpSession;

import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebRequest;
import org.geoserver.web.wicket.ParamResourceModel;

public class GeoServerLoginPage extends WebPage {

    public GeoServerLoginPage(PageParameters parameters) {
        FeedbackPanel feedbackPanel;
        add(feedbackPanel = new FeedbackPanel("feedback"));
        feedbackPanel.setOutputMarkupId( true );
        
        TextField field = new TextField("username");
        HttpSession session = ((WebRequest) getRequest()).getHttpServletRequest().getSession();
        String lastUserName = (String) session.getAttribute(AuthenticationProcessingFilter.ACEGI_SECURITY_LAST_USERNAME_KEY);
        field.setModel(new Model(lastUserName));
        add(field);
        
        try {
            if(parameters.getBoolean("error"))
                error(new ParamResourceModel("error", this).getString());
        } catch(Exception e) {
            // ignore
        }
    }
    
}
