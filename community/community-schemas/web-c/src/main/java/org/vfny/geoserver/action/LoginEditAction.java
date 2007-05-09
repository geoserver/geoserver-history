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

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.form.LoginForm;
import org.vfny.geoserver.global.UserContainer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * <p>
 * Allows for the changing of username and password
 * </p>
 *
 * @author rgould, Refractions Research, Inc.
 */
public class LoginEditAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, UserContainer user,
        HttpServletRequest request, HttpServletResponse response) {
        LoginForm loginForm = (LoginForm) form;
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        String confirm = loginForm.getConfirm();

        GlobalConfig global = (GlobalConfig) getServlet().getServletContext()
                                                 .getAttribute(GlobalConfig.CONFIG_KEY);
        //Failed experiment, this stuff should really go in a validate method,
        //I think this is showing the weakness of both edit and non using Login
        //Form, instead of a LoginEditForm.
        /*if (confirm == null || !confirm.equals(password)){
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR,
                       new ActionError("error.password.mismatch"));
            saveErrors(request, errors);
            return mapping.getInputForward();//findForward("config.loginEditSubmit");
            }*/
        global.setAdminUserName(username);
        global.setAdminPassword(password);
        getApplicationState().notifyConfigChanged();
        getServlet().getServletContext().setAttribute(GlobalConfig.CONFIG_KEY, global);

        String forward = (String) request.getAttribute("forward");

        if (forward == null) {
            forward = "config";
        }

        return mapping.findForward(forward);
    }
}
