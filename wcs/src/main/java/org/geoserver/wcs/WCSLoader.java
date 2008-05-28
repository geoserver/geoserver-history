package org.geoserver.wcs;

import java.util.Map;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.LegacyServicesReader;
import org.geoserver.config.util.ServiceLoader;

/**
 * Configuration loader for Web Coverage Service.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WCSLoader extends ServiceLoader {

    public ServiceInfo load(LegacyServicesReader reader, GeoServer gs) throws Exception {
        
        WCSInfo wcs = new WCSInfoImpl();
        
        Map<String,Object> map = reader.wcs();
        load( wcs, map, gs );
        
        //wcs.setGMLPrefixing((Boolean)map.get( "gmlPrefixing"));
        
        return wcs;
    }

}
