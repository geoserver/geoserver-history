package org.geoserver.wfs.http.kvp;

import java.util.List;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;

/**
 * Parses kvp of form "...typeNames=
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class TypeNamesKvpReader extends KvpParser {

	public TypeNamesKvpReader() {
		super( "typeNames", List.class );
	}

	public Object parse(String value) throws Exception {
		return KvpUtils.readFlat( value, "," );
	}
}
