/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.action;

import org.apache.struts.action.Action;
import org.vfny.geoserver.global.ApplicationState;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.UserContainer;
import org.vfny.geoserver.requests.Requests;
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
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: GeoServerAction.java,v 1.3 2004/01/21 00:26:09 dmzwiers Exp $
 */
public class GeoServerAction extends Action {
    /**
     * Logs the user out from the current Session.
     *
     * @param request DOCUMENT ME!
     */
    public void logOut(HttpServletRequest request) {
        Requests.logOut(request);
    }

    /**
     * Tests if the user has logged onto the current Session
     *
     * @param request DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLoggedIn(HttpServletRequest request) {
        return Requests.isLoggedIn(request);
    }

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
        return Requests.getUserContainer(request);
    }

    /**
     * Aquire GeoServer from Web Container.
     * 
     * <p>
     * The GeoServer instance is create by a STRUTS plug-in and is available
     * through the Web container. (Test cases may seed the request object with
     * a Mock WebContainer and a Mock GeoServer)
     * </p>
     *
     * @param request HttpServletRequest used to aquire session reference
     *
     * @return GeoServer instance for this Web Application
     */
    public GeoServer getGeoServer(HttpServletRequest request) {
        return Requests.getGeoServer(request);
    }

    /**
     * Access GeoServer Application State from the WebContainer.
     *
     * @param request DOCUMENT ME!
     *
     * @return Configuration model for Catalog information.
     */
    protected ApplicationState getApplicationState(HttpServletRequest request) {
        return Requests.getApplicationState(request);
    }
}
