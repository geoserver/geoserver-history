/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.form.data;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesSelectForm extends ActionForm {
	
	private String buttonAction;
	private String selectedAttributeType;
	
	//we must save the request so getAttributeTypes can function
	private HttpServletRequest request;
	
	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		super.reset(arg0, arg1);
		
		request = arg1;
		
		buttonAction ="";
		selectedAttributeType="";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errors = new ActionErrors();
		
		return errors;
	}
	
	public SortedSet getAttributeTypes() {
		
		ServletContext context = getServlet().getServletContext();
		DataConfig config =
			(DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
        System.out.println("ABSCOND!");

		FeatureTypeConfig ftConfig = (FeatureTypeConfig) request.getSession().getAttribute(DataConfig.SELECTED_FEATURE_TYPE);		
		System.out.println("Return!!" +ftConfig);
		
		return Collections.unmodifiableSortedSet(new TreeSet(
					ftConfig.getSchema()
				));
	}
	/**
	 * @return Returns the buttonAction.
	 */
	public String getButtonAction() {
		return buttonAction;
	}

	/**
	 * @param buttonAction The buttonAction to set.
	 */
	public void setButtonAction(String buttonAction) {
		this.buttonAction = buttonAction;
	}

	/**
	 * @return Returns the selectedAttributeType.
	 */
	public String getSelectedAttributeType() {
		return selectedAttributeType;
	}

	/**
	 * @param selectedAttributeType The selectedAttributeType to set.
	 */
	public void setSelectedAttributeType(String selectedAttributeType) {
		this.selectedAttributeType = selectedAttributeType;
	}

}
