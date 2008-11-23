package org.geoserver.web;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

/**
 * Displays a message suggesting the user to login or to elevate his privileges
 * @author Andrea Aime - TOPP
 *
 */
public class UnauthorizedPage extends GeoServerBasePage {

    public UnauthorizedPage() {
        IModel model = null;
        if(getSession().getAuthentication() == null || !getSession().getAuthentication().isAuthenticated())
            model = new ResourceModel( "UnauthorizedPage.loginRequired" );
        else
            model = new ResourceModel( "UnauthorizedPage.insufficientPrivileges" );
        add(new Label("unauthorizedMessage", model));
    }
}
