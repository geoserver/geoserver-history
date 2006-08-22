package org.geoserver.wfs.v1_1_0;

import net.opengis.wfs.v1_1_0.GetCapabilitiesType;
import net.opengis.wfs.v1_1_0.WFSFactory;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.wfs.WFS;

/**
 * WFS 1.1 GetCapabilities operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GetCapabilities {

	/**
	 * WFS configuration
	 */
	WFS wfs;
	/**
	 * GeoServer catalog
	 */
	GeoServerCatalog catalog;
	/**
	 * The request
	 */
	GetCapabilitiesType request;
	
	public GetCapabilities( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	 
	public WFSCapabilitiesTransformer getCapabilities() {
		return getCapabilities( request() );
	}
	
	public WFSCapabilitiesTransformer getCapabilities( GetCapabilitiesType request ) {
		return new WFSCapabilitiesTransformer( wfs, catalog );
	}
	
	GetCapabilitiesType request() {
		if ( request == null ) {
			request = WFSFactory.eINSTANCE.createGetCapabilitiesType();
		}
		
		return request;
	}
}
