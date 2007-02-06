package org.geoserver.ows;

import org.geoserver.ows.KvpParser;

public class MessageKvpParser extends KvpParser {

	public MessageKvpParser() {
		super("message", Message.class);
	}

	public Object parse(String value) throws Exception {
		return new Message( value );
	}
}
