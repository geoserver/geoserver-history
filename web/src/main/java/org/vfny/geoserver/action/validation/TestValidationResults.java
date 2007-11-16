/*
 * Created on Feb 27, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.validation;

import org.geotools.validation.Validation;
import org.geotools.validation.ValidationResults;
import org.opengis.feature.simple.SimpleFeature;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * TestValidationResults purpose.
 * <p>
 * Description of TestValidationResults ...
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
 */
public class TestValidationResults implements ValidationResults {
    public static final String CURRENTLY_SELECTED_KEY = "TestValidationResults";
    Validation v = null;

    //HACK for JODY cause he messed up and then whined alot.
    boolean run = false;

    public void setValidation(Validation v) {
        this.v = v;
        run = true;
    }

    private String toMessage(String message) {
        StringBuffer buf = new StringBuffer();
        buf.append(v.getName());
        buf.append(": ");
        buf.append(message);
        buf.append("\n");
        buf.append(v.getDescription());

        return buf.toString();
    }

    Map errors = new HashMap();

    public Map getErrors() {
        return errors;
    }

    public void error(SimpleFeature f, String s) {
        String message = toMessage(s);
        Logger logger = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver");

        if (logger.getLevel().equals(Level.FINEST)) {
            logger.warning(message);
        }

        errors.put(f, message);
    }

    Map warning = new HashMap();

    public Map getWarnings() {
        return warning;
    }

    public void warning(SimpleFeature f, String s) {
        String message = toMessage(s);
        Logger logger = Logger.getLogger("org.vfny.geoserver");

        if (logger.getLevel().equals(Level.FINEST)) {
            logger.warning(message);
        }

        warning.put(f, message);
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
     */
    public void setRun(boolean run) {
        this.run = run;
    }
}
