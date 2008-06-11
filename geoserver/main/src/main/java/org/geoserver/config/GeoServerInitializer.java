package org.geoserver.config;


/**
 * Extension point interface for initializing based on configuration.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface GeoServerInitializer {

    void initialize( GeoServer geoServer ) throws Exception;
}
