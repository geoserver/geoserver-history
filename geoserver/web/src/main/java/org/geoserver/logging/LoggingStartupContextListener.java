/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.logging;

import org.geotools.util.Logging;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class LoggingStartupContextListener implements ServletContextListener {
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    public void contextInitialized(ServletContextEvent context) {
        Logger LOGGER = Logger.getLogger(this.getClass().toString());

        if (!Logging.GEOTOOLS.redirectToCommonsLogging()) {
            LOGGER.warning("Tried to send 'org.geotools' logging to the "
                + "commons-logging subsystem, but commons-logging is set"
                + " up to log to java logging.  Leaving default" + " logging behavior alone.");
        }

        Logging gsLogging = new Logging("org.geoserver");

        if (!gsLogging.redirectToCommonsLogging()) {
            LOGGER.warning("Tried to send 'org.geoserver' logging to the "
                + "commons-logging subsystem, but commons-logging is set"
                + " up to log to java logging.  Leaving default" + " logging behavior alone.");
        }

        gsLogging = new Logging("org.vfny");

        if (!gsLogging.redirectToCommonsLogging()) {
            LOGGER.warning("Tried to send 'org.vfny' logging to the "
                + "commons-logging subsystem, but commons-logging is set"
                + " up to log to java logging.  Leaving default" + " logging behavior alone.");
        }
    }
}
