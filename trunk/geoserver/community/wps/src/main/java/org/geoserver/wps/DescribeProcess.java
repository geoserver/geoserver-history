/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 * @author lreed@refractions.net
 */

package org.geoserver.wps;

import net.opengis.wps.DescribeProcessType;

public class DescribeProcess
{
    public WPS wps;

    public DescribeProcess(WPS wps)
    {
        this.wps  = wps;
    }

    public DescribeProcessTransformer run(DescribeProcessType request)
    {
        DescribeProcessTransformer describeProcessTransformer = new DescribeProcessTransformer.WPS1_0(this.wps);

        describeProcessTransformer.setEncoding(this.wps.getCharSet());

        return describeProcessTransformer;
    }
}