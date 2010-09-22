/* Copyright (c) 2010 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.wms;

import java.util.ArrayList;

import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.config.util.XStreamServiceLoader;
import org.geoserver.platform.GeoServerResourceLoader;
import org.geotools.util.Version;

/**
 * Loads and persist the {@link WMSInfo} object to and from xstream persistence.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public class WMSXStreamLoader extends XStreamServiceLoader<WMSInfo> {

    public WMSXStreamLoader(GeoServerResourceLoader resourceLoader) {
        super(resourceLoader, "wms");
    }

    public Class<WMSInfo> getServiceClass() {
        return WMSInfo.class;
    }

    protected WMSInfo createServiceFromScratch(GeoServer gs) {
        return new WMSInfoImpl();
    }

    @Override
    protected void initXStreamPersister(XStreamPersister xp, GeoServer gs) {
        xp.getXStream().alias("wms", WMSInfo.class, WMSInfoImpl.class);
    }

    @Override
    protected WMSInfo initialize(WMSInfo service) {
        super.initialize(service);

        if (service.getVersions().isEmpty()) {
            service.getVersions().add(new Version("1.1.1"));
        }
        if (service.getSRS() == null) {
            ((WMSInfoImpl) service).setSRS(new ArrayList<String>());
        }
        return service;
    }
}
