/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.data;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataDataStoresForm extends ActionForm {
	
	private String dataStoreID;
	private boolean enabled;
	private String namespace;
	private String description;
	private String server;
	private String port;
	private String username;
	private String password;
	
	/*
	 * Because of the way that STRUTS works, if the user does not check the enabled box,
	 * or unchecks it, setEnabled() is never called, thus we must monitor setEnabled()
	 * to see if it doesn't get called. This must be accessible, as ActionForms need to
	 * know about it -- there is no way we can tell whether we are about to be passed to
	 * an ActionForm or not.
	 * 
	 * Probably a better way to do this, but I can't think of one.
	 * -rgould
	 */
	private boolean enabledChecked = false; 	
	
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		
		enabledChecked = false;		
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}
	/**
	 * @return
	 */
	public String getDataStoreID() {
		return dataStoreID;
	}

	/**
	 * @return
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @return
	 */
	public String getNamespace() {
		return namespace;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return
	 */
	public String getPort() {
		return port;
	}

	/**
	 * @return
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param string
	 */
	public void setDataStoreID(String string) {
		dataStoreID = string;
	}

	/**
	 * @param string
	 */
	public void setDescription(String string) {
		description = string;
	}

	/**
	 * @param b
	 */
	public void setEnabled(boolean b) {
		enabledChecked = true;		
		enabled = b;
	}

	/**
	 * @param string
	 */
	public void setNamespace(String string) {
		namespace = string;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string) {
		password = string;
	}

	/**
	 * @param string
	 */
	public void setPort(String string) {
		port = string;
	}

	/**
	 * @param string
	 */
	public void setServer(String string) {
		server = string;
	}

	/**
	 * @param string
	 */
	public void setUsername(String string) {
		username = string;
	}

	/**
	 * @return
	 */
	public boolean isEnabledChecked() {
		return enabledChecked;
	}

	/**
	 * @param b
	 */
	public void setEnabledChecked(boolean b) {
		enabledChecked = b;
	}

}
