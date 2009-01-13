/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.logging;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.geoserver.logging.LoggingUtils.GeoToolsLoggingRedirection;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.util.logging.CommonsLoggerFactory;
import org.geotools.util.logging.Log4JLoggerFactory;
import org.geotools.util.logging.Logging;
import org.vfny.geoserver.global.GeoserverDataDirectory;

/**
 * Listens for GeoServer startup and tries to configure logging redirection to
 * LOG4J, then configures LOG4J according to the GeoServer configuration files
 * (provided logging control hasn't been disabled)
 * 
 */
public class LoggingStartupContextListener implements ServletContextListener {
    private static Logger LOGGER;

    public void contextDestroyed(ServletContextEvent event) {
    }

    public void contextInitialized(ServletContextEvent event) {
        // setup GeoTools logging redirection (to log4j by default, but so that it can be overridden)
        final ServletContext context = event.getServletContext();
        GeoToolsLoggingRedirection logging = GeoToolsLoggingRedirection.findValue(
                GeoServerExtensions.getProperty(LoggingUtils.RELINQUISH_LOG4J_CONTROL, 
                context));
        try {
            if(logging == GeoToolsLoggingRedirection.JavaLogging) {
                // no redirection needed 
            } else if(logging == GeoToolsLoggingRedirection.CommonsLogging) {
                Logging.ALL.setLoggerFactory(CommonsLoggerFactory.getInstance());
            } else {
                Logging.ALL.setLoggerFactory(Log4JLoggerFactory.getInstance());
            }
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Could not configure log4j logging redirection", e);
        }
        
        String relinquishLoggingControl = GeoServerExtensions.getProperty(LoggingUtils.RELINQUISH_LOG4J_CONTROL, 
                context);
        if(Boolean.valueOf(relinquishLoggingControl)) {
            getLogger().info("RELINQUISH_LOG4J_CONTROL on, won't attempt to reconfigure LOG4J loggers");
        } else {
            try {
                File baseDir = new File(GeoserverDataDirectory.findGeoServerDataDir(context));
                GeoServerResourceLoader loader = new GeoServerResourceLoader(baseDir);
                LegacyLoggingImporter loggingImporter = new LegacyLoggingImporter();
                loggingImporter.imprt(baseDir);
                LoggingUtils.initLogging(loader, loggingImporter.getConfigFileName(), loggingImporter
                        .getSuppressStdOutLogging(), loggingImporter.getLogFile());
            } catch (Exception e) {
                getLogger().log(Level.SEVERE, "Could not configure log4j overrides", e);
            }
        }
    }
    
    Logger getLogger() {
        if(LOGGER == null) {
            LOGGER = Logging.getLogger("org.geoserver.logging");
        }
        return LOGGER;
    }
}
