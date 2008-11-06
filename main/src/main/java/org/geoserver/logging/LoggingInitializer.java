package org.geoserver.logging;

import java.util.List;
import java.util.logging.Logger;

import org.geoserver.config.ConfigurationListener;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.GeoServerInitializer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.util.logging.Logging;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Initializes GeoServer logging funcionality based on configuration settings.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class LoggingInitializer implements GeoServerInitializer, ApplicationContextAware {

    /**
     * logging instance
     */
    static Logger LOGGER = Logging.getLogger( "org.geoserver.logging");
    
    GeoServerResourceLoader resourceLoader;

    Boolean relinquishLoggingControl;
    
    public void setResourceLoader(GeoServerResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    
    public void initialize(GeoServer geoServer) throws Exception {
        geoServer.addListener( new ConfigurationListener() {

            public void handleGlobalChange(GeoServerInfo global,
                    List<String> propertyNames, List<Object> oldValues,
                    List<Object> newValues) {
                
                //TODO: get rid of this hack checking singleton
                if (!relinquishLoggingControl ) {
                    boolean reload = false;
                    String loggingLevel = global.getLoggingLevel();
                    String loggingLocation = global.getLoggingLocation();
                    Boolean stdOutLogging = global.isStdOutLogging();
                    
                    if ( propertyNames.contains( "loggingLevel") ) {
                        loggingLevel = (String) newValues.get( propertyNames.indexOf( "loggingLevel" ) );
                        reload = true;
                    }
                    if ( propertyNames.contains( "loggingLocation") ) {
                        loggingLocation = (String) newValues.get( propertyNames.indexOf( "loggingLocation" ) );
                        reload = true;
                    }
                    if ( propertyNames.contains( "stdOutLogging" ) ) {
                        stdOutLogging = (Boolean) newValues.get( propertyNames.indexOf( "stdOutLogging" ) );
                        reload = true;
                    }
                   
                    if ( reload ) {
                        try {
                            LoggingUtils.initLogging(resourceLoader, loggingLevel, !stdOutLogging, loggingLocation);
                        } 
                        catch (Exception e) {
                            throw new RuntimeException( e );
                        }
                    }
                    
                }
            }

            public void handleServiceChange(ServiceInfo service,
                    List<String> propertyNames, List<Object> oldValues,
                    List<Object> newValues) {
            }
            
        });
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String strValue = GeoServerExtensions.getProperty(LoggingUtils.RELINQUISH_LOG4J_CONTROL, 
                applicationContext);
        relinquishLoggingControl = Boolean.valueOf(strValue);
    }
}

