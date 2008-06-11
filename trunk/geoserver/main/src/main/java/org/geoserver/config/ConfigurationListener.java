package org.geoserver.config;

import java.util.List;

/**
 *  
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface ConfigurationListener {

    /**
     * Handles a change to the global configuration.
     * 
     * @param global The global config object.
     * @param propertyNames The names of the properties that were changed.
     * @param oldValue The old values for the properties that were changed.
     * @param newValue The new values for the properties that were changed.
     */
    void handleGlobalChange( GeoServerInfo global, List<String> propertyNames, List<Object> oldValues, List<Object> newValues );
    
    /**
     * Handles a change to a service configuration.
     * 
     * @param service The service config object.
     * @param propertyNames The names of the properties that were changed.
     * @param oldValue The old values for the properties that were changed.
     * @param newValue The new values for the properties that were changed.
     */
    void handleServiceChange( ServiceInfo service, List<String> propertyNames, List<Object> oldValues, List<Object> newValues );
}
