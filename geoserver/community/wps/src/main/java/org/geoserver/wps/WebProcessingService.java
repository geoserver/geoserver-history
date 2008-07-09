/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import java.io.OutputStream;

import net.opengis.wps.ExecuteType;
import net.opengis.wps.GetCapabilitiesType;
import net.opengis.wps.DescribeProcessType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geoserver.wps.WPSException;
import org.geotools.xml.transform.TransformerBase;

/**
 * @author Lucas Reed, Refractions Research Inc
 */
public interface WebProcessingService {
    TransformerBase getCapabilities(GetCapabilitiesType request) throws WPSException;

    TransformerBase describeProcess(DescribeProcessType request) throws WPSException;

    void execute(ExecuteType request, HttpServletResponse response) throws WPSException;

    void getSchema(HttpServletRequest request, HttpServletResponse response) throws WPSException;
}
