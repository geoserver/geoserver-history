package org.geoserver.wfs.http;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.geoserver.ows.http.KVPParser;

/**
 * Parses kvp of form "...typeNames=
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class RequestedTypesKvpReader extends KVPParser {

	public RequestedTypesKvpReader() {
		super( "typeNames", List.class );
	}

	public Object parse(String value) throws Exception {
		List types = new ArrayList();
		
		StringTokenizer st = new StringTokenizer( value, "," );
		while ( st.hasMoreElements() ) types.add( st.nextToken() );

		return types;
	}
}
