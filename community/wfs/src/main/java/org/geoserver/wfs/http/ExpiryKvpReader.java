package org.geoserver.wfs.http;

import java.math.BigInteger;

import org.geoserver.ows.http.KvpReader;

public class ExpiryKvpReader extends KvpReader {

	public ExpiryKvpReader() {
		super( "expiry", BigInteger.class );
	}
	
	public Object parse( String value ) throws Exception {
		return new BigInteger( value );
	}

}
