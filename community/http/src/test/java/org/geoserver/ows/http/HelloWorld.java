package org.geoserver.ows.http;

public class HelloWorld {

	Message message;
	
	public void setMessage( Message message ) {
		this.message = message;
	}
	
	public String hello() {
		return message.message;
	}
	
	public String hello ( Message message ) {
		return message.message;
	}
	
}
