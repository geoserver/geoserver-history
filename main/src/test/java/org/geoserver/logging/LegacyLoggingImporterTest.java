package org.geoserver.logging;

import java.io.File;

import junit.framework.TestCase;

import org.geoserver.config.GeoServer;
import org.geoserver.config.impl.GeoServerImpl;

public class LegacyLoggingImporterTest extends TestCase {

    GeoServer gs;
    LegacyLoggingImporter importer;
    
    protected void setUp() throws Exception {
        gs = new GeoServerImpl();
        
        importer = new LegacyLoggingImporter();
        importer.imprt( 
            new File( getClass().getResource("services.xml").getFile() ).getParentFile()
        );
    }
    
    public void test() throws Exception {
        assertEquals( "DEFAULT_LOGGING.properties", importer.getConfigFileName() );
        assertFalse( importer.getSuppressStdOutLogging() );
        assertEquals( "logs/geoserver.log", importer.getLogFile());
    }
}
