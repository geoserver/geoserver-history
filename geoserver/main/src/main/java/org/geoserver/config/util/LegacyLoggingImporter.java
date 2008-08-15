package org.geoserver.config.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.logging.Logger;

import org.geoserver.config.GeoServer;

import org.geoserver.config.GeoServerInfo;
import org.geotools.util.logging.Logging;

/**
 * Imports logging configuration from the legacy services.xml file.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 */
public class LegacyLoggingImporter extends LegacyImporterSupport {

    /** logger */
    static Logger LOGGER = Logging.getLogger( "org.geoserver.confg" );

    /**
     * configuration
     */
    GeoServer geoServer;

    public LegacyLoggingImporter( GeoServer geoServer ) {
        this.geoServer = geoServer;
    }
    
    public void imprt( File dir ) throws Exception {
        LegacyServicesReader reader = reader( dir );
        
        //grab the global config object, creating if necessary
        GeoServerInfo info = geoServer.getGlobal();
        if ( info == null ) {
            info = geoServer.getFactory().createGlobal();
            geoServer.setGlobal( info );
        }
        
        //read the logging info
        Map<String,Object> global = reader.global();
        
        info.setLoggingLevel( (String) global.get( "log4jConfigFile") );
        info.setLoggingLocation( (String) global.get( "logLocation") );
        
        if ( global.get( "suppressStdOutLogging" ) != null ) {
            info.setStdOutLogging( ! get( global, "suppressStdOutLogging", Boolean.class) );    
        }
        else {
            info.setStdOutLogging(true);
        }

    }
}
