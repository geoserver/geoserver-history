/*
 * Created on Feb 27, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.validation;

import java.util.HashMap;
import java.util.Map;

import org.geotools.feature.Feature;
import org.geotools.validation.Validation;
import org.geotools.validation.ValidationResults;

/**
 * TestValidationResults purpose.
 * <p>
 * Description of TestValidationResults ...
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: TestValidationResults.java,v 1.2 2004/03/16 23:58:31 dmzwiers Exp $
 */
public class TestValidationResults implements ValidationResults{
	public static final String CURRENTLY_SELECTED_KEY = "TestValidationResults";
	
	Validation v = null;
	
	//HACK for JODY cause he messed up and then whined alot.
	boolean run = false;
	public void setValidation(Validation v){this.v = v;run=true;}
	
	Map errors = new HashMap();
	public Map getErrors(){return errors;}
	public void error(Feature f,String s){errors.put(f,s);}
	
	Map warning = new HashMap();
	public Map getWarnings(){return warning;}
	public void warning(Feature f,String s){errors.put(f,s);}
	
	/**
	 * Access run property.
	 * 
	 * @return Returns the run.
	 */
	public boolean isRun() {
		return run;
	}
	
	/**
	 * Set run to run.
	 *
	 * @param run The run to set.
	 */
	public void setRun(boolean run) {
		this.run = run;
	}
}
