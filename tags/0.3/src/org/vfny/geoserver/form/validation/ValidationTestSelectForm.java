/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.validation;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.validation.TestSuiteConfig;
import org.vfny.geoserver.config.validation.ValidationConfig;

/**
 * ValidationTestSelectForm purpose.
 * <p>
 * Description of ValidationTestSelectForm ...
 * </p>
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ValidationTestSelectForm.java,v 1.1 2004/01/31 00:27:28 jive Exp $
 */
public class ValidationTestSelectForm extends ActionForm {

	/**
	 * 
	 * @uml.property name="selectedTest" multiplicity="(0 1)"
	 */
	private String selectedTest;

	/**
	 * 
	 * @uml.property name="buttonAction" multiplicity="(0 1)"
	 */
	private String buttonAction;

	/**
	 * 
	 * @uml.property name="request"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private HttpServletRequest request;

    
    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
        selectedTest="";
        buttonAction="";
        this.request = request;
    }
    
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }
    
    public SortedSet getTests() {
        ServletContext context = this.getServlet().getServletContext();
        ValidationConfig validationConfig = (ValidationConfig) context.getAttribute(ValidationConfig.CONFIG_KEY);
        TestSuiteConfig suiteConfig = (TestSuiteConfig) request.getSession().getAttribute(TestSuiteConfig.CURRENTLY_SELECTED_KEY);
        return new TreeSet(suiteConfig.getTests().keySet());
    }

	/**
	 * Access selectedTest property.
	 * 
	 * @return Returns the selectedTest.
	 * 
	 * @uml.property name="selectedTest"
	 */
	public String getSelectedTest() {
		return selectedTest;
	}

	/**
	 * Set selectedTest to selectedTest.
	 * 
	 * @param selectedTest The selectedTest to set.
	 * 
	 * @uml.property name="selectedTest"
	 */
	public void setSelectedTest(String selectedTest) {
		this.selectedTest = selectedTest;
	}

	/**
	 * Access buttonAction property.
	 * 
	 * @return Returns the buttonAction.
	 * 
	 * @uml.property name="buttonAction"
	 */
	public String getButtonAction() {
		return buttonAction;
	}

	/**
	 * Set buttonAction to buttonAction.
	 * 
	 * @param buttonAction The buttonAction to set.
	 * 
	 * @uml.property name="buttonAction"
	 */
	public void setButtonAction(String buttonAction) {
		this.buttonAction = buttonAction;
	}

}
