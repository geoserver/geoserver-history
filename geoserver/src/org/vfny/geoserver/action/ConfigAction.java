/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.config.WFSConfig;
import org.vfny.geoserver.config.WMSConfig;


/**
 * GeoConfigAction is a common super class used by STRUTS Actions.
 * 
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
 * @author $Author: jive $ (last modification)
 * @version $Id: ConfigAction.java,v 1.1.2.9 2004/01/08 23:50:52 jive Exp $
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
        return (WMSConfig) getServlet().getServletContext().getAttribute(WMSConfig.CONFIG_KEY);
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
        return (WFSConfig) getServlet().getServletContext().getAttribute(WFSConfig.CONFIG_KEY);
    }

    /**
     * Access Web Map Server Configuration Model from the WebContainer.
     *
     * @return Configuration model for Global information.
     */
    protected GlobalConfig getGlobalConfig() {
        return (GlobalConfig) getServlet().getServletContext().getAttribute(GlobalConfig.CONFIG_KEY);
    }

    /**
     * Access Catalog Configuration Model from the WebContainer.
     *
     * @return Configuration model for Catalog information.
     */
    protected DataConfig getDataConfig() {
        return (DataConfig) getServlet().getServletContext().getAttribute(DataConfig.CONFIG_KEY);
    }
}
