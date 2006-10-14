package org.geoserver.wfs.http.kvp;

import java.util.List;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpParser;

/**
 * Reads kvp of the form 'featureid=fid1,fid2,...' into a list of strings.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class FeatureIdKvpReader extends KvpParser {

	public FeatureIdKvpReader() {
		super( "featureid", List.class );
	}

	public Object parse( String value ) throws Exception {
		return KvpUtils.readNested( value );
	}

}
