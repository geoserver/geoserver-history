/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import org.vfny.geoserver.global.GeoValidator;
import org.vfny.geoserver.global.ConfigurationException;

/**
 * WPS state class
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public class WPS extends org.vfny.geoserver.global.Service {
    public static final String WEB_CONTAINER_KEY = "WPS";

    private GeoValidator geoValidator;

    public WPS(org.geoserver.config.GeoServer geoServer) throws ConfigurationException {
        super(geoServer.getService(WPSInfo.class), geoServer);
    }

    public void setValidation(GeoValidator validator) {
        this.geoValidator = validator;
    }

    public GeoValidator getValidation() {
        return this.geoValidator;
    }
}
