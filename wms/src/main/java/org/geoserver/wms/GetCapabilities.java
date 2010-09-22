package org.geoserver.wms;

import org.geoserver.platform.ServiceException;
import org.vfny.geoserver.wms.requests.WMSCapabilitiesRequest;

/**
 * WMS GetCapabilities operation.
 * 
 * @author groldan
 * 
 */
public interface GetCapabilities {

    public GetCapabilitiesTransformer run(final WMSCapabilitiesRequest request) throws ServiceException;
}
