package org.geoserver.wfs.http.kvp;

import org.geoserver.data.GeoServerCatalog;

/**
 * Parses typeName=... into a list of qualified names.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class TypeNameKvpReader extends QNameKvpParser {

	public TypeNameKvpReader( GeoServerCatalog catalog ) {
		super( "typeName", catalog);
		
	}

}
