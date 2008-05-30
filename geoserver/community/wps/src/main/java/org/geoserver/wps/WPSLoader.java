/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.XStreamServiceLoader;
import org.geoserver.platform.GeoServerResourceLoader;

/**
 * Service loader for the Web Processing Service.
 * 
 * @author Lucas Reed, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WPSLoader extends XStreamServiceLoader
{
    public WPSLoader(GeoServerResourceLoader resourceLoader) {
        super(resourceLoader);
    }

    public String getServiceId() {
        return "wps";
    }
    
    protected ServiceInfo createServiceFromScratch(GeoServer gs) {
        WPSInfoImpl wps = new WPSInfoImpl();
        wps.setId( getServiceId() );
        
        return wps;
    }
}