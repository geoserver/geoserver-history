/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.vfny.geoserver.global.xml.XMLConfigReader;
import java.io.File;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;


/**
 * GeoServerPlugIn purpose.
 * 
 * <p>
 * Used to load the config into GeoServer. Is a pre-Condition for ConfigPlugIn.
 * This is started by struts.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: GeoServerPlugIn.java,v 1.3 2004/01/21 00:26:07 dmzwiers Exp $
 *
 * @see org.vfny.geoserver.config.ConfigPlugIn
 */
public class GeoServerPlugIn implements PlugIn {
    /**
     * To allow for this class to be used as a precondition, and be pre-inited.
     *
     * @see org.vfny.geoserver.config.ConfigPlugIn
     */
    private boolean started = false;

    /**
     * Implement destroy.
     * 
     * <p>
     * Does Nothing
     * </p>
     *
     * @see org.apache.struts.action.PlugIn#destroy()
     */
    public void destroy() {
    }

    /**
     * Implement init.
     * 
     * <p>
     * This does the load of the config files for GeoServer. Check the struts
     * configuration if this is not laoding correctly.
     * </p>
     *
     * @param as Used to get ServletContext
     * @param mc Not used
     *
     * @throws javax.servlet.ServletException
     * @throws ServletException when a load error occurs
     *
     * @see org.apache.struts.action.PlugIn#init(org.apache.struts.action.ActionServlet,
     *      org.apache.struts.config.ModuleConfig)
     */
    public void init(ActionServlet as, ModuleConfig mc)
        throws javax.servlet.ServletException {
        if (started) {
            return;
        }

        ServletContext sc = as.getServletContext();
        String rootDir = sc.getRealPath("/");

        try {
            File f = new File(rootDir);
            XMLConfigReader cr = new XMLConfigReader(f);
            GeoServer gs = new GeoServer();
            sc.setAttribute(GeoServer.WEB_CONTAINER_KEY, gs);

            if (cr.isInitialized()) {
                gs.load(cr.getWms(), cr.getWfs(), cr.getGeoServer(),
                    cr.getData());
            } else {
                throw new ConfigurationException(
                    "An error occured loading the initial configuration.");
            }
        } catch (ConfigurationException e) {
            sc.setAttribute(GeoServer.WEB_CONTAINER_KEY, null);
            throw new ServletException(e);
        }

        started = true;
    }
}
