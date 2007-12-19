/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/*
 * Created on Feb 3, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.form.LoginForm;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.UserContainer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * LoginAction purpose.
 *
 * <p>
 * Processes the login of a user to gain access to the GeoServer Configuration.
 * Currently the defaults are "admin" for username, "geoserver" for password,
 * case insensitive.  This value can be changed in the services.xml file,
 * found in the WEB-INF directory of a running GeoServer.  A page to change
 * the log in would be nice, but it's not here yet.
 * </p>
 *
 * @author rgould, Refractions Research, Inc.
 * @author $Author: cholmesny $ (last modification)
 * @version $Id$
 *
 * @task TODO: add a page to change the username and password from the ui.
 */
public class LoginAction extends GeoServerAction implements ApplicationContextAware {
    ApplicationContext context;

    public ActionForward execute(ActionMapping mapping, ActionForm form,
        HttpServletRequest request, HttpServletResponse response) {
        LoginForm loginForm = (LoginForm) form;
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        //GlobalConfig global = (GlobalConfig) getServlet().getServletContext()
        //                                       .getAttribute(GlobalConfig.CONFIG_KEY);
        GeoServer geoserver = getWFS(request).getGeoServer();

        if (username.equalsIgnoreCase(geoserver.getAdminUserName())
                && password.equalsIgnoreCase(geoserver.getAdminPassword())) {
            UserContainer user = new UserContainer();
            user.setUsername(username);
            request.getSession().setAttribute(UserContainer.SESSION_KEY, user);

            String forward = (String) request.getAttribute("forward");

            if (forward == null) {
                forward = "welcome";
            }

            return mapping.findForward(forward);
        }

        ActionErrors errors = new ActionErrors();
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.login.invalidCombo"));
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("message.login.hint"));
        request.setAttribute(Globals.ERROR_KEY, errors);

        return mapping.findForward("login");
    }

    public void setApplicationContext(ApplicationContext arg0)
        throws BeansException {
        this.context = arg0;
    }
}
