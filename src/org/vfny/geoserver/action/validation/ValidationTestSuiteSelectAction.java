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
import org.vfny.geoserver.config.validation.TestSuiteConfig;
import org.vfny.geoserver.config.validation.ValidationConfig;
import org.vfny.geoserver.form.validation.ValidationTestSuiteSelectForm;

/**
 * ValidationTestSuiteSelect purpose.
 * <p>
 * Used to look up a test suite indicated by the ActionForm 
 * (ValidationTestSuiteSelectForm) passed to it, save it into
 * session and pass it to the test selector.
 * </p>
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ValidationTestSuiteSelectAction.java,v 1.1 2004/01/31 00:27:26 jive Exp $
 */
public class ValidationTestSuiteSelectAction extends ConfigAction {

    public ActionForward execute(ActionMapping mapping,
            ActionForm incomingForm, HttpServletRequest request,
            HttpServletResponse response) throws ServletException{
        
        ValidationTestSuiteSelectForm form = (ValidationTestSuiteSelectForm) incomingForm;
        
        String selectedTestSuite = form.getSelectedTestSuite();
        String buttonAction = form.getButtonAction();
        
        Locale locale = (Locale) request.getLocale();
        MessageResources messages = servlet.getResources();
        String edit = messages.getMessage(locale, "label.edit");
        String delete = messages.getMessage(locale, "label.delete");

        ServletContext context = this.getServlet().getServletContext();
        ValidationConfig validationConfig = (ValidationConfig) context.getAttribute(ValidationConfig.CONFIG_KEY);
        
        if (edit.equals(buttonAction)) {
            TestSuiteConfig suiteConfig = (TestSuiteConfig) validationConfig.getTestSuites().get(selectedTestSuite);

            request.getSession().setAttribute(TestSuiteConfig.CURRENTLY_SELECTED_KEY, suiteConfig);
            
            return mapping.findForward("validationTest");            
        } else if (delete.equals(buttonAction)) {
            Map suites = validationConfig.getTestSuites();
            suites.remove(selectedTestSuite);
            validationConfig.setTestSuites(suites);
            
            request.getSession().removeAttribute(TestSuiteConfig.CURRENTLY_SELECTED_KEY);
            
            return mapping.findForward("validationTestSuite");
        }

        throw new ServletException(
        "Action must be a MessageResource key value of either 'label.edit' or 'label.delete'");
    }
}
