package org.geoserver.wfs;

import java.io.OutputStream;

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
	
	OutputStream outputStream;
	
	public GetCapabilities( WFS wfs, GeoServerCatalog catalog) {
		this.wfs = wfs;
		this.catalog = catalog;
	}
	
	public void setOutputStream( OutputStream outputStream ) {
		this.outputStream = outputStream;
	}
	
	public void getCapabilities() throws Exception {
		WFSCapsTransformer transformer = new WFSCapsTransformer( wfs, catalog );
        transformer.setIndentation(2);

        transformer.transform( this, outputStream );
    }

}
