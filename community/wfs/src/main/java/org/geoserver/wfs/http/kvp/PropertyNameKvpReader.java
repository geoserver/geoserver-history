package org.geoserver.wfs.http.kvp;

import java.util.List;

import org.geoserver.data.GeoServerCatalog;
import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;

/**
 * Parses propertyName=... into a list of qualified names.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class PropertyNameKvpReader extends KvpParser {

	public PropertyNameKvpReader() {
		super( "propertyName", List.class );
	}
	
	public Object parse(String value) throws Exception {
		return KvpUtils.readNested( value );
	}
	
}
