/*
 * Created on Feb 18, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.NameSpaceConfig;
/**
 * DataNamespacesNewForm purpose.
 * <p>
 * Description of DataNamespacesNewForm ...
 * </p>
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: DataNamespacesNewForm.java,v 1.1 2004/02/18 19:32:51 emperorkefka Exp $
 */
public class DataNamespacesNewForm extends ActionForm {
    private String prefix;
    
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        prefix ="";
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }
	/**
	 * Access prefix property.
	 * 
	 * @return Returns the prefix.
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Set prefix to prefix.
	 *
	 * @param prefix The prefix to set.
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

}
