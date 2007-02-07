package org.geoserver.ows;

import java.io.Reader;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.util.Version;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class MessageXmlParser extends XmlRequestReader {

	public MessageXmlParser() {
		super(new QName( null, "Hello"), new Version( "1.0.0" ) );
	}

	public Object read(Reader reader) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
			.newDocumentBuilder();
		
		Document doc = builder.parse( new InputSource( reader ) );
		String message = doc.getDocumentElement().getAttribute( "message" );
			
		
		return new Message( message );
	}

}
