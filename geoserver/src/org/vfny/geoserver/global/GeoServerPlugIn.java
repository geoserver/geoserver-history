/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.vfny.geoserver.global.xml.XMLConfigReader;

/**
 * GeoServerPlugIn purpose.
 * <p>
 * Description of GeoServerPlugIn ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: GeoServerPlugIn.java,v 1.1.2.2 2004/01/06 23:03:12 dmzwiers Exp $
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
			sc.setAttribute(GeoServer.SESSION_KEY,gs);
			if(cr.isInitialized()){
				gs.load(cr.getWms(),cr.getWfs(),cr.getGeoServer(),cr.getData());
			}else
				throw new ConfigurationException("An error occured loading the initial configuration.");
		}catch(ConfigurationException e){
			sc.setAttribute(GeoServer.SESSION_KEY,null);
			throw new ServletException(e);
		}
	}
}
