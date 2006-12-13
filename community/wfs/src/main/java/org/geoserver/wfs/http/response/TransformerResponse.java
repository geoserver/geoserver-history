package org.geoserver.wfs.http.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.TransformerException;

import net.opengis.wfs.GetCapabilitiesType;

import org.geoserver.ows.Operation;
import org.geoserver.ows.http.OWSUtils;
import org.geoserver.ows.http.Response;
import org.geotools.xml.transform.TransformerBase;

public class TransformerResponse extends Response {

	public TransformerResponse() {
		super( TransformerBase.class );
	}

	public String getMimeType(Operation operation) {
		GetCapabilitiesType request = 
			(GetCapabilitiesType) OWSUtils.parameter( operation.getParameters(), GetCapabilitiesType.class );
		
		if ( request != null && request.getAcceptFormats() != null ) {
			//look for an accepted format
			List formats = request.getAcceptFormats().getOutputFormat();
			for ( Iterator f = formats.iterator(); f.hasNext(); ) {
				String format = (String) f.next();
				if ( format.endsWith( "/xml" ) ) {
					return format;
				}
			}
		}
		
		//default
		return "application/xml";
	}
	
	public void write( Object value, OutputStream output, Operation operation )
			throws IOException {
		
		TransformerBase tx = (TransformerBase) value;
		try {
			tx.transform( this, output );
		} 
		catch (TransformerException e) {
			throw (IOException) new IOException().initCause( e );
		}
	}

	public void abort(Object value, OutputStream output, Operation operation)
			throws IOException {
		
		//do nothing
	}

}
