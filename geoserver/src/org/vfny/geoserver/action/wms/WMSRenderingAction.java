package org.vfny.geoserver.action.wms;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.action.ConfigAction;
import org.vfny.geoserver.config.WMSConfig;

import org.vfny.geoserver.form.wms.WMSRenderingForm;
import org.vfny.geoserver.global.UserContainer;

public class WMSRenderingAction extends ConfigAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			UserContainer user, HttpServletRequest request, HttpServletResponse response
	) throws IOException, ServletException {
		
			WMSConfig config = getWMSConfig();
	        WMSRenderingForm renderingForm = (WMSRenderingForm) form;

	        config.setSvgRenderer(renderingForm.getSvgRenderer());
	        config.setSvgAntiAlias(renderingForm.getSvgAntiAlias());
	        getApplicationState().notifyConfigChanged();

	        return mapping.findForward("config");
	    }
}
