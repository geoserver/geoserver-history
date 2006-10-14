package org.geoserver.wfs.http.kvp;

import java.math.BigInteger;

import org.geoserver.ows.http.KvpParser;

public class MaxFeaturesKvpReader extends KvpParser {

	public MaxFeaturesKvpReader() {
		super( "maxFeatures", BigInteger.class );
	}
	
	public Object parse( String value ) throws Exception {
		return new BigInteger( value );
	}

}
