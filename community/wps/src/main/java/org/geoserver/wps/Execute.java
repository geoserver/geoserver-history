/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
    @author lreed@refractions.net
*/

package org.geoserver.wps;

import net.opengis.wps.ExecuteType;
import org.vfny.geoserver.global.Data;

public class Execute
{
    public WPS  wps;
    public Data data;

    public Execute(WPS wps, Data data)
    {
        this.wps  = wps;
        this.data = data;

        return;
    }

    public ExecuteTransformer run(ExecuteType request) throws WPSException
    {
        ExecuteTransformer executeTransformer = new ExecuteTransformer.WPS1_0(this.wps, this.data);

        executeTransformer.setEncoding(this.wps.getCharSet());

        return executeTransformer;
    }
}