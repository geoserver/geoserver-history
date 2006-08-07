package org.geoserver.ows.http;

import org.geoserver.ows.http.KvpReader;

public class MessageKvpParser extends KvpReader {

	public MessageKvpParser() {
		super("message", Message.class);
	}

	public Object parse(String value) throws Exception {
		return new Message( value );
	}
}
