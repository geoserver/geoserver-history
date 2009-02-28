/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs.requests;

import org.geoserver.config.GeoServer;
import org.geoserver.wcs.WCSInfo;
import org.vfny.geoserver.Request;


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
    public WCSRequest(String requestType, WCSInfo service) {
        super(WCS_SERVICE_TYPE, requestType, service);
    }

    /**
     * Convenience method for obtaining the global wcs service instance.
     */
    public WCSInfo getWCS() {
        return (WCSInfo) serviceConfig;
    }

    /**
     * Convenience method for obtaining the global geoserver instance.
     */
    public GeoServer getGeoServer() {
        return getWCS().getGeoServer();
    }
}
