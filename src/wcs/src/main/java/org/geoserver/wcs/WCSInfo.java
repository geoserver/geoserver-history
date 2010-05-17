package org.geoserver.wcs;

import org.geoserver.config.ServiceInfo;

/**
 * Service configuration object for Web Coverage Service.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public interface WCSInfo extends ServiceInfo {

    /**
     * Flag determining if gml prefixing is used.
     */
    boolean isGMLPrefixing();
    
    /**
     * Sets flag determining if gml prefixing is used.
     */
    void setGMLPrefixing( boolean gmlPrefixing );
}
