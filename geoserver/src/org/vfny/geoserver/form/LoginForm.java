/*
 * Created on Feb 3, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;

/**
 * LoginForm purpose.
 * <p>
 * Stores the username/password information for the login page, to be used by the LoginAction
 * </p>
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: LoginForm.java,v 1.2 2004/02/09 23:30:06 dmzwiers Exp $
 */
public class LoginForm extends ActionForm {
	private String username;
    private String password;
    
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
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        String usernameLabel = messages.getMessage(locale, "label.username");
        
        if (username == null || username.equals("") || username.length() == 0) {
        	errors.add("username", new ActionError("errors.required", usernameLabel));
        }
        
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

}
