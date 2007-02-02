package org.geoserver.wms;

import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;

/**
 * Web Map Service implementations.
 * <p>
 * Each of the methods on this class corresponds to an operation as defined
 * by the Web Map Specification. See {@link http://www.opengeospatial.org/standards/wms}
 * for more details.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public interface WebMapService {

	/**
	 * GetCapabilities operation.
	 */
	WMSCapabilitiesResponse getCapabilities( CapabilitiesRequest request );
	
	/**
	 * GetMap operation.
	 */
	GetMapResponse getMap( GetMapRequest request );
}
