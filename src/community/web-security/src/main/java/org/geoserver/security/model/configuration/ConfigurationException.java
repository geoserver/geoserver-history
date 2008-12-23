package org.geoserver.security.model.configuration;

/**
 * Configuration Exception
 * 
 * @author Francesco Izzi (geoSDI)
 */

public class ConfigurationException extends RuntimeException {
    
    public ConfigurationException() {
        super();
    }
    
    public ConfigurationException(String s) {
        super(s);
    }
    
    public ConfigurationException(Exception e) {
        super(e);
    }

}
