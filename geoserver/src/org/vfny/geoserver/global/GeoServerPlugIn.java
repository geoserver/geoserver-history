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
 * Used to load the config into GeoServer. Is a pre-Condition for ConfigPlugIn. 
 * This is started by struts.
 * <p>
 * @see org.vfny.geoserver.config.ConfigPlugIn
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: GeoServerPlugIn.java,v 1.1.2.3 2004/01/08 01:31:59 dmzwiers Exp $
 */
public class GeoServerPlugIn implements PlugIn {
	
	/**
	 * To allow for this class to be used as a precondition, and be pre-inited.
	 * @see org.vfny.geoserver.config.ConfigPlugIn
	 */
	private boolean started = false;
	
	public void destroy(){}
	
	public void init(ActionServlet as, ModuleConfig mc) throws javax.servlet.ServletException{
		if(started)
			return;
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
		started = true;
	}
}
