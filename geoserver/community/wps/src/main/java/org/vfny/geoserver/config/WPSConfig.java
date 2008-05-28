/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.vfny.geoserver.config;

import org.geoserver.wps.WPS;
import org.vfny.geoserver.global.dto.WPSDTO;

public class WPSConfig extends ServiceConfig
{
    public static final String CONFIG_KEY = "Config.WPS";

    public WPSConfig()
    {
        super();
    }

    public WPSConfig(WPS wps)
    {
        this((WPSDTO)wps.toDTO());
        }

    public WPSConfig(WPSDTO w)
    {
        super(w.getService());
    }
}