/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.vfny.geoserver.config.*;
import org.vfny.geoserver.oldconfig.*;

//Logging system
import org.vfny.geoserver.zserver.*;
import java.util.logging.*;
import javax.servlet.http.*;


/**
 * Initializes all logging functions.
 *
 * @author Rob Hranac, Vision for New York
 * @author Chris Holmes, TOPP
 * @version $Id: FreefsLog.java,v 1.13.4.1 2003/11/04 23:29:20 cholmesny Exp $
 */
public class FreefsLog extends HttpServlet {
    /** Standard logging instance for class */
    private static final Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.servlets");

    /** Default name for configuration directory */
    private static final String CONFIG_DIR = "data/";
    private GeoZServer server;

    /**
     * Initializes logging and config.
     */
    public void init() {
        //HACK: java.util.prefs are awful.  See
        //http://www.allaboutbalance.com/disableprefs.  When the site comes
        //back up we should implement their better way of fixing the problem.
        System.setProperty("java.util.prefs.syncInterval", "5000000");

        String root = this.getServletContext().getRealPath("/");
        String path = root + CONFIG_DIR;
        LOGGER.finer("init with path: " + path);

        try {
            ServerConfig.load(path);
        } catch (ConfigurationException ex) {
            LOGGER.severe("Can't initialize server: " + ex.getMessage());
            ex.printStackTrace();
        }

        /*
           ConfigInfo cfgInfo = ConfigInfo.getInstance(path);
                   if (cfgInfo.runZServer()) {
              try {
                  server = new GeoZServer(cfgInfo.getZServerProps());
                  server.start();
              } catch (java.io.IOException e) {
                  LOGGER.info("zserver module could not start: " + e.getMessage());
              }
                   }
         */
    }

    /**
     * Initializes logging.
     *
     * @param req The servlet request object.
     * @param res The servlet response object.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        //BasicConfigurator.configure();
    }

    /**
     * Closes down the zserver if it is running, and frees up resources.
     */
    public void destroy() {
        super.destroy();
        TypeRepository.getInstance().closeTypeResources();

        LOGGER.finer("shutting down zserver");

        if (server != null) {
            server.shutdown(1);
        }
    }
}
