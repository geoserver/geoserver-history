/*
 * Created on Feb 27, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.validation;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.ValidationResults;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.validation.TestSuiteConfig;
import org.vfny.geoserver.config.validation.ValidationConfig;
import org.vfny.geoserver.global.GeoValidator;
import org.vfny.geoserver.global.UserContainer;

import com.vividsolutions.jts.geom.Envelope;

/**
 * ValidationTestDoIt purpose.
 * <p>
 * Description of ValidationTestDoIt ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id: ValidationTestDoIt.java,v 1.9 2004/06/28 23:41:10 emperorkefka Exp $
 */
public class ValidationTestDoIt extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
            ActionForm incomingForm, UserContainer user, HttpServletRequest request,
            HttpServletResponse response) {
	    
	    boolean stopThread = false;
	    String parameter = mapping.getParameter();
	    if (parameter != null && parameter.equals("stop")) {
	        stopThread = true;
	    }
	    
	    //Checks to see if previous Validation has even finished executing yet.
		Thread oldThread = (Thread) request.getSession().getAttribute(ValidationRunnable.KEY);
		if (oldThread != null && oldThread.isAlive()) {
		    //OldThread has not finished execution; Shouldn't start a new one.
		    //Alternatively, we could wait.
		    
		    if (stopThread == true) {
		        oldThread.stop(); //This is decprecated, but is there another way to stop a Runnable?
		    }
		} else {
	    
		    ServletContext context = this.getServlet().getServletContext();
		    ValidationConfig validationConfig = (ValidationConfig) context.getAttribute(ValidationConfig.CONFIG_KEY);
		    TestSuiteConfig suiteConfig = (TestSuiteConfig) request.getSession().getAttribute(TestSuiteConfig.CURRENTLY_SELECTED_KEY);
		    Map plugins = new HashMap();
		    Map ts = new HashMap();
		    validationConfig.toDTO(plugins,ts); // return by ref.
		    
		    ValidationRunnable testThread = new ValidationRunnable();
		    testThread.setup(ts, plugins, getDataConfig(), context, request);
		
		    Thread thread = new Thread(testThread);
		
		    request.getSession().setAttribute(ValidationRunnable.KEY, thread);
		    thread.start();
		}
		
		return  mapping.findForward("config.validation.displayResults");
	}
}
