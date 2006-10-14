package org.geoserver.wfs.http.kvp;

import java.util.Map;

import org.geoserver.ows.http.KvpRequestReader;

public class ReleaseLockKvpRequestReader extends KvpRequestReader {

	public ReleaseLockKvpRequestReader( ) {
		super( String.class );
	}
	
	public Object createRequest() throws Exception {
		return new String();
	}
	
	public Object read(Object request, Map kvp) throws Exception {
		return kvp.get( "lockId" );
	}

}
