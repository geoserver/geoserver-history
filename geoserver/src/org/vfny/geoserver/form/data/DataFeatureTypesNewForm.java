/*
 * Created on Jan 19, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;

/**
 * DataFeatureTypesNewForm purpose.
 * 
 * @author rgould, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: DataFeatureTypesNewForm.java,v 1.2 2004/01/20 07:57:45 jive Exp $
 */
public class DataFeatureTypesNewForm extends ActionForm {
	
	String selectedNewFeatureType;
	HttpServletRequest request;
	
	public SortedSet getNewFeatureTypes() {
		DataConfig dataConfig = (DataConfig) request.getSession().getServletContext().getAttribute(DataConfig.CONFIG_KEY);

		TreeSet out = new TreeSet(dataConfig.getFeatureTypeIdentifiers());
		out.removeAll(dataConfig.getFeaturesTypes().keySet());
		return out;
	}
	
	public void reset(ActionMapping arg0, HttpServletRequest request) {
		super.reset(arg0, request);
		this.request = request;
		
		selectedNewFeatureType = "";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}	
	/**
	 * Access selectedNewFeatureType property.
	 * 
	 * @return Returns the selectedNewFeatureType.
	 */
	public String getSelectedNewFeatureType() {
		return selectedNewFeatureType;
	}

	/**
	 * Set selectedNewFeatureType to selectedNewFeatureType.
	 *
	 * @param selectedNewFeatureType The selectedNewFeatureType to set.
	 */
	public void setSelectedNewFeatureType(String selectedNewFeatureType) {
		this.selectedNewFeatureType = selectedNewFeatureType;
	}

}
