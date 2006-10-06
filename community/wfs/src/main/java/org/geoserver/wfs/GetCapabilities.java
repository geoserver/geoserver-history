package org.geoserver.wfs;

import org.geoserver.data.GeoServerCatalog;

/**
 * Web Feature Service GetCapabilities operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GetCapabilities {

	/**
	 * WFS service configuration
	 */
	WFS wfs;
	/**
	 * The catalog
	 */
	GeoServerCatalog catalog;

	public GetCapabilities( WFS wfs, GeoServerCatalog catalog ) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	
	public WFSCapsTransformer getCapabilities() {
		WFSCapsTransformer transformer = new WFSCapsTransformer( wfs, catalog );
        transformer.setIndentation( 2 );

        return transformer;
	}
}
