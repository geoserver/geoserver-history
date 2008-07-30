/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import net.opengis.wps.DescribeProcessType;

/**
 * First-call DescribeProcess class
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public class DescribeProcess {
    public WPSInfo wps;

    public DescribeProcess(WPSInfo wps) {
        this.wps = wps;
    }

    public DescribeProcessTransformer run(DescribeProcessType request) {
        DescribeProcessTransformer transformer = new DescribeProcessTransformer.WPS1_0(this.wps);

        return transformer;
    }
}