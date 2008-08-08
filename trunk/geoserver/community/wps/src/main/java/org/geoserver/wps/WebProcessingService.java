/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 *  @author Lucas Reed, Refractions Research, lreed@refractions.net
 */

package org.geoserver.wps;

import net.opengis.wps.GetCapabilitiesType;
import net.opengis.wps.DescribeProcessType;
import net.opengis.wps.ExecuteType;
import net.opengis.wps.RequestBaseType;

import org.geoserver.wps.WPSException;
import org.geotools.xml.transform.TransformerBase;

public interface WebProcessingService
{
    TransformerBase getCapabilities(GetCapabilitiesType request) throws WPSException;

    TransformerBase describeProcess(DescribeProcessType request) throws WPSException;

    TransformerBase execute(ExecuteType request) throws WPSException;

    void getSchema(RequestBaseType request) throws WPSException;
}
