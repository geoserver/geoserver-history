/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.util.SortedSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataFeatureTypesSelectForm extends ActionForm {
	
	private String selectedFeatureTypeName;
	
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		
		selectedFeatureTypeName="";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}	

	/**
	 * @return Returns the selectedFeatureTypeName.
	 */
	public String getSelectedFeatureTypeName() {
		return selectedFeatureTypeName;
	}

	/**
	 * @param selectedFeatureTypeName The selectedFeatureTypeName to set.
	 */
	public void setSelectedFeatureTypeName(String selectedFeatureTypeName) {
		this.selectedFeatureTypeName = selectedFeatureTypeName;
	}
	
	public SortedSet getTypeNames() {
		ServletContext context = getServlet().getServletContext();
		DataConfig config =
			(DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);	
		
		return config.getFeatureTypeIdentifiers();
	}

}
