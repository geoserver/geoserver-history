package org.geoserver.ows.http;

import java.io.IOException;
import java.io.InputStream;

public class HelloWorldWithInput extends HelloWorld {

	public Message hello( InputStream input ) throws IOException {
		byte[] buf = new byte[ 255 ];
		int n = input.read( buf );
		
		return new Message( new String( buf, 0, n ) );
	}
}
