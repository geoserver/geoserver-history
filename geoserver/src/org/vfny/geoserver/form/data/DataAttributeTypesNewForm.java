/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesNewForm extends ActionForm {
	
	String selectedNewFeatureType;
	
	public void reset(ActionMapping arg0, HttpServletRequest request) {
		super.reset(arg0, request);
		
		selectedNewFeatureType="";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}
	/**
	 * @return Returns the selectedNewFeatureType.
	 */
	public String getSelectedNewFeatureType() {
		return selectedNewFeatureType;
	}

	/**
	 * @param selectedNewFeatureType The selectedNewFeatureType to set.
	 */
	public void setSelectedNewFeatureType(String selectedNewFeatureType) {
		this.selectedNewFeatureType = selectedNewFeatureType;
	}

}
