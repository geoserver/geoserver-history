/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.data;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataStylesForm extends ActionForm {
	
	private String styleID;
	private boolean _default;
	private String filename;
	
	private String selectedStyle;	
	
	/*
	 * Because of the way that STRUTS works, if the user does not check the default box,
	 * or unchecks it, set_default() is never called, thus we must monitor set_default()
	 * to see if it doesn't get called. This must be accessible, as ActionForms need to
	 * know about it -- there is no way we can tell whether we are about to be passed to
	 * an ActionForm or not.
	 * 
	 * Probably a better way to do this, but I can't think of one.
	 * -rgould
	 */
	private boolean defaultChecked = false; 	
	
		
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		
		defaultChecked = false;
		
		ServletContext context = getServlet().getServletContext();
		DataConfig config =
			(DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}
	/**
	 * @return
	 */
	public boolean is_default() {
		return _default;
	}

	/**
	 * @return
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @return
	 */
	public String getStyleID() {
		return styleID;
	}

	/**
	 * @param b
	 */
	public void set_default(boolean b) {
		defaultChecked = true;
		_default = b;
	}

	/**
	 * @param string
	 */
	public void setFilename(String string) {
		filename = string;
	}

	/**
	 * @param string
	 */
	public void setStyleID(String string) {
		styleID = string;
	}

}
