/*
 * Created on Feb 3, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.form.LoginForm;
import org.vfny.geoserver.global.UserContainer;

/**
 * LoginAction purpose.
 * <p>
 * Processes the login of a user to gain access to the GeoServer Configuration.
 * Currently the only valid parameters are "admin" for username, "geoserver" for password, case insensitive.
 * </p>
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: LoginAction.java,v 1.5 2004/03/15 08:16:10 jive Exp $
 */
public class LoginAction extends GeoServerAction {
    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        
    	LoginForm loginForm = (LoginForm) form;
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        
        /*
         * @TODO Change this! Allow support for real users!
         */
        if (username.equalsIgnoreCase("admin") && password.equalsIgnoreCase("geoserver")) {
            UserContainer user = new UserContainer();
            user.setUsername(username);
            request.getSession().setAttribute(UserContainer.SESSION_KEY, user);
            String forward = (String) request.getAttribute("forward");
            if(forward == null){
                forward = "welcome";
            }            
            return mapping.findForward( forward );
        }
        
        ActionErrors errors = new ActionErrors();
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.login.invalidCombo"));
        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("message.login.hint"));        
        request.setAttribute(Globals.ERROR_KEY, errors);
        
        return mapping.findForward("login");
    }
}
