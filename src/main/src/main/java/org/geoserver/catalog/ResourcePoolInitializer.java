/* Copyright (c) 2001 - 2010 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.catalog;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import org.geoserver.config.ConfigurationListenerAdapter;
import org.geoserver.config.CoverageAccessInfo;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.config.GeoServerInitializer;
import org.geoserver.config.impl.CoverageAccessInfoImpl;

/**
 * Initializes parameters of the {@link ResourcePool} class from configuration.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class ResourcePoolInitializer implements GeoServerInitializer {

    GeoServer gs;
    
    public void initialize(GeoServer geoServer) throws Exception {
        this.gs = geoServer;
        
        final GeoServerInfo global = geoServer.getGlobal();
        final int cacheSize = global.getFeatureTypeCacheSize();
        if (cacheSize > 0) {
            gs.getCatalog().getResourcePool().setFeatureTypeCacheSize(cacheSize);
        }
        CoverageAccessInfo coverageAccess = global.getCoverageAccess();
        if (coverageAccess == null){
           coverageAccess = new CoverageAccessInfoImpl();
           global.setCoverageAccess(coverageAccess);
        }
        final ThreadPoolExecutor executor = coverageAccess.getThreadPoolExecutor();
        if (executor != null){
            gs.getCatalog().getResourcePool().setCoverageExecutor(executor);    
        }
        
        geoServer.addListener(new ConfigurationListenerAdapter() {
            @Override
            public void handleGlobalChange(GeoServerInfo global, List<String> propertyNames,
                    List<Object> oldValues, List<Object> newValues) {
                int i = propertyNames.indexOf( "featureTypeCacheSize" );
                if (i > -1) {
                    gs.getCatalog().getResourcePool().setFeatureTypeCacheSize(i);
                }
                gs.getCatalog().getResourcePool().setCoverageExecutor(global.getCoverageAccess().getThreadPoolExecutor());
            }
        });
    }
    
}
