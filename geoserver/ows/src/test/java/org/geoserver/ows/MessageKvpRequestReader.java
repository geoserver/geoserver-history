package org.geoserver.ows;

public class MessageKvpRequestReader extends KvpRequestReader {

	public MessageKvpRequestReader( ) {
		super( Message.class );
	}

	public Object createRequest() {
		return new Message();
	}

}
