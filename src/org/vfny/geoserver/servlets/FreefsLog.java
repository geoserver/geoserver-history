/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.servlets;

import org.geotools.data.jdbc.ConnectionPoolManager;
import org.vfny.geoserver.zserver.GeoZServer;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Initializes all logging functions.
 *
 * @author Rob Hranac, Vision for New York
 * @author Chris Holmes, TOPP
 * @version $Id: FreefsLog.java,v 1.19 2004/01/31 00:27:29 jive Exp $
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
     *
     * @throws ServletException DOCUMENT ME!
     */
    public void init() throws ServletException {
        //HACK: java.util.prefs are awful.  See
        //http://www.allaboutbalance.com/disableprefs.  When the site comes
        //back up we should implement their better way of fixing the problem.
        System.setProperty("java.util.prefs.syncInterval", "5000000");

        //if(GeoServer.getInstance()==null){
        //	(new GeoServer()).init(this);
        //}

        /*ServletContext sc = getServletContext();
           GeoServer gs = (GeoServer)sc.getAttribute(GeoServer.WEB_CONTAINER_KEY);
           ConfigInfo cfgInfo = ConfigInfo.getInstance(path, gs);
                   if (cfgInfo.runZServer()) {
              try {
                  server = new GeoZServer(cfgInfo.getZServerProps());
                  server.start();
              } catch (java.io.IOException e) {
                  LOGGER.info("zserver module could not start: " + e.getMessage());
              }
                   }*/
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
     *
     * @task REVISIT: what we should consider is having geotools provide a
     *       nicer way to clean up datastores's resources, something like a
     *       close, so that we could just iterate through all the datastores
     *       calling close. Once that's done we can clean up this method a
     *       bit.
     */
    public void destroy() {
        super.destroy();
        ConnectionPoolManager.getInstance().closeAll();

        /*
           HACK: we must get a standard API way for releasing resources...
         */
        try {
            Class sdepfClass = Class.forName(
                    "org.geotools.data.sde.SdeConnectionPoolFactory");

            LOGGER.fine("SDE datasource found, releasing resources");

            java.lang.reflect.Method m = sdepfClass.getMethod("getInstance",
                    new Class[0]);
            Object pfInstance = m.invoke(sdepfClass, new Object[0]);

            LOGGER.fine("got sde connection pool factory instance: "
                + pfInstance);

            java.lang.reflect.Method closeMethod = pfInstance.getClass()
                                                             .getMethod("closeAll",
                    new Class[0]);

            closeMethod.invoke(pfInstance, new Object[0]);
            LOGGER.info("just asked SDE datasource to release connections");
        } catch (ClassNotFoundException cnfe) {
            LOGGER.fine("No SDE datasource found");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        LOGGER.finer("shutting down zserver");

        if (server != null) {
            server.shutdown(1);
        }
    }
}
