package org.geoserver.ows.http;

import org.geoserver.ows.http.KVPParser;

public class MessageKvpParser extends KVPParser {

	public MessageKvpParser() {
		super("message", Message.class);
	}

	public Object parse(String value) throws Exception {
		return new Message( value );
	}
}
