/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import javax.servlet.ServletException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;


/**
 * This class represents the state of the GeoServer appliaction.
 * 
 * <p>
 * ApplicationState used by the state.jsp tile as a single view on the state of
 * the GeoServer application. This class may be extended in the future to
 * provide runtime statistics.
 * </p>
 * 
 * <p>
 * This class is not a bean - content is updated based on methods. As an
 * example consider the following State diagram:
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ApplicationState.java,v 1.2 2004/01/12 21:01:27 dmzwiers Exp $
 */
public class ApplicationState implements PlugIn {
    /** The key used to store this value in the Web Container */
    public static final String WEB_CONTAINER_KEY = "GeoServer.ApplicationState";

    /** true if the configuration has been changed (but not applied) */
    private boolean configChanged = false;

    /** true if the geoserve setup has been changed (but not saved) */
    private boolean geoServerChanged = false;

    /**
     * Clean up the Configuration State during application exit.
     * 
     * <p>
     * Since this class just holds data, no resources need to be released.
     * </p>
     *
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy() {
    }

    /**
     * Set up the ApplicationState during Application start up.
     * 
     * <p>
     * ApplicationState simply registers itself with the WEB_CONTAINER_KEY
     * ("GeoServer.ApplicationState") during start up.
     * </p>
     *
     * @param actionServlet ActionServlet representing the Application
     * @param moduleConfig Configuration used to set up this plug in
     *
     * @throws ServletException
     *
     * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet,
     *      org.apache.struts.config.ModuleConfig)
     */
    public void init(ActionServlet actionServlet, ModuleConfig moduleConfig)
        throws ServletException {
        actionServlet.getServletContext().setAttribute(WEB_CONTAINER_KEY, this);
    }

    /**
     * True if the user has changed the Configuration and not yet applied them.
     *
     * @return <code>true</code> if Configuration needs changing.
     */
    public boolean isConfigChanged() {
        return configChanged;
    }

    /**
     * True if the user has changed GeoServer and not yet saved the changes.
     *
     * @return <code>true</code> if GeoServer has been changed (but not saved)
     */
    public boolean isGeoServerChanged() {
        return geoServerChanged;
    }

    /**
     * Notification that Global has been updated from XML config files
     */
    public void notifyLoadXML() {
        geoServerChanged = true;
        configChanged = false;
    }

    /**
     * Notification that Global has been updated from Configuration
     */
    public void notifyToGeoServer() {
        geoServerChanged = true;
        configChanged = false;
    }

    /**
     * Notification that Global has been saved to XML config files.
     */
    public void notifiySaveXML() {
        geoServerChanged = false;
    }

    /**
     * Notification that the User has changed the Configuration
     */
    public void notifyConfigChanged() {
        configChanged = true;
    }
}
