/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.validation;

import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.validation.TestConfig;
import org.vfny.geoserver.config.validation.TestSuiteConfig;
import org.vfny.geoserver.config.validation.ValidationConfig;
import org.vfny.geoserver.form.validation.ValidationTestSelectForm;
import org.vfny.geoserver.global.UserContainer;

/**
 * ValidationTestSelectAction purpose.
 * <p>
 * Description of ValidationTestSelectAction ...
 * </p>
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: ValidationTestSelectAction.java,v 1.2 2004/02/05 00:01:51 emperorkefka Exp $
 */
public class ValidationTestSelectAction extends ConfigAction {
    public ActionForward execute(ActionMapping mapping,
            ActionForm incomingForm, UserContainer user, HttpServletRequest request,
            HttpServletResponse response) throws ServletException {
        
        ValidationTestSelectForm form = (ValidationTestSelectForm) incomingForm;
        
        String selectedTest = form.getSelectedTest();
        String buttonAction = form.getButtonAction();

        
        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        String edit = messages.getMessage(locale, "label.edit");
        String delete = messages.getMessage(locale, "label.delete");
        
        ServletContext context = this.getServlet().getServletContext();
        ValidationConfig validationConfig = (ValidationConfig) context.getAttribute(ValidationConfig.CONFIG_KEY);
        TestSuiteConfig suiteConfig = (TestSuiteConfig) request.getSession().getAttribute(TestSuiteConfig.CURRENTLY_SELECTED_KEY);
        
        if (edit.equals(buttonAction)) {
            TestConfig testConfig = (TestConfig) suiteConfig.getTests().get(selectedTest);
            request.getSession().setAttribute(TestConfig.CURRENTLY_SELECTED_KEY, testConfig);
            
            return mapping.findForward("validationTestEditor");            
        } else if (delete.equals(buttonAction)) {
            
            Map tests = suiteConfig.getTests();
            tests.remove(selectedTest);
            suiteConfig.setTests(tests);
            
            request.getSession().removeAttribute(TestConfig.CURRENTLY_SELECTED_KEY);
            
            return mapping.findForward("validationTest");
        }
        
        throw new ServletException(
            "Action must be a MessageResource key value of either 'label.edit' or 'label.delete'");
    }
}
