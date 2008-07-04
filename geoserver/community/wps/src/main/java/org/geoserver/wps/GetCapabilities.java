/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.wps;

import net.opengis.wps.GetCapabilitiesType;
import org.geoserver.wps.CapabilitiesTransformer;

/**
 * @author Lucas Reed, Refractions Research Inc
 */
public class GetCapabilities
{
    public WPS wps;

    public GetCapabilities(WPS wps)
    {
        this.wps = wps;
    }

    public CapabilitiesTransformer run(GetCapabilitiesType request) throws WPSException
    {
        // Version detection and alternative invocation if being implemented.
        // Build XML document.

        CapabilitiesTransformer capabilitiesTransformer = new CapabilitiesTransformer.WPS1_0(this.wps);

        capabilitiesTransformer.setEncoding(this.wps.getCharSet());

        return capabilitiesTransformer;
    }
}
