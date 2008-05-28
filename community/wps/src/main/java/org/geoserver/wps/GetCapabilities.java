/**
    @author lreed@refractions.net
*/

package org.geoserver.wps;

import org.geoserver.wps.CapabilitiesTransformer;
import net.opengis.wps.GetCapabilitiesType;
import org.vfny.geoserver.global.Data;

public class GetCapabilities
{
    public WPS  wps;
    public Data data;

    public GetCapabilities(WPS wps, Data data)
    {
        this.wps  = wps;
        this.data = data;

        return;
    }

    public CapabilitiesTransformer run(GetCapabilitiesType request) throws WPSException
    {
        // Version detection and alternative invocation if being implemented.
        // Build XML document.

        CapabilitiesTransformer capabilitiesTransformer = new CapabilitiesTransformer.WPS1_0(this.wps, this.data);

        capabilitiesTransformer.setEncoding(this.wps.getCharSet());

        return capabilitiesTransformer;
    }
}
