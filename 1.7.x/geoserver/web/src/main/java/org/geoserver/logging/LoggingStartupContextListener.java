/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.logging;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.geotools.util.logging.Log4JLoggerFactory;
import org.geotools.util.logging.Logging;

/**
 * Listens for GeoServer startup and tries to configure logging redirection to LOG4J
 *
 */
public class LoggingStartupContextListener implements ServletContextListener {
    private static final Logger LOGGER = Logging.getLogger("org.geoserver.logging");

    public void contextDestroyed(ServletContextEvent arg0) {
    }

    public void contextInitialized(ServletContextEvent context) {
        try {
            Logging.ALL.setLoggerFactory(Log4JLoggerFactory.getInstance());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Could not configure log4j logging redirection", e);
        }
    }
}
