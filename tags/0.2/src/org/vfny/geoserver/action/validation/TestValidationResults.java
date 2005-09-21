/*
 * Created on Feb 27, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * @author $Author: jive $ (last modification)
 * @version $Id: TestValidationResults.java,v 1.6 2004/04/21 08:16:22 jive Exp $
 */
public class TestValidationResults implements ValidationResults{
	public static final String CURRENTLY_SELECTED_KEY = "TestValidationResults";

	/**
	 * 
	 * @uml.property name="v"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	Validation v = null;

	/**
	 * 
	 * @uml.property name="run" multiplicity="(0 1)"
	 */
	//HACK for JODY cause he messed up and then whined alot.
	boolean run = false;

    
	public void setValidation(Validation v){
        this.v = v;run=true;
    }
	
	private String toMessage( String message ) {
        StringBuffer buf = new StringBuffer();
        buf.append( v.getName() );
        buf.append( ": " );
        buf.append( message );
        buf.append( "\n" );
        buf.append( v.getDescription() );
        
        return buf.toString();        
    }

	/**
	 * 
	 * @uml.property name="errors"
	 * @uml.associationEnd qualifier="f:org.geotools.feature.Feature java.lang.String"
	 * multiplicity="(0 1)"
	 */
	Map errors = new HashMap();

	/**
	 * 
	 * @uml.property name="errors"
	 */
	public Map getErrors() {
		return errors;
	}

	public void error(Feature f,String s){
        String message = toMessage( s );
		Logger logger = Logger.getLogger("org.vfny.geoserver");
		if(logger.getLevel().equals(Level.FINEST)){
			logger.warning( message );
		}
		errors.put(f, message );
	}

	/**
	 * 
	 * @uml.property name="warning"
	 * @uml.associationEnd qualifier="f:org.geotools.feature.Feature java.lang.String"
	 * multiplicity="(0 1)"
	 */
	Map warning = new HashMap();

	public Map getWarnings(){return warning;}
    
	public void warning(Feature f,String s){
        String message = toMessage( s );
        Logger logger = Logger.getLogger("org.vfny.geoserver");
        if(logger.getLevel().equals(Level.FINEST)){
            logger.warning( message );
        }
        warning.put(f, message );        
	}
	
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
	 * 
	 * @uml.property name="run"
	 */
	public void setRun(boolean run) {
		this.run = run;
	}

}
