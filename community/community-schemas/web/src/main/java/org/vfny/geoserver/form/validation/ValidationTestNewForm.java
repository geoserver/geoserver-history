/*
 * Created on Jan 23, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.validation;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.validation.ValidationConfig;
import java.util.Collection;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;


/**
 * ValidationTestNewForm purpose.
 * <p>
 * Description of ValidationTestNewForm ...
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
 * ValidationTestNewForm x = new ValidationTestNewForm(...);
 * </code></pre>
 *
 * @author User, Refractions Research, Inc.
 * @author $Author: emperorkefka $ (last modification)
 * @version $Id$
 */
public class ValidationTestNewForm extends ActionForm {
    private String newName;
    private String selectedPlugIn;

    //Key is the PlugIn name, Value is the description
    private Collection plugInConfigs;
    private Set plugInNames;

    public void reset(ActionMapping arg0, HttpServletRequest request) {
        super.reset(arg0, request);

        ValidationConfig validationConfig = (ValidationConfig) this.getServlet().getServletContext()
                                                                   .getAttribute(ValidationConfig.CONFIG_KEY);
        plugInConfigs = validationConfig.getPlugIns().values();
        plugInNames = validationConfig.getPlugInNames();

        newName = "";
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        return errors;
    }

    public Collection getPlugInConfigs() {
        return plugInConfigs;
    }

    public Set getPlugIns() {
        return plugInNames;
    }

    /**
     * Access newName property.
     *
     * @return Returns the newName.
     */
    public String getNewName() {
        return newName;
    }

    /**
     * Set newName to newName.
     *
     * @param newName The newName to set.
     */
    public void setNewName(String newName) {
        this.newName = newName;
    }

    /**
     * Access selectedPlugIn property.
     *
     * @return Returns the selectedPlugIn.
     */
    public String getSelectedPlugIn() {
        return selectedPlugIn;
    }

    /**
     * Set selectedPlugIn to selectedPlugIn.
     *
     * @param selectedPlugIn The selectedPlugIn to set.
     */
    public void setSelectedPlugIn(String selectedPlugIn) {
        this.selectedPlugIn = selectedPlugIn;
    }
}
