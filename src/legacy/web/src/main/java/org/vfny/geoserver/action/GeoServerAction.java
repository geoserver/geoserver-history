/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import org.apache.struts.action.Action;
import org.geoserver.catalog.Catalog;
import org.geoserver.wfs.WFS;
import org.springframework.web.struts.ActionSupport;
import org.vfny.geoserver.global.ApplicationState;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.Requests;
import org.vfny.geoserver.util.RequestsLegacy;

import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;


/**
 * GeoServerAction is a common super class used by STRUTS Actions.
 *
 * <p>
 * GeoServerAction is used to store shared services, such as looking up the
 * GeoServer Application.
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
 * GeoServer (Application) Access: Convience routines have been writen to allow
 * access to the GeoServer Application from the Web Container.
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
 * <p>
 * The Services provided by this class are convience methods for the Services
 * provided by the Requests utiltiy class.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: cholmesny $ (last modification)
 * @version $Id$
 */
public class GeoServerAction extends ActionSupport {
    /** Class logger */
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.vfny.geoserver.action");

    //    /**
    //     * Logs the user out from the current Session.
    //     *
    //     * @param request DOCUMENT ME!
    //     */
    //    public void logOut(HttpServletRequest request) {
    //        Requests.logOut(request);
    //    }
    //
    //    /**
    //     * Tests if the user has logged onto the current Session
    //     *
    //     * @param request DOCUMENT ME!
    //     *
    //     * @return DOCUMENT ME!
    //     */
    //    public boolean isLoggedIn(HttpServletRequest request) {
    //        return Requests.isLoggedIn(request);
    //    }

    /**
     * Aquire type safe session information in a UserContainer.
     *
     * <p>
     * Please note that the UserContainer may be lazyly created.
     * </p>
     *
     * @param request Http Request used to aquire session reference
     *
     * @return UserContainer containing typesafe session information.
     */
    public UserContainer getUserContainer(HttpServletRequest request) {
        return RequestsLegacy.getUserContainer(request);
    }

    /**
     * Aquire WMS from Web Container.
     *
     * <p>
     * The WMS instance is create by a STRUTS plug-in and is available
     * through the Web container. (Test cases may seed the request object with
     * a Mock WebContainer and a Mock WMS)
     * </p>
     *
     * @param request HttpServletRequest used to aquire session reference
     *
     * @return WMS instance for this Web Application
     */
    public WMS getWMS(HttpServletRequest request) {
        return (WMS) getWebApplicationContext().getBean("wms-legacy");
    }

    /**
     * Aquire WCS from Web Container.
     *
     * <p>
     * The WCS instance is create by a STRUTS plug-in and is available
     * through the Web container. (Test cases may seed the request object with
     * a Mock WebContainer and a Mock WMS)
     * </p>
     *
     * @param request HttpServletRequest used to aquire session reference
     *
     * @return WCS instance for this Web Application
     */
    public WCS getWCS(HttpServletRequest request) {
        return (WCS) getWebApplicationContext().getBean("wcs");
    }

    /**
     * Aquire WFS from Web Container.
     *
     * <p>
     * The WFS instance is create by a STRUTS plug-in and is available
     * through the Web container. (Test cases may seed the request object with
     * a Mock WebContainer and a Mock WFS)
     * </p>
     *
     * @param request HttpServletRequest used to aquire session reference
     *
     * @return WFS instance for this Web Application
     */
    public WFS getWFS(HttpServletRequest request) {
        return (WFS) getWebApplicationContext().getBean("wfs");
    }

    /**
     * Aquire global configuration from the Spring context.

     * @return Global configuration of this web app
     */
    public GeoServer getGeoServer() {
        return (GeoServer) getWebApplicationContext().getBean("geoServer");
    }
    
    public org.geoserver.config.GeoServer getConfiguration() {
        return (org.geoserver.config.GeoServer) getWebApplicationContext().getBean( "geoServer2" );
    }
    
    public Catalog getCatalog() {
        return getConfiguration().getCatalog();
    }
    
    /**
     * Access GeoServer Application State from the WebContainer.
     *
     * @param request DOCUMENT ME!
     *
     * @return Configuration model for Catalog information.
     */
    protected ApplicationState getApplicationState(HttpServletRequest request) {
        return (ApplicationState) getWebApplicationContext().getBean("applicationState");
    }
}
