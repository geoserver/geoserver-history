package org.geoserver.wms;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.util.requests.CapabilitiesRequest;
import org.vfny.geoserver.wms.requests.GetMapRequest;
import org.vfny.geoserver.wms.responses.GetMapResponse;
import org.vfny.geoserver.wms.responses.WMSCapabilitiesResponse;
import org.vfny.geoserver.wms.servlets.Capabilities;
import org.vfny.geoserver.wms.servlets.GetMap;

public class DefaultWebMapService implements WebMapService, ApplicationContextAware {

	/**
	 * WMS Configuration
	 */
	WMS wms;
	/**
	 * Application context
	 */
	ApplicationContext context;
	
	public DefaultWebMapService( WMS wms ) {
		this.wms = wms;
	}
	
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	public WMSCapabilitiesResponse getCapabilities(CapabilitiesRequest request) {
		Capabilities capabilities = 
			(Capabilities) context.getBeansOfType( Capabilities.class ).values().iterator().next();
		return (WMSCapabilitiesResponse) capabilities.getResponse();
	}
	
	public GetMapResponse getMap(GetMapRequest request) {
		GetMap getMap = 
			(GetMap) context.getBeansOfType( GetMap.class ).values().iterator().next();
		return (GetMapResponse) getMap.getResponse();
	}

}
