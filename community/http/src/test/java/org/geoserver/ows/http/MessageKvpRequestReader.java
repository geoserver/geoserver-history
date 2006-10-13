package org.geoserver.ows.http;

public class MessageKvpRequestReader extends KvpRequestReader {

	public MessageKvpRequestReader( ) {
		super( Message.class );
	}

	public Object createRequest() {
		return new Message();
	}

}
