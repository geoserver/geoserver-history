/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Utility methods for locatating Config classes in the Servlet context.
 * <p>
 * Called by ActionForms to lookup things in the WebContainer for the JSP page.
 * Similar to the Requests utility classes.
 * </p>
 * 
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ConfigRequests.java,v 1.1.2.1 2004/01/12 05:11:18 jive Exp $
 */
public class ConfigRequests {
    /**
     * Access Web Map Server Configuration Model from the WebContainer.
     * 
     * <p>
     * Note that this represents the Configuration and not the state of the Web
     * Feature Server.
     * </p>
     *
     * @return Configuration information for the Web Map Server
     */
    protected WMSConfig getWMSConfig(HttpServletRequest request){
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();                 
        return (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);
    }

    /**
     * Access Web Feature Server Configuration Model from the WebContainer.
     * 
     * <p>
     * Note that this represents the Configuration and not the state of the Web
     * Feature Server.
     * </p>
     *
     * @return Configuration information for Web Feature Server
     */
    protected WFSConfig getWFSConfig(HttpServletRequest request){
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();                 
        return (WFSConfig) context.getAttribute(WFSConfig.CONFIG_KEY);
    }

    /**
     * Access Web Map Server Configuration Model from the WebContainer.
     *
     * @return Configuration model for Global information.
     */
    protected GlobalConfig getGlobalConfig(HttpServletRequest request){
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();                 
        return (GlobalConfig) context.getAttribute(GlobalConfig.CONFIG_KEY);
    }

    /**
     * Access Catalog Configuration Model from the WebContainer.
     *
     * @return Configuration model for Catalog information.
     */
    protected DataConfig getDataConfig(HttpServletRequest request){
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();                 
        return (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
    }
}
