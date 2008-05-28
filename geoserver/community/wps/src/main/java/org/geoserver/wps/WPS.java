/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
    @author lreed@refractions.net
*/

package org.geoserver.wps;

import org.vfny.geoserver.global.Config;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.global.GeoValidator;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WPSDTO;

public class WPS extends org.vfny.geoserver.global.Service
{
    public static final String WEB_CONTAINER_KEY = "WPS";

    private GeoValidator geoValidator;

    public WPS()
    {
        super(new ServiceDTO());
        this.setId("wps");
    }

    public WPS(WPSDTO config)
    {
        super(config.getService());
        this.setId("wps");
    }

    public WPS(Config config, Data data, GeoServer geoServer, GeoValidator validator) throws ConfigurationException
    {
        this(config.getWps());
        this.setData(data);
        this.setGeoServer(geoServer);
        this.setValidation(validator);
    }

    public Object toDTO()
    {
        WPSDTO dto = new WPSDTO();
        dto.setService((ServiceDTO) super.toDTO());

        return dto;
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
