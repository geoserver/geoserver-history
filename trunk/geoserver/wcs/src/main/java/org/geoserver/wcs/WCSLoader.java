package org.geoserver.wcs;

import java.util.Map;

import org.geoserver.config.GeoServer;
import org.geoserver.config.util.LegacyServiceLoader;
import org.geoserver.config.util.LegacyServicesReader;

/**
 * Configuration loader for Web Coverage Service.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class WCSLoader extends LegacyServiceLoader<WCSInfo> {

    public Class<WCSInfo> getServiceClass() {
        return WCSInfo.class;
    }
    
    public WCSInfo load(LegacyServicesReader reader, GeoServer gs) throws Exception {
        
        WCSInfoImpl wcs = new WCSInfoImpl();
        wcs.setId( "wcs" );
        
        Map<String,Object> map = reader.wcs();
        readCommon( wcs, map, gs );
        
        //wcs.setGMLPrefixing((Boolean)map.get( "gmlPrefixing"));
        
        return wcs;
    }

    public void save(WCSInfo service, GeoServer gs) throws Exception {
    }
}
