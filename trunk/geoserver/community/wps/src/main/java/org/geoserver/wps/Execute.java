/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 *	@author lreed@refractions.net
 */

package org.geoserver.wps;

import net.opengis.wps.ExecuteType;

public class Execute
{
    public WPS  wps;

    public Execute(WPS wps)
    {
        this.wps  = wps;

        return;
    }

    public ExecuteTransformer run(ExecuteType request) throws WPSException
    {
        ExecuteTransformer executeTransformer = new ExecuteTransformer.WPS1_0(this.wps);

        executeTransformer.setEncoding(this.wps.getCharSet());

        return executeTransformer;
    }
}