/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.logging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;
import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.ServiceInfo;
import org.geoserver.platform.ExtensionPriority;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.util.logging.Logging;
import org.springframework.context.ApplicationContext;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoserverDataDirectory;


/**
 * Initializes GeoServer logging funcionality based on configuration settings.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class LoggingInitializer {
    public static final String RELINQUISH_LOG4J_CONTROL = "RELINQUISH_LOG4J_CONTROL";  

    /**
     * logging instance
     */
    static Logger LOGGER = Logging.getLogger( "org.geoserver.logging");
    
    GeoServerResourceLoader resourceLoader;
    
    public void setResourceLoader(GeoServerResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    public int getPriority() {
        //we want this to execute before other extensions initialize
        return ExtensionPriority.HIGHEST - 10;
    }
    
    public void initialize(GeoServer geoServer, ApplicationContext context) throws Exception {
        String strValue = GeoServerExtensions.getProperty(RELINQUISH_LOG4J_CONTROL, context);
        boolean relinquishLoggingControl = Boolean.valueOf(strValue);
        
        if(relinquishLoggingControl)
            return;
        
        initLogging( geoServer.getGlobal() );
        geoServer.addListener( new ConfigurationListener() {

            public void handleGlobalChange(GeoServerInfo global,
                    List<String> propertyNames, List<Object> oldValues,
                    List<Object> newValues) {
            
                boolean reload = false;
                if ( propertyNames.contains( "loggingLevel") ) {
                    reload = true;
                }
                if ( propertyNames.contains( "loggingLocation") ) {
                    reload = true;
                }
                if ( propertyNames.contains( "stdOutLogging" ) ) {
                    reload = true;
                }
               
                if ( reload ) {
                    try {
                        initLogging( global );
                    } 
                    catch (Exception e) {
                        throw new RuntimeException( e );
                    }
                }
            }

            public void handleServiceChange(ServiceInfo service,
                    List<String> propertyNames, List<Object> oldValues,
                    List<Object> newValues) {
            }
            
            public void reloaded() {
            }
        });
    }
    
    void initLogging( GeoServerInfo info ) throws Exception {
        
        String log4jConfigFileStr = info.getLoggingLevel();
        boolean suppressStdOutLogging = !info.isStdOutLogging();
        String logFileName = info.getLoggingLocation();
        
        //to initialize logging we need to do a couple of things:
        // 1)  Figure out whether the user has 'overridden' some configuration settings
        // in the logging system (not using log4j in commons-logging.properties or perhaps
        // has set up their own 'custom' log4j.properties file.
        // 2)  If they *have*, then we don't worry about configuring logging
        // 3)  If they haven't, then we configure logging to use the log4j config file
        // specified, and remove console appenders if the suppressstdoutlogging is true.
        LOGGER.fine("CONFIGURING GEOSERVER LOGGING -------------------------");
        
        if (log4jConfigFileStr == null) {
            log4jConfigFileStr = "DEFAULT_LOGGING.properties";
            LOGGER.warning("No log4jConfigFile defined in services.xml:  using 'DEFAULT_LOGGING.properties'");
        }
        
        File log4jConfigFile = resourceLoader.find( "logs", log4jConfigFileStr );
        if (log4jConfigFile == null) {
            //hmm, well, we don't have a log4j config file and this could be due to the fact
            //that this is a data-dir upgrade.  We can count on the DEFAULT_LOGGING.properties file
            //being present on the classpath, so we'll upgrade their data_dir and then use the
            //default DEFAULT_LOGGING.properties configuration.
            LOGGER.warning("log4jConfigFile '" + log4jConfigFileStr + "' couldn't be found in the data dir, so GeoServer will " +
            "install the various logging config file into the data dir, and then try to find it again.");
            
            //this forces the data_dir/logs directory to be present (if it wasn't already)
            File lcdir = resourceLoader.find( "logs" );
            if ( lcdir == null ) {
                lcdir = resourceLoader.createDirectory( "logs" );
            }
            
            //now we copy in the various logging config files from the base repo location on the classpath
            final String[] lcfiles = new String[] { "DEFAULT_LOGGING.properties",
                    "VERBOSE_LOGGING.properties",
                    "PRODUCTION_LOGGING.properties",
                    "GEOTOOLS_DEVELOPER_LOGGING.properties",
                    "GEOSERVER_DEVELOPER_LOGGING.properties" };
            
            for (int i = 0; i < lcfiles.length; i++) {
                File target = new File(lcdir.getAbsolutePath(), lcfiles[i]);
                if (!target.exists()) {
                    resourceLoader.copyFromClassPath(lcfiles[i], target);
                }
            }
            
            //ok, the possibly-new 'logs' directory is in-place, with all the various configs there.
            // Is the originally configured log4jconfigfile there now?
            log4jConfigFile = resourceLoader.find( "logs", log4jConfigFileStr );
            if (log4jConfigFile == null) {
                LOGGER.warning("Still couldn't find log4jConfigFile '" + log4jConfigFileStr + "'.  Using DEFAULT_LOGGING.properties instead.");
            }
            
            log4jConfigFile = resourceLoader.find( "logs", "DEFAULT_LOGGING.properties" );
        }

        if (log4jConfigFile == null || !log4jConfigFile.exists()) {
            throw new ConfigurationException("Unable to load logging configuration '" + log4jConfigFileStr + "'.  In addition, an attempt " +
                    "was made to create the 'logs' directory in your data dir, and to use the DEFAULT_LOGGING configuration, but" +
                    "this failed as well.  Is your data dir writeable?");
        }
        
                // reconfiguring log4j logger levels by resetting and loading a new set of configuration properties
        InputStream loggingConfigStream = new FileInputStream(log4jConfigFile);
        if (loggingConfigStream == null) {
            LOGGER.warning("Couldn't open Log4J configuration file '" + log4jConfigFile.getAbsolutePath());
            return;
        } else {
            LOGGER.fine("GeoServer logging profile '" + log4jConfigFile.getName() + "' enabled.");
        }

        configureGeoServerLogging(loggingConfigStream, suppressStdOutLogging, false, 
                                logFileName);
        
        
    }

    public static void configureGeoServerLogging(InputStream loggingConfigStream, boolean suppressStdOutLogging, boolean suppressFileLogging, String logFileName) throws FileNotFoundException, IOException,
                        ConfigurationException {
        //JD: before we wipe out the logging configuration, save any appenders that are not 
        // console or file based. This allows for other types of appenders to remain in tact
        // when geoserver is reloaded.
        List<Appender> appenders = new ArrayList();
        Enumeration a = LogManager.getRootLogger().getAllAppenders();
        while( a.hasMoreElements() ) {
            Appender appender = (Appender) a.nextElement();
            if ( !( appender instanceof ConsoleAppender || appender instanceof FileAppender )  ){ 
                //save it 
                appenders.add( appender );
            }
        }
        
        
        Properties lprops = new Properties();
        lprops.load(loggingConfigStream);
        LogManager.resetConfiguration();
//        LogLog.setQuietMode(true);
        PropertyConfigurator.configure(lprops);
//        LogLog.setQuietMode(false);
        
        // configuring the log4j file logger
        if(!suppressFileLogging) {
                Appender gslf = org.apache.log4j.Logger.getRootLogger().getAppender("geoserverlogfile");
                if (gslf instanceof org.apache.log4j.RollingFileAppender) {
                    if (logFileName == null ) {
                        logFileName = new File(GeoserverDataDirectory.findCreateConfigDir("logs"),  "geoserver.log").getAbsolutePath();
                    } else { 
                        if (!new File(logFileName).isAbsolute()) {
                            logFileName = new File(GeoserverDataDirectory.getGeoserverDataDirectory(), logFileName).getAbsolutePath();
                            LOGGER.fine("Non-absolute pathname detected for logfile.  Setting logfile relative to data dir.");
                        }
                    }
                    lprops.setProperty("log4j.appender.geoserverlogfile.File", logFileName);
                    PropertyConfigurator.configure(lprops);
                    LOGGER.fine("Logging output to file '" + logFileName + "'");
                } else if (gslf != null) {
                    LOGGER.warning("'log4j.appender.geoserverlogfile' appender is defined, but isn't a RollingFileAppender.  GeoServer won't control the file-based logging.");
                } else {
                    LOGGER.warning("'log4j.appender.geoserverlogfile' appender isn't defined.  GeoServer won't control the file-based logging.");
                }
        }
        
        // ... and the std output logging too
        if (suppressStdOutLogging) {
            LOGGER.warning("Suppressing StdOut logging.  If you want to see GeoServer logs, be sure to look in '" + logFileName + "'");
            Enumeration allAppenders = org.apache.log4j.Logger.getRootLogger().getAllAppenders();
            Appender curApp;
            while (allAppenders.hasMoreElements()) {
                curApp = (Appender)allAppenders.nextElement();
                if (curApp instanceof org.apache.log4j.ConsoleAppender) {
                    org.apache.log4j.Logger.getRootLogger().removeAppender(curApp);
                }
            }
        } 
        
        //add the appenders we saved above
        for ( Appender appender : appenders ) {
            LogManager.getRootLogger().addAppender( appender );
        }

        LOGGER.fine("FINISHED CONFIGURING GEOSERVER LOGGING -------------------------");
    }
}

