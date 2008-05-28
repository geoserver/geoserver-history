/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps;

import net.opengis.wps.DescribeProcessType;

import org.vfny.geoserver.global.Data;

public class DescribeProcess
{
    public WPS  wps;
    public Data data;

    public DescribeProcess(WPS wps, Data data)
    {
        this.wps  = wps;
        this.data = data;
    }

    public DescribeProcessTransformer run(DescribeProcessType request)
    {
        DescribeProcessTransformer describeProcessTransformer = new DescribeProcessTransformer.WPS1_0(this.wps, this.data);

        describeProcessTransformer.setEncoding(this.wps.getCharSet());
        
        return describeProcessTransformer;
    }
}