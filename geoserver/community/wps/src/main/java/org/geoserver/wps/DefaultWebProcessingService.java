/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
 *  @author lreed@refractions.net
 */

package org.geoserver.wps;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.opengis.wps.GetCapabilitiesType;
import net.opengis.wps.DescribeProcessType;
import net.opengis.wps.ExecuteType;

import org.geotools.xml.transform.TransformerBase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultWebProcessingService implements WebProcessingService, ApplicationContextAware
{
    protected WPS  wps;

    protected ApplicationContext context;

    public DefaultWebProcessingService(WPS wps)
    {
        this.wps  = wps;
    }

    public TransformerBase getCapabilities(GetCapabilitiesType request) throws WPSException
    {
        return new GetCapabilities(this.wps).run(request);
    }

    public TransformerBase describeProcess(DescribeProcessType request) throws WPSException
    {
        return new DescribeProcess(this.wps).run(request);
    }

    public void execute(ExecuteType request, OutputStream output) throws WPSException
    {
        new Execute.WPS1_0(this.wps).run(request, output);
    }

    public void getSchema(HttpServletRequest request, HttpServletResponse response) throws WPSException
    {
        new GetSchema(this.wps).run(request, response);
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }
}
