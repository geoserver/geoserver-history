/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

package org.vfny.geoserver.form.data;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 */
public class DataCoveragesSelectForm extends ActionForm {

	/**
	 * 
	 * @uml.property name="selectedCoverageName" multiplicity="(0 1)"
	 */
	private String selectedCoverageName;

	/**
	 * 
	 * @uml.property name="buttonAction" multiplicity="(0 1)"
	 */
	private String buttonAction;


    public void reset(ActionMapping arg0, HttpServletRequest arg1) {
        super.reset(arg0, arg1);
        selectedCoverageName = "";
    }

    public ActionErrors validate(ActionMapping mapping,
        HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        return errors;
    }

	/**
	 * DOCUMENT ME!
	 * 
	 * @return Returns the selectedCoverageName.
	 * 
	 * @uml.property name="selectedCoverageName"
	 */
	public String getSelectedCoverageName() {
		return selectedCoverageName;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param selectedCoverageName The selectedCoverageName to set.
	 * 
	 * @uml.property name="selectedCoverageName"
	 */
	public void setSelectedCoverageName(String selectedCoverageName) {
		this.selectedCoverageName = selectedCoverageName;
	}


    public Set getCoverageNames() {
        ServletContext context = getServlet().getServletContext();
        DataConfig config = (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);

        return config.getCoverages().keySet();
    }

	/**
	 * Access buttonAction property.
	 * 
	 * @return Returns the buttonAction.
	 * 
	 * @uml.property name="buttonAction"
	 */
	public String getButtonAction() {
		return buttonAction;
	}

	/**
	 * Set buttonAction to buttonAction.
	 * 
	 * @param buttonAction The buttonAction to set.
	 * 
	 * @uml.property name="buttonAction"
	 */
	public void setButtonAction(String buttonAction) {
		this.buttonAction = buttonAction;
	}

}
