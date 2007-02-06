package org.geoserver.ows;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.platform.Operation;

public class MessageResponse extends Response {

	public MessageResponse() {
		super( Message.class );
	}

	public String getMimeType(Object value, Operation operation) {
		return "text/plain";
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
