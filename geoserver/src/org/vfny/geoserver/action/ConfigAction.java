/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import org.apache.struts.action.Action;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.config.data.CatalogConfig;
import org.vfny.geoserver.config.wfs.WFSConfig;
import org.vfny.geoserver.config.wms.WMSConfig;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * GeoServerAction is a common super class used by STRUTS Actions.
 * <p>
 * GeoServerAction is used to store shared services, such as looking up the
 * Configuration Model.
 * </p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * LoggedIn: Convience routines for checking if User has been Authenticated.
 * These will need to be extended in the future if we allow User based
 * Capabilities documents.
 * </li>
 * <li>
 * Config (Model) Access: Convience routines have been writen to allow access
 * to the Config Model from the Web Container.
 * </li>
 * </ul>
 * 
 * Example Use:
 * <pre><code>
 * class MyAction extends GeoServerAction {
 *   ...
 * }
 * </code></pre>
 * 
 * <p>
 * Please remember that Actions (like servlets) should never make use of
 * instance variables in order to remain thread-safe.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: ConfigAction.java,v 1.1.2.1 2004/01/02 19:14:21 jive Exp $
 */
public class ConfigAction extends Action {
    
    public boolean isLoggedIn(HttpServletRequest request) {
        return false;
    }

    public UserContainer getUser( HttpServletRequest request ){
        HttpSession session = request.getSession();
        UserContainer user = (UserContainer) request.getAttribute( UserContainer.SESSION_KEY );
        if( user == null ){
            user = new UserContainer( request.getLocale() );
            session.setAttribute( UserContainer.SESSION_KEY, user );
        }
        return user;                
    }
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
    protected WMSConfig getWMSConfig() {
        ServletContext context = getServlet().getServletContext();

        return (WMSConfig) context.getAttribute("GeoServer.WMSConfig");
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
    protected WFSConfig getWFSConfig() {
        ServletContext context = getServlet().getServletContext();

        return (WFSConfig) context.getAttribute("GeoServer.WFSConfig");
    }

    /**
     * Access Web Map Server Configuration Model from the WebContainer.
     *
     * @return Configuration model for Global information.
     */
    protected GlobalConfig getGlobalConfig() {
        ServletContext context = getServlet().getServletContext();

        return (GlobalConfig) context.getAttribute("GeoServer.GlobalConfig");
    }
    
    /**
     * Access Catalog Configuration Model from the WebContainer.
     *
     * @return Configuration model for Catalog information.
     */
    protected CatalogConfig getCatalogConfig() {
        ServletContext context = getServlet().getServletContext();

        return (CatalogConfig) context.getAttribute("GeoServer.CatalogConfig");
    }    
}
