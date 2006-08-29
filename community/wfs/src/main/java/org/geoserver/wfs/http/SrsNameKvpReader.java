package org.geoserver.wfs.http;

import java.util.List;

import org.geoserver.http.util.KvpUtils;
import org.geoserver.ows.http.KvpReader;

/**
 * Reads key value pairing of form 'srsName=srsName1,srsName2...' into a list of strings.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public class SrsNameKvpReader extends KvpReader {

	public SrsNameKvpReader() {
		super( "srsName", List.class );
	}
	
	public Object parse( String value ) throws Exception {
		return KvpUtils.readFlat( value, KvpUtils.INNER_DELIMETER );
	}

}
