/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import java.util.ArrayList;

import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.config.util.XStreamServiceLoader;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.util.Version;

/**
 * Service loader for the Web Processing Service
 *
 * @author Lucas Reed, Refractions Research Inc
 * @author Justin Deoliveira, The Open Planning Project
 */
public class WPSLoader extends XStreamServiceLoader<WPSInfo> {
    public WPSLoader(GeoServerResourceLoader resourceLoader) {
        super(resourceLoader, "wps");
    }

    public String getServiceId() {
        return "wps";
    }
    
    public Class<WPSInfo> getServiceClass() {
        return WPSInfo.class;
    }

    protected WPSInfo createServiceFromScratch(GeoServer gs) {
        WPSInfoImpl wps = new WPSInfoImpl();
        wps.setId(getServiceId());
        wps.setGeoServer( gs );
        
        return wps;
    }
    
    @Override
    protected void initXStreamPersister(XStreamPersister xp, GeoServer gs) {
        xp.getXStream().alias( "wcs", WPSInfo.class, WPSInfoImpl.class );
    }
    
    @Override
    protected WPSInfo initialize(WPSInfo service) {
        if ( service.getVersions() == null ) {
            ((WPSInfoImpl)service).setVersions( new ArrayList() );
        }
        if ( service.getVersions().isEmpty() ) {
            service.getVersions().add( new Version( "1.0.0") );
        }
        return service;
    }
}