/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.config.WMSConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.GeoServerDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
import org.vfny.geoserver.global.dto.WMSDTO;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class UpdateGSAction extends Action {
	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
		GeoServer gs = null;
		ServletContext sc = request.getSession().getServletContext();
		gs = (GeoServer)sc.getAttribute(GeoServer.WEB_CONTAINER_KEY);
		try{
			gs.load((WMSDTO)(((WMSConfig)sc.getAttribute(WMSConfig.CONFIG_KEY)).toDTO()),
				(WFSDTO)((WFSConfig)sc.getAttribute(WFSConfig.CONFIG_KEY)).toDTO(),
				(GeoServerDTO)((GlobalConfig)sc.getAttribute(GlobalConfig.CONFIG_KEY)).toDTO(),
				(DataDTO)((DataConfig)sc.getAttribute(DataConfig.CONFIG_KEY)).toDTO());
		}catch(ConfigurationException e){
			throw new ServletException(e);
		}
			
		//HACK
		return mapping.findForward("welcome");
	}
}
