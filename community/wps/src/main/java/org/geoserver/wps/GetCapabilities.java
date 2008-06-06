/**
    @author lreed@refractions.net
*/

package org.geoserver.wps;

import org.geoserver.wps.CapabilitiesTransformer;
import net.opengis.wps.GetCapabilitiesType;

public class GetCapabilities
{
    public WPS  wps;

    public GetCapabilities(WPS wps)
    {
        this.wps  = wps;

        return;
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
