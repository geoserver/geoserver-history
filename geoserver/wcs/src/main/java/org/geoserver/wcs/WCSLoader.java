package org.geoserver.wcs;

import java.util.Map;

import org.geoserver.config.GeoServer;
import org.geoserver.config.ServiceInfo;
import org.geoserver.config.util.LegacyServiceLoader;
import org.geoserver.config.util.LegacyServicesReader;

/**
 * Configuration loader for Web Coverage Service.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WCSLoader extends LegacyServiceLoader {

    public String getServiceId() {
        return "wcs";
    }
    
    public ServiceInfo load(LegacyServicesReader reader, GeoServer gs) throws Exception {
        
        WCSInfoImpl wcs = new WCSInfoImpl();
        wcs.setId( getServiceId() );
        
        Map<String,Object> map = reader.wcs();
        readCommon( wcs, map, gs );
        
        //wcs.setGMLPrefixing((Boolean)map.get( "gmlPrefixing"));
        
        return wcs;
    }

}
