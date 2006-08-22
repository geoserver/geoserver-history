package org.geoserver.wfs.http;

import java.math.BigInteger;

import org.geoserver.ows.http.KvpReader;

public class MaxFeaturesKvpReader extends KvpReader {

	public MaxFeaturesKvpReader() {
		super( "maxFeatures", BigInteger.class );
	}
	
	public Object parse( String value ) throws Exception {
		return new BigInteger( value );
	}

}
