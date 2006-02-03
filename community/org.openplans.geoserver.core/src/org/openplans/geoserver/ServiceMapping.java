package org.openplans.geoserver;

import java.util.Map;

/**
 * A bean used to map a service identifier to the id of the bean implementing 
 * the service.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class ServiceMapping {

	Map mappings;
	
	public ServiceMapping(Map mappings) {
		this.mappings = mappings;
	}
	
	public Map getMappings() {
		return mappings;
	}
}
