package org.geoserver.wfs;

import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.WFSFactory;

import org.geoserver.data.GeoServerCatalog;

/**
 * Web Feature Service GetCapabilities operation.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class GetCapabilities {

	WFS wfs;
	GeoServerCatalog catalog;

	GetCapabilitiesType request;
	
	public GetCapabilities( WFS wfs, GeoServerCatalog catalog) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	
	GetCapabilitiesType request() {
		if ( request == null ) {
			request = WFSFactory.eINSTANCE.createGetCapabilitiesType();
		}
		
		return request;
	}
	
	public void setVersion( String version ) {
		request().setVersion( version );
	}
	
	public WFSCapsTransformer getCapabilities() {
		return getCapabilities( request() );
	}
	
	public WFSCapsTransformer getCapabilities( GetCapabilitiesType request ) {
		WFSCapsTransformer transformer = new WFSCapsTransformer( wfs, catalog );
        transformer.setIndentation( 2 );

        return transformer;
	}
}
