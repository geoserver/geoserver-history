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

    protected <T extends Object> T get(Map map, String key, Class<T> clazz, T def ) {
        Object o = map.get( key );
        if ( o == null ) {
            if ( def != null ) {
                return def;
            }
            
            //check for primitive type
            if ( clazz.isPrimitive() ) {
                if ( clazz == int.class ) {
                    return (T) Integer.valueOf( 0 );
                }
                if ( clazz == double.class ) {
                    return (T) Double.valueOf( 0d ); 
                }
                if ( clazz == boolean.class ) {
                    return (T) Boolean.FALSE;
                }
            }
            return null;
        }
        
        return (T) o;
    }
    
    protected <T extends Object> T get(Map map, String key,
        Class<T> clazz) {
        return get( map, key, clazz, null );
    }

}
