package org.geoserver.ows;

import java.io.InputStream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.util.Version;
import org.w3c.dom.Document;

public class MessageXmlParser extends XmlRequestReader {

	public MessageXmlParser() {
		super(new QName( null, "Hello"), new Version( "1.0.0" ) );
	}

	public Object read(InputStream input) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			.newDocumentBuilder();
		
		Document doc = builder.parse( input );
		String message = doc.getDocumentElement().getAttribute( "message" );
			
		
		return new Message( message );
	}

}
