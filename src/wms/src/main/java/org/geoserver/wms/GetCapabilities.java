package org.geoserver.wms;

import org.geoserver.platform.ServiceException;
import org.geoserver.wms.request.GetCapabilitiesRequest;

/**
 * WMS GetCapabilities operation.
 * 
 * @author groldan
 * 
 */
public interface GetCapabilities {

    public GetCapabilitiesTransformer run(final GetCapabilitiesRequest request) throws ServiceException;
}
