package org.geoserver.config;

import java.io.File;

import org.geoserver.config.impl.GeoServerFactoryImpl;
import org.geoserver.config.impl.GeoServerImpl;
import org.geoserver.config.util.LegacyConfigurationImporter;
import org.geoserver.config.util.LegacyLoggingImporter;

import junit.framework.TestCase;

public class LegacyLoggingImporterTest extends TestCase {

    GeoServer gs;
    LegacyLoggingImporter importer;
    
    protected void setUp() throws Exception {
        gs = new GeoServerImpl();
        gs.setFactory( new GeoServerFactoryImpl() );
        
        importer = new LegacyLoggingImporter( gs );
        importer.imprt( 
            new File( getClass().getResource("services.xml").getFile() ).getParentFile()
        );
    }
    
    public void test() throws Exception {
        
        GeoServerInfo info = gs.getGlobal();
        assertEquals( "DEFAULT_LOGGING.properties", info.getLoggingLevel() );
        assertTrue( info.isStdOutLogging() );
        assertEquals( "logs/geoserver.log", info.getLoggingLocation() );
    }
}
