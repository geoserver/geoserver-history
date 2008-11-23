package org.geoserver.wms;

import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamServiceLoader;
import org.geoserver.platform.GeoServerResourceLoader;

/**
 * Loads and persist the {@link WMSInfo} object to and from xstream 
 * persistence.
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
}
