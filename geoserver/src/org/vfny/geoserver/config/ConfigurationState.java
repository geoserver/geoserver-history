/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;

/**
 * ConfigurationState purpose.
 * <p>
 * Description of ConfigurationState ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ConfigurationState.java,v 1.1.2.1 2004/01/08 18:44:01 dmzwiers Exp $
 */
public class ConfigurationState implements PlugIn {

	private boolean configChanged = false;
	private boolean geoServerChanged = false;
	public static final String CONFIG_KEY = "Config.State";
	/**
	 * Implement destroy.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.apache.struts.action.PlugIn#destroy()
	 * 
	 * 
	 */
	public void destroy() {

	}

	/**
	 * Implement init.
	 * <p>
	 * Description ...
	 * </p>
	 * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet, org.apache.struts.config.ModuleConfig)
	 * 
	 * @param arg0
	 * @param arg1
	 * @throws javax.servlet.ServletException
	 */
	public void init(ActionServlet arg0, ModuleConfig arg1)
		throws ServletException {
		arg0.getServletContext().setAttribute(CONFIG_KEY,this);
	}

	/**
	 * isConfigChanged purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public boolean isConfigChanged() {
		return configChanged;
	}

	/**
	 * isGeoServerChanged purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public boolean isGeoServerChanged() {
		return geoServerChanged;
	}

	public void configSentToGeoServer(){
		geoServerChanged = true;
		configChanged = false;
	}

	public void geoServerSentToXML(){
		geoServerChanged = false;
	}

	public void configChanged(){
		configChanged = true;
	}
}
