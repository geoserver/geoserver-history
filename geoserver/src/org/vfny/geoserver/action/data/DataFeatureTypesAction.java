/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
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
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.form.data.DataFeatureTypesForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataFeatureTypesAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
			
		DataFeatureTypesForm featureTypesForm = (DataFeatureTypesForm) form;
		
		String name = featureTypesForm.getName();
		String SRS = featureTypesForm.getSRS();
		String title = featureTypesForm.getTitle();
		String latLonBoundingBox = featureTypesForm.getLatlonBoundingBox();
		String keywords = featureTypesForm.getKeywords();
		String _abstract = featureTypesForm.get_abstract();
		
		DataConfig config = (DataConfig) getDataConfig();		
			
		return mapping.findForward("data.featureTypes");
	}

}
