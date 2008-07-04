/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import net.opengis.wps.DescribeProcessType;

/**
 * @author Lucas Reed, Refractions Research Inc
 */
public class DescribeProcess
{
    public WPS wps;

    public DescribeProcess(WPS wps)
    {
        this.wps = wps;
    }

    public DescribeProcessTransformer run(DescribeProcessType request)
    {
        DescribeProcessTransformer transformer = new DescribeProcessTransformer.WPS1_0(this.wps);

        transformer.setEncoding(this.wps.getCharSet());

        return transformer;
    }
}