/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

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
    /**
     * Generates a XML object for the return of the getCapabilities request
     *
     * @param request
     * @return
     * @throws WPSException
     */
    TransformerBase getCapabilities(GetCapabilitiesType request) throws WPSException;

    /**
     * Generates a XML object for  the return of the describeProcess request
     *
     * @param request
     * @return
     * @throws WPSException
     */
    TransformerBase describeProcess(DescribeProcessType request) throws WPSException;

    /**
     * Executes a execute request and writes output to the Servlet response
     *
     * @param request
     * @param response
     * @throws WPSException
     */
    void execute(ExecuteType request, HttpServletResponse response) throws WPSException;

    /**
     * Executes a get schema request and writes the output to the Servlet response
     *
     * @param request
     * @param response
     * @throws WPSException
     */
    void getSchema(HttpServletRequest request, HttpServletResponse response) throws WPSException;
}
