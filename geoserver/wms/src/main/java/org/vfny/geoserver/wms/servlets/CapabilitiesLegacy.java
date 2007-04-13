package org.vfny.geoserver.wms.servlets;

import org.vfny.geoserver.global.WMS;

/**
 * Service handling capabilities request of the form "request=capabilities" 
 * instead of "request=GetCapabilities".
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class CapabilitiesLegacy extends Capabilities {

    public CapabilitiesLegacy(WMS wms) {
        super("capabilities", wms);
        
    }

}
