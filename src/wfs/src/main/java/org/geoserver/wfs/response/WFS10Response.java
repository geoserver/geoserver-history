package org.geoserver.wfs.response;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geotools.util.Version;

/**
 * Base class for WFS 1.0 responses.
 * <p>
 * This base class ensures that the responses only engages on a wfs 1.0.0 request.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class WFS10Response extends Response {

    public WFS10Response(Class binding) {
        super(binding);
    }

    public boolean canHandle(Operation operation) {
        return new Version("1.0.0").equals( operation.getService().getVersion() );
    }

}
