package org.geoserver.ows;

import java.io.IOException;
import java.io.OutputStream;

public class HelloWorldWithOutput extends HelloWorld {

	public void hello( Message message, OutputStream output ) throws IOException {
		output.write( message.message.getBytes() );
	}
}
