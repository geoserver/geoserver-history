package org.geoserver.ows.http;

import java.io.IOException;
import java.io.OutputStream;

public class MessageResponse extends Response {

	public MessageResponse() {
		super( "text/plain", Message.class );
	}

	public void write(Object value, OutputStream output, Object operation)
			throws IOException {
		
		Message message = (Message) value;
		output.write( message.message.getBytes() );

	}

	public void abort(Object value, OutputStream output, Object operation)
			throws IOException {
	
	}

}
