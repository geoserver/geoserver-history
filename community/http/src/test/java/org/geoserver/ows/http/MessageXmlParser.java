package org.geoserver.ows.http;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geoserver.ows.http.XmlReader;
import org.w3c.dom.Document;

public class MessageXmlParser extends XmlReader {

	public MessageXmlParser() {
		super(null, "Hello", null);
	}

	public Object parse(InputStream input) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			.newDocumentBuilder();
		
		Document doc = builder.parse( input );
		String message = doc.getDocumentElement().getAttribute( "message" );
			
		
		return new Message( message );
	}

}
