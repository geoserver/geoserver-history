/*
 * Created on Jan 13, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.vfny.geoserver.action.data;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.form.data.DataAttributeTypesSelectForm;
/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataAttributeTypesSelectAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
			ActionForm incomingForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws IOException, ServletException {
		
		DataAttributeTypesSelectForm form = (DataAttributeTypesSelectForm) incomingForm; 
		String action = form.getButtonAction();
		
		//SAVE SELECTED ATTRIBUTE AND FORWARD TO EDITOR
		if (action.equals("edit")) {
			request.getSession().setAttribute("selectedAttributeType", form.getSelectedAttributeType());
			form.reset(mapping, request);
			return mapping.findForward("dataConfigFeatureTypes");
		}
		
		if (action.equals("delete")) {
			//dataConfig.removeFeatureType(featureTypesForm.getSelectedFeatureType());
			return mapping.findForward("dataConfigFeatureTypes");
		}
		
		throw new ServletException("Action must equal either 'edit' or 'delete'");        		
	}
}
