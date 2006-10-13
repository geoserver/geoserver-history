package org.geoserver.ows.http;

import org.geoserver.ows.http.KvpParser;

public class MessageKvpParser extends KvpParser {

	public MessageKvpParser() {
		super("message", Message.class);
	}

	public Object parse(String value) throws Exception {
		return new Message( value );
	}
}
