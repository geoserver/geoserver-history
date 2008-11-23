/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.validation;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.validation.TestSuiteConfig;
import org.vfny.geoserver.config.validation.ValidationConfig;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * ValidationTestSelectForm purpose.
 * <p>
 * Description of ValidationTestSelectForm ...
 * </p>
 *
 * @author rgould, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class ValidationTestSelectForm extends ActionForm {
    private String selectedTest;
    private String buttonAction;
    private HttpServletRequest request;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);
        selectedTest = "";
        buttonAction = "";
        this.request = request;
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    public SortedSet getTests() {
        ServletContext context = this.getServlet().getServletContext();
        ValidationConfig validationConfig = (ValidationConfig) context.getAttribute(ValidationConfig.CONFIG_KEY);
        TestSuiteConfig suiteConfig = (TestSuiteConfig) request.getSession()
                                                               .getAttribute(TestSuiteConfig.CURRENTLY_SELECTED_KEY);

        return new TreeSet(suiteConfig.getTests().keySet());
    }

    /**
     * Access selectedTest property.
     *
     * @return Returns the selectedTest.
     */
    public String getSelectedTest() {
        return selectedTest;
    }

    /**
     * Set selectedTest to selectedTest.
     *
     * @param selectedTest The selectedTest to set.
     */
    public void setSelectedTest(String selectedTest) {
        this.selectedTest = selectedTest;
    }

    /**
     * Access buttonAction property.
     *
     * @return Returns the buttonAction.
     */
    public String getButtonAction() {
        return buttonAction;
    }

    /**
     * Set buttonAction to buttonAction.
     *
     * @param buttonAction The buttonAction to set.
     */
    public void setButtonAction(String buttonAction) {
        this.buttonAction = buttonAction;
    }
}
