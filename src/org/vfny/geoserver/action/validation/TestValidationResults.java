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
 * @version $Id: TestValidationResults.java,v 1.1 2004/02/28 00:54:26 dmzwiers Exp $
 */
public class TestValidationResults implements ValidationResults{
	public static final String CURRENTLY_SELECTED_KEY = "TestValidationResults";
	
	Validation v = null;
	public void setValidation(Validation v){this.v = v;}
	
	Map errors = new HashMap();
	public Map getErrors(){return errors;}
	public void error(Feature f,String s){errors.put(f,s);}
	
	Map warning = new HashMap();
	public Map getWarnings(){return warning;}
	public void warning(Feature f,String s){errors.put(f,s);}
}
