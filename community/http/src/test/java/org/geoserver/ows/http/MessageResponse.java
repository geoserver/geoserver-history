package org.geoserver.ows.http;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.ows.Operation;

public class MessageResponse extends Response {

	public MessageResponse() {
		super( "text/plain", Message.class );
	}

	public void write(Object value, OutputStream output, Operation operation)
			throws IOException {
		
		Message message = (Message) value;
		output.write( message.message.getBytes() );

	}

	public void abort(Object value, OutputStream output, Operation operation)
			throws IOException {
	
	}

}
