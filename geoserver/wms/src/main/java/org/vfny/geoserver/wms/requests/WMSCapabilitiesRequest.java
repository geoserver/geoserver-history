package org.vfny.geoserver.wms.requests;

import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;

/**
 * Subclass of {@link CapabilitiesRequest} for Web Map Service.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WMSCapabilitiesRequest extends CapabilitiesRequest {

	public WMSCapabilitiesRequest(AbstractService service) {
		super("WMS", service);
	}

}
