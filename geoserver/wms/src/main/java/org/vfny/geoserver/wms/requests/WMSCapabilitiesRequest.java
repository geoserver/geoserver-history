/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms.requests;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;


/**
 * Subclass of {@link CapabilitiesRequest} for Web Map Service.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WMSCapabilitiesRequest extends CapabilitiesRequest {
    
    
    public WMSCapabilitiesRequest(WMS wms) {
        super("WMS",wms);
    }
}
