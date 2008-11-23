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
import org.vfny.geoserver.config.validation.PlugInConfig;
import org.vfny.geoserver.config.validation.TestConfig;
import org.vfny.geoserver.config.validation.TestSuiteConfig;
import org.vfny.geoserver.config.validation.ValidationConfig;
import org.vfny.geoserver.form.validation.ValidationTestNewForm;
import org.vfny.geoserver.global.UserContainer;
import java.util.HashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * ValidationTestNewAction purpose.
 * <p>
 * Description of ValidationTestNewAction ...
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
 * ValidationTestNewAction x = new ValidationTestNewAction(...);
 * </code></pre>
 *
 * @author User, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id$
 */
public class ValidationTestNewAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping, ActionForm incomingForm,
        UserContainer user, HttpServletRequest request, HttpServletResponse response) {
        ServletContext context = this.getServlet().getServletContext();
        ValidationConfig validationConfig = (ValidationConfig) context.getAttribute(ValidationConfig.CONFIG_KEY);

        ValidationTestNewForm form = (ValidationTestNewForm) incomingForm;

        String newName = form.getNewName();
        String selectedPlugIn = form.getSelectedPlugIn();

        PlugInConfig plugIn = validationConfig.getPlugIn(selectedPlugIn);

        TestConfig testConfig = new TestConfig();
        testConfig.setName(newName);
        testConfig.setPlugIn(plugIn);
        testConfig.setArgs(new HashMap());

        TestSuiteConfig suiteConfig = (TestSuiteConfig) request.getSession()
                                                               .getAttribute(TestSuiteConfig.CURRENTLY_SELECTED_KEY);
        suiteConfig.addTest(testConfig);
        getApplicationState().notifyConfigChanged();

        request.getSession().setAttribute(TestConfig.CURRENTLY_SELECTED_KEY, testConfig);

        return mapping.findForward("validationTestEditor");
    }
}
