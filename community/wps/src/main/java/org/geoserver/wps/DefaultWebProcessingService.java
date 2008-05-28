/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

/**
    @author lreed@refractions.net
*/

package org.geoserver.wps;

import net.opengis.wps.GetCapabilitiesType;
import net.opengis.wps.DescribeProcessType;
import net.opengis.wps.ExecuteType;

import org.geotools.xml.transform.TransformerBase;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.global.Data;

public class DefaultWebProcessingService implements WebProcessingService, ApplicationContextAware
{
    protected WPS  wps;
    protected Data data;

    protected ApplicationContext context;

    public DefaultWebProcessingService(WPS wps, Data data)
    {
        this.wps  = wps;
        this.data = data;
    }

    public TransformerBase getCapabilities(GetCapabilitiesType request) throws WPSException
    {
        return new GetCapabilities(this.wps, this.data).run(request);
    }

    public TransformerBase describeProcess(DescribeProcessType request) throws WPSException
    {
        return new DescribeProcess(this.wps, this.data).run(request);
    }

    public TransformerBase execute(ExecuteType request) throws WPSException
    {
        return new Execute(this.wps, this.data).run(request);
    }

    public void setApplicationContext(ApplicationContext context) throws BeansException
    {
        this.context = context;
    }
}
