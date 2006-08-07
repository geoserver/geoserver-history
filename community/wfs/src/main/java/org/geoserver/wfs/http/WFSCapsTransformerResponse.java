package org.geoserver.wfs.http;

import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.TransformerException;

import org.geoserver.ows.http.Response;
import org.geoserver.wfs.WFSCapsTransformer;

public class WFSCapsTransformerResponse extends Response {

	public WFSCapsTransformerResponse() {
		super( "text/xml", WFSCapsTransformer.class );
	}

	public void write(Object value, OutputStream output, Object operation)
			throws IOException {
		
		WFSCapsTransformer tx = (WFSCapsTransformer) value;
		try {
			tx.transform( this, output );
		} 
		catch (TransformerException e) {
			throw (IOException) new IOException().initCause( e );
		}
	}

	public void abort(Object value, OutputStream output, Object operation)
			throws IOException {
		
		//do nothing
	}

}
