/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.NameSpaceConfig;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataNamespacesForm extends ActionForm {
	
	private String namespaceID;
	private String URI;
	private boolean _default;
	private String prefix;
	
	private String selectedNamespace;
	private String action;
	
	private TreeSet namespaces;	

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
		action = "";
				
		ServletContext context = getServlet().getServletContext();
		DataConfig config =
			(DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
			
		namespaces = new TreeSet(config.getNameSpaces().keySet());
		
		NameSpaceConfig nsConfig;
		
		selectedNamespace = (String) context.getAttribute("selectedNamespace");	
			
		nsConfig = config.getNameSpace(selectedNamespace);		
		if (nsConfig == null) {
			nsConfig = config.getNameSpace( (String) namespaces.first());
		}
		
		_default = nsConfig.isDefault();
		namespaceID = selectedNamespace;
		prefix = nsConfig.getPrefix();
		URI = nsConfig.getUri();
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
	public String getNamespaceID() {
		return namespaceID;
	}

	/**
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @return
	 */
	public String getURI() {
		return URI;
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
	public void setNamespaceID(String string) {
		namespaceID = string;
	}

	/**
	 * @param string
	 */
	public void setPrefix(String string) {
		prefix = string;
	}

	/**
	 * @param string
	 */
	public void setURI(String string) {
		URI = string;
	}

	/**
	 * @return
	 */
	public boolean isDefaultChecked() {
		return defaultChecked;
	}

	/**
	 * @param b
	 */
	public void setDefaultChecked(boolean b) {
		defaultChecked = b;
	}

	/**
	 * @return
	 */
	public String getSelectedNamespace() {
		return selectedNamespace;
	}

	/**
	 * @param string
	 */
	public void setSelectedNamespace(String string) {
		ServletContext context = getServlet().getServletContext();
		context.setAttribute("selectedNamespace", string);			
		selectedNamespace = string;
	}
	/**
	 * @return
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param string
	 */
	public void setAction(String string) {
		action = string;
	}

	/**
	 * @return
	 */
	public TreeSet getNamespaces() {
		return namespaces;
	}

}
