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

import org.vfny.geoserver.form.data.DataFeatureTypesSelectForm;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DataFeatureTypesSelectAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
			ActionForm incomingForm,
			HttpServletRequest request,
			HttpServletResponse response)
	throws IOException, ServletException {
		
		DataFeatureTypesSelectForm form = (DataFeatureTypesSelectForm) incomingForm;

		String selectedFeatureType = form.getSelectedFeatureTypeName();
		
		request.getSession().setAttribute("selectedFeatureType", form.getSelectedFeatureTypeName());
		return mapping.findForward("dataConfigFeatureTypes");
	}
}
