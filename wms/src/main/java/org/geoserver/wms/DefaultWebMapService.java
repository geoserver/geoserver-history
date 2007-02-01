package org.geoserver.wms;

import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;
import org.vfny.geoserver.wms.servlets.Capabilities;

public class DefaultWebMapService implements WebMapService {

	/**
	 * WMS Configuration
	 */
	WMS wms;
	
	public DefaultWebMapService( WMS wms ) {
		this.wms = wms;
	}
	
	public WMSCapabilitiesResponse getCapabilities(CapabilitiesRequest request) {
		return (WMSCapabilitiesResponse) new Capabilities( wms ).getResponse();
	}

}
