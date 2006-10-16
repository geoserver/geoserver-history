package org.geoserver.wfs.http.kvp;

import org.geoserver.data.GeoServerCatalog;

/**
 * Parses propertyName=... into a list of qualified names.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class PropertyNameKvpReader extends QNameKvpParser {

	public PropertyNameKvpReader( GeoServerCatalog catalog ) {
		super( "propertyName", catalog );
		
	}
}
