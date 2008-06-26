package org.geoserver.wfs.response.v1_1_0;

import org.geoserver.ows.Response;
import org.geoserver.platform.Operation;
import org.geoserver.platform.ServiceException;
import org.geotools.util.Version;

/**
 * Base class for wfs 1.1 responses.
 * <p>
 * This class ensures that the responses only engage for wfs 1.1 requests.
 * </p>
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class WFS11Response extends Response {

    public WFS11Response(Class binding) {
        super(binding);
    }
   
    public boolean canHandle(Operation operation) {
        //JD: the check we do here is != 1.0.0. Since the spec states that the
        //highest possible version of the prototocol should be returned in the 
        // event when an invalid version is requested, and wfs 1.0 is the only 
        // other valid version we know of
        // TODO: this will not hold up when new wfs versions are added...
        return !( new Version( "1.0.0" ).equals( operation.getService().getVersion() ) );
    }

    public String getMimeType(Object value, Operation operation)
            throws ServiceException {
        return "text/xml";
    }
}
