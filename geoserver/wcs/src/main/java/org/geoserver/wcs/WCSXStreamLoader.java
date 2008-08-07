package org.geoserver.wcs;

import org.geoserver.config.GeoServer;
import org.geoserver.config.util.XStreamServiceLoader;
import org.geoserver.platform.GeoServerResourceLoader;

/**
 * Loads and persist the {@link WCSInfo} object to and from xstream 
 * persistence.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WCSXStreamLoader extends XStreamServiceLoader<WCSInfo> {

    
    public WCSXStreamLoader(GeoServerResourceLoader resourceLoader) {
        super(resourceLoader, "wcs");
        
    }

    public Class<WCSInfo> getServiceClass() {
        return WCSInfo.class;
    }
    
    protected WCSInfo createServiceFromScratch(GeoServer gs) {
        
        WCSInfoImpl wcs = new WCSInfoImpl();
        wcs.setId( "wcs" );
        
        return wcs;
    }


}
