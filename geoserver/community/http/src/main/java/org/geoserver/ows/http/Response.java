package org.geoserver.ows.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Response to an operation.
 * <p>
 * Implementations of this interface are responsible for serializing 
 * objects to an output stream.
 * </p>
 * <p>
 * 
 * </p>
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public abstract class Response {

	/**
	 * Mime type of the response.
	 */
	String mimeType;
	/**
	 * Class of object to serialize
	 */
	Class binding;
	
	public Response( String mimeType, Class binding ) {
		this.mimeType = mimeType;
		this.binding = binding;
		
	}
	
	public final String getMimeType() {
		return mimeType;
	}
	
	public final Class getBinding() {
		return binding;
	}
	
	/**
	 * Serializes <code>value</code> to <code>output</code>.
	 * <p>
	 * The <code>operation</code> bean is provided for context.
	 * </p>
	 * @param value The value to serialize.
	 * @param output The output stream.
	 * @param operation The operation which resulted in <code>value</code>
	 * 
	 * @throws IOException
	 */
	abstract public void write( Object value, OutputStream output, Object operation ) 
		throws IOException;

	abstract public void abort( Object value, OutputStream output, Object operation ) 
		throws IOException;
}
