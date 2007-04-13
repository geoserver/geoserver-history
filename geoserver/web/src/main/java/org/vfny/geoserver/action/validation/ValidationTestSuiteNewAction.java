/*
 * Created on Jan 23, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.validation;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.validation.TestSuiteConfig;
import org.vfny.geoserver.config.validation.ValidationConfig;
import org.vfny.geoserver.form.validation.ValidationTestSuiteNewForm;
import org.vfny.geoserver.global.UserContainer;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * ValidationTestSuiteNewAction purpose.
 * <p>
 * Description of ValidationTestSuiteNewAction ...
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
 * ValidationTestSuiteNewAction x = new ValidationTestSuiteNewAction(...);
 * </code></pre>
 *
 * @author User, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class ValidationTestSuiteNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm incomingForm,
        UserContainer user, HttpServletRequest request, HttpServletResponse response) {
        ValidationTestSuiteNewForm form = (ValidationTestSuiteNewForm) incomingForm;

        String newName = form.getNewName();

        TestSuiteConfig suiteConfig = new TestSuiteConfig();
        suiteConfig.setName(newName);

        ServletContext context = this.getServlet().getServletContext();
        ValidationConfig validationConfig = (ValidationConfig) context.getAttribute(ValidationConfig.CONFIG_KEY);
        validationConfig.addTestSuite(suiteConfig);

        request.getSession().setAttribute(TestSuiteConfig.CURRENTLY_SELECTED_KEY, suiteConfig);

        return mapping.findForward("validationTest");
    }
}
