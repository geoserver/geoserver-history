/*
 * Created on Jan 23, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.validation;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.validation.TestSuiteConfig;
import org.vfny.geoserver.config.validation.ValidationConfig;

/**
 * ValidationTestNewForm purpose.
 * <p>
 * Description of ValidationTestNewForm ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * </p>
 * <ul>
 * <li>
 * Feature: description
 * </li>
 * </ul>
 * <p>
 * Example Use:
 * </p>
 * <pre><code>
 * ValidationTestNewForm x = new ValidationTestNewForm(...);
 * </code></pre>
 * 
 * @author User, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ValidationTestNewForm.java,v 1.1 2004/01/31 00:27:28 jive Exp $
 */
public class ValidationTestNewForm extends ActionForm {
    
    private String newName;
    private String selectedPlugIn;
    
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
        
        newName ="";
    }
    
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }    
    
    public SortedSet getPlugIns(){
        
        ValidationConfig validationConfig = (ValidationConfig) this.getServlet().getServletContext().getAttribute(ValidationConfig.CONFIG_KEY);
        return new TreeSet(validationConfig.getPlugInNames());
    }
    
	/**
	 * Access newName property.
	 * 
	 * @return Returns the newName.
	 */
	public String getNewName() {
		return newName;
	}

	/**
	 * Set newName to newName.
	 *
	 * @param newName The newName to set.
	 */
	public void setNewName(String newName) {
		this.newName = newName;
	}

	/**
	 * Access selectedPlugIn property.
	 * 
	 * @return Returns the selectedPlugIn.
	 */
	public String getSelectedPlugIn() {
		return selectedPlugIn;
	}

	/**
	 * Set selectedPlugIn to selectedPlugIn.
	 *
	 * @param selectedPlugIn The selectedPlugIn to set.
	 */
	public void setSelectedPlugIn(String selectedPlugIn) {
		this.selectedPlugIn = selectedPlugIn;
	}

}
