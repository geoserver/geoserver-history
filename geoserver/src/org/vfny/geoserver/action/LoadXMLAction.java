/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.vfny.geoserver.action;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.xml.XMLConfigReader;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LoadXMLAction extends GeoServerAction {
	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
			
		ServletContext sc = request.getSession().getServletContext();
		String rootDir = sc.getRealPath("/");
    	
		try{
			File f = new File(rootDir);
			XMLConfigReader cr = new XMLConfigReader(f);
			GeoServer gs = new GeoServer();
			sc.setAttribute(GeoServer.SESSION_KEY,gs);
			if(cr.isInitialized()){
				gs.load(cr.getWms(),cr.getWfs(),cr.getGeoServer(),cr.getData());
			}else
				throw new ConfigurationException("An error occured loading the initial configuration.");
		}catch(ConfigurationException e){
			sc.setAttribute(GeoServer.SESSION_KEY,null);
			throw new ServletException(e);
		}

		//HACK
		return mapping.findForward("welcome");
	}
}
