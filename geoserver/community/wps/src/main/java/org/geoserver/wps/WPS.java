/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 *  @author lreed@refractions.net
 */

package org.geoserver.wps;

import java.util.HashMap;
import java.util.Map;

import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.GeoValidator;

public class WPS extends org.vfny.geoserver.global.Service
{
    public static final String WEB_CONTAINER_KEY = "WPS";

    private Map<String, Class>  schemaConfigs = new HashMap<String, Class>();
    private Map<Class,  String> classSchemas  = new HashMap<Class,  String>();

    private GeoValidator geoValidator;

    public WPS(org.geoserver.config.GeoServer geoServer) throws ConfigurationException
    {
        super(geoServer.getService(WPSInfo.class), geoServer);
    }

    public void setValidation(GeoValidator validator)
    {
        this.geoValidator = validator;
    }

    public GeoValidator getValidation()
    {
        return this.geoValidator;
    }
}
