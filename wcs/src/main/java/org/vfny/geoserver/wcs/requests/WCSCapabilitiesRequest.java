package org.vfny.geoserver.wcs.requests;

import org.vfny.geoserver.servlets.AbstractService;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;

/**
 * Subclass of {@link CapabilitiesRequest} for Web Coverage Service.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class WCSCapabilitiesRequest extends CapabilitiesRequest {

	public WCSCapabilitiesRequest(AbstractService service) {
		super("WCS", service);
	}

}
