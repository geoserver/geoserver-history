/*
 * Created on Feb 3, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;


/**
 * LoginForm purpose.
 * <p>
 * Stores the username/password information for the login page, to be used by the LoginAction
 * </p>
 *
 * @author rgould, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public class LoginForm extends ActionForm {
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3258410616858358324L;
    private String username;
    private String password;
    private String confirm;

    /**
     *
     * sets username and password to empty strings
     *
     * @see org.apache.struts.action.ActionForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     *
     * @param arg0
     * @param request
     */
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        username = "";
        password = "";
        confirm = "";
    }

    /**
     *
     * Verifies that username is not null or empty.
     * Could potentially do the same for password later.
     *
     * @see org.apache.struts.action.ActionForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     *
     * @param mapping
     * @param request
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        Locale locale = (Locale) request.getLocale();

        //MessageResources messages = servlet.getResources();
        //TODO: not sure about this, changed for struts 1.2.8 upgrade
        MessageResources messages = (MessageResources) request.getAttribute(Globals.MESSAGES_KEY);

        String usernameLabel = messages.getMessage(locale, "label.username");
        String passwordLabel = messages.getMessage(locale, "label.password");

        if ((username == null) || username.equals("")) {
            errors.add("username", new ActionError("errors.required", usernameLabel));
        }

        if ((password == null) || password.equals("")) {
            errors.add("password", new ActionError("errors.required", passwordLabel));
        }

        //failed experiment, as it looks like Login and LoginEdit use the same
        //form, which means that confirm will always be null on the normal.
        //I think the best course of action would be to have a LoginEditForm,
        //which extends this class, just adding the confirm stuff, and does the
        //validate with the confirm.  We don't want the normal login using
        //the validate, which is why this experiment failed.  Shouldn't be
        //too hard, it's just getting late and I've spent too much time on this
        //already.

        //if (confirm == null || !confirm.equals(password)) {
        //    errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("error.password.mismatch"));
        //}
        return errors;
    }

    /**
     * Access password property.
     *
     * @return Returns the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Set password to password.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Access username property.
     *
     * @return Returns the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Set username to username.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }
}
