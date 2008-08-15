package org.geoserver.config;

import java.io.File;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.impl.GeoServerFactoryImpl;
import org.geoserver.config.impl.GeoServerImpl;
import org.geoserver.config.util.LegacyConfigurationImporter;

import junit.framework.TestCase;

public class LegacyConfigurationImporterTest extends TestCase {

    LegacyConfigurationImporter importer;
    
    protected void setUp() throws Exception {
        GeoServer gs = new GeoServerImpl();
        gs.setFactory( new GeoServerFactoryImpl() );
        
        importer = new LegacyConfigurationImporter( gs );
        importer.imprt( 
            new File( getClass().getResource("services.xml").getFile() ).getParentFile()
        );
    }
    
    public void testGlobal() throws Exception {
        GeoServerInfo info = importer.getConfiguration().getGlobal();
        assertNotNull( info );
        
        assertEquals( 1000000, info.getMaxFeatures() );
        assertFalse( info.isVerbose() );
        assertFalse( info.isVerboseExceptions() );  
        assertEquals( 8, info.getNumDecimals() );
        assertEquals( "UTF-8", info.getCharset() );
        assertEquals( 3, info.getUpdateSequence() );
    }
}
