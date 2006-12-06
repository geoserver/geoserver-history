package org.geoserver.ows.http;

public class HelloWorld {

	Message message;
	
	public void setMessage( Message message ) {
		this.message = message;
	}
	
	public Message hello() {
		return message;
	}
	
	public Message hello ( Message message ) {
		return message;
	}
	
}
