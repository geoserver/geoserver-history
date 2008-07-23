/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */

/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Utility methods for locatating Config classes in the Servlet context.
 *
 * <p>
 * Called by ActionForms to lookup things in the WebContainer for the JSP page.
 * Similar to the Requests utility classes.
 * </p>
 *
 * <p>
 * These methods need to be kept in lockstep with the ConfigAction convience
 * methods.
 * </p>
 *
 * @author jgarnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id$
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
     * @param request DOCUMENT ME!
     *
     * @return Configuration information for the Web Map Server
     */

    //JD: kill this
    //    public static WMSConfig getWMSConfig(HttpServletRequest request) {
    //        HttpSession session = request.getSession();
    //        ServletContext context = session.getServletContext();
    //
    //        return (WMSConfig) context.getAttribute(WMSConfig.CONFIG_KEY);
    //    }

    /**
     * Access Web Feature Server Configuration Model from the WebContainer.
     *
     * <p>
     * Note that this represents the Configuration and not the state of the Web
     * Feature Server.
     * </p>
     *
     * @param request DOCUMENT ME!
     *
     * @return Configuration information for Web Feature Server
     */

    //	JD: kill this
    //    public static WFSConfig getWFSConfig(HttpServletRequest request) {
    //        HttpSession session = request.getSession();
    //        ServletContext context = session.getServletContext();
    //
    //        return (WFSConfig) context.getAttribute(WFSConfig.CONFIG_KEY);
    //    }

    /**
     * Access Web Map Server Configuration Model from the WebContainer.
     *
     * @param request DOCUMENT ME!
     *
     * @return Configuration model for Global information.
     */
    public static GlobalConfig getGlobalConfig(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();

        return (GlobalConfig) context.getAttribute(GlobalConfig.CONFIG_KEY);
    }

    /**
     * Access Catalog Configuration Model from the WebContainer.
     *
     * @param request DOCUMENT ME!
     *
     * @return Configuration model for Catalog information.
     */
    public static DataConfig getDataConfig(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ServletContext context = session.getServletContext();

        return (DataConfig) context.getAttribute(DataConfig.CONFIG_KEY);
    }
}
