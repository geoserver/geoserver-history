/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
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
