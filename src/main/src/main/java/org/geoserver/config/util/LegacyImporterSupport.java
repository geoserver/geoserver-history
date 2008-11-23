package org.geoserver.config.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Base class factoring out some basic utility methods from various legacy importers.
 * 
 * @author Justin Deoliveira, OpenGEO
 *
 */
public class LegacyImporterSupport {

    protected LegacyServicesReader reader( File dir ) throws Exception {
        // services.xml
        File servicesFile = new File(dir, "services.xml");
        if (!servicesFile.exists()) {
            throw new FileNotFoundException(
                    "Could not find services.xml under:"
                            + dir.getAbsolutePath());
        }

        //create a services.xml reader
        LegacyServicesReader reader = new LegacyServicesReader();
        reader.read(servicesFile);
        
        return reader;
    }
    
    protected Object value(Object value, Object def) {
        return value != null ? value : def;
    }

    protected <T extends Object> T get(Map map, String key,
            Class<T> clazz) {
        Object o = map.get( key );
        if ( o == null ) {
            return null;
        }
        
        return (T) o;
    }

}
