/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action.data;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.StyleConfig;
import org.vfny.geoserver.form.data.DataStylesForm;

/**
 * @author rgould
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DataStylesAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
			DataStylesForm stylesForm = (DataStylesForm) form;
	
			String action = stylesForm.getAction();
			
			String filename = stylesForm.getFilename();
			String styleID = stylesForm.getStyleID();
		
			boolean _default = stylesForm.is_default();
			if (stylesForm.isDefaultChecked()) {
				_default = false;
			}
	
			DataConfig dataConfig = (DataConfig) getDataConfig();			
			StyleConfig config = null;		
		
			if (action.equals("edit") || action.equals("submit")) {
				config = (StyleConfig) dataConfig.getStyle(stylesForm.getSelectedStyle());
			} else if (action.equals("new")) {
				config = new StyleConfig();
			}
		
			//If they push edit, simply forward them back so the information is repopulated.
			if (action.equals("edit")) {
				stylesForm.reset(mapping, request);
				return mapping.findForward("dataConfigStyles");
			}
		
			if (action.equals("delete")) {
				dataConfig.removeStyle(stylesForm.getSelectedStyle());
			} else {
				
				config.setFilename(new File(filename));
				config.setDefault(_default);
				config.setId(styleID);
			
			
				//Do configuration parameters here.
		
				dataConfig.addStyle(styleID, config);
			}
			
			stylesForm.reset(mapping, request);					
		return mapping.findForward("dataConfigStyles");
	}
}
