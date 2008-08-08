/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests;

import org.vfny.geoserver.Request;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.WCS;
import org.vfny.geoserver.wcs.servlets.WCService;


/**
 * DOCUMENT ME!
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 */
public class WCSRequest extends Request {
    public static final String WCS_SERVICE_TYPE = "WCS";

    /**
     * A WCSRequest configured with WCS_SERVICE_TYPE
     */
    public WCSRequest(String requestType, WCS service) {
        super(WCS_SERVICE_TYPE, requestType, service);
    }

    /**
     * Convenience method for obtaining the global wcs service instance.
     */
    public WCS getWCS() {
        return (WCS) serviceConfig;
    }

    /**
     * Convenience method for obtaining the global geoserver instance.
     */
    public GeoServer getGeoServer() {
        GeoServer gs = getWCS().getGeoServer();

        return gs;
    }
}
