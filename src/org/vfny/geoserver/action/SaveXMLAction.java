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
import org.vfny.geoserver.global.xml.XMLConfigWriter;

/**
 * @author User
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SaveXMLAction extends GeoServerAction {
	public ActionForward execute(ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws IOException, ServletException {
			
			GeoServer gs = getGeoServer(request);
			ServletContext sc = request.getSession().getServletContext();
			File rootDir = new File(sc.getRealPath("/"));
			try{
				XMLConfigWriter.store(gs.toWMSDTO(),gs.toWFSDTO(),gs.toGeoServerDTO(),gs.toDataDTO(),rootDir);
			}catch(ConfigurationException e){
				throw new ServletException(e);
			}
			
			//HACK
			return mapping.findForward("welcome");
	}
}
