/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import java.net.URL;

import javax.servlet.ServletContext;

import org.vfny.geoserver.config.CatalogConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.config.WMSConfig;


/**
 * GeoConfigAction is a common super class used by STRUTS Actions.
 * <p>
 * ConfigAction is used to store shared services, such as looking up the
 * Configuration Model.
 * </p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * Config (Model) Access: Convience routines have been writen to allow access
 * to the Config Model from the Web Container.
 * </li>
 * </ul>
 * 
 * Example Use:
 * <pre><code>
 * class MyAction extends ConfigAction {
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
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: ConfigAction.java,v 1.1.2.6 2004/01/07 21:36:13 dmzwiers Exp $
 */
public class ConfigAction extends GeoServerAction {
    
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
        WFSConfig config =
            (WFSConfig) context.getAttribute("GeoServer.WFSConfig");
        if( config == null ){
        	// need to grab from global as soon as we have the api
        	// to do so!
            config = new WFSConfig();
            //config.setDescribeUrl("http://localhost:8080/wfs");
            config.setAbstract("Hello Richard? Testing? 1 2 3 Testing?");
            config.setAccessConstraints("none");
            config.setEnabled( true );
            config.setFees("A small fish");
            String [] keywords = {"GeoServer","Configuration","STRUTS","test"};
            config.setKeywords( keywords );
            config.setMaintainer("Refractions Research");
            config.setName("WFS");
            try{
	            config.setOnlineResource(new URL("http://vwfs.refractions.net/"));
            }catch(Exception e){}
            config.setTitle("Sample WFS Configuration");                        
        }
        return config; 
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
