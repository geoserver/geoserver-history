/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.apache.struts.action.PlugIn;
import org.apache.struts.action.*;
import org.apache.struts.config.*;
import javax.servlet.*;
import org.vfny.geoserver.global.xml.XMLConfigReader;
import java.io.*;

/**
 * GeoServerPlugIn purpose.
 * <p>
 * Description of GeoServerPlugIn ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: GeoServerPlugIn.java,v 1.1.2.1 2004/01/06 22:05:08 dmzwiers Exp $
 */
public class GeoServerPlugIn implements PlugIn {
	public GeoServerPlugIn(){}
	public void destroy(){}
	
	public void init(ActionServlet as, ModuleConfig mc) throws javax.servlet.ServletException{
		ServletContext sc = as.getServletContext();
		String rootDir = sc.getRealPath("/");
    	
		try{
			File f = new File(rootDir);
			XMLConfigReader cr = new XMLConfigReader(f);
			GeoServer gs = new GeoServer();
			sc.setAttribute(GeoServer.NAME,gs);
			if(cr.isInitialized()){
				gs.load(cr.getWms(),cr.getWfs(),cr.getGeoServer(),cr.getData());
			}else
				throw new ConfigurationException("An error occured loading the initial configuration.");
		}catch(ConfigurationException e){
			sc.setAttribute(GeoServer.NAME,null);
			throw new ServletException(e);
		}
	}
}
