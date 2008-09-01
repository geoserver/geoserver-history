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

import org.geotools.xml.transform.TransformerBase;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Default Web Processing Service class
 *
 * @author Lucas Reed, Refractions Research Inc
 */
public class DefaultWebProcessingService implements WebProcessingService, ApplicationContextAware {
    protected WPSInfo  wps;

    protected ApplicationContext context;

    public DefaultWebProcessingService(WPSInfo wps) {
        this.wps = wps;
    }

    /**
     * @see org.geoserver.wps.WebProcessingService#getCapabilities
     */
    public TransformerBase getCapabilities(GetCapabilitiesType request) throws WPSException {
        return new GetCapabilities(this.wps).run(request);
    }

    /**
     * @see org.geoserver.wps.WebProcessingService#describeProcess
     */
    public TransformerBase describeProcess(DescribeProcessType request) throws WPSException {
        return new DescribeProcess(this.wps).run(request);
    }

    /**
     * @see org.geoserver.wps.WebProcessingService#execute
     */
    public void execute(ExecuteType request, HttpServletResponse response) throws WPSException {
        new Execute.WPS1_0(this.wps).run(request, response);
    }

    /**
     * @see org.geoserver.wps.WebProcessingService#getSchema
     */
    public void getSchema(HttpServletRequest request, HttpServletResponse response)
        throws WPSException {
        new GetSchema(this.wps).run(request, response);
    }

    /**
     * @see org.springframework.context.ApplicationContextAware#setApplicationContext
     */
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
