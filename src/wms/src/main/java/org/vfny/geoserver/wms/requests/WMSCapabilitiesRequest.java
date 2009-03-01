/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.geoserver.wms.WMS;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;

/**
 * Subclass of {@link CapabilitiesRequest} for Web Map Service.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * 
 */
public class WMSCapabilitiesRequest extends CapabilitiesRequest {

    private WMS config;

    public WMSCapabilitiesRequest(WMS config) {
        super("WMS", config.getServiceInfo());
        this.config = config;
    }

    public WMS getWMS() {
        return config;
    }
}
