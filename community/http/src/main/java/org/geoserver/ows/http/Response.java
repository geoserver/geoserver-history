package org.geoserver.ows.http;

import java.io.IOException;
import java.io.OutputStream;

import org.geoserver.ows.Operation;
import org.geoserver.ows.ServiceException;

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
	 * Class of object to serialize
	 */
	Class binding;
	
	public Response( Class binding ) {
		this.binding = binding;
	}
	
	/**
	 * The type of object the response can handle.
	 */
	public final Class getBinding() {
		return binding;
	}
	
	/**
	 * Determines if the response can handle the operation being performed.
	 * <p>
	 * This method is called before {@link #write(Object, OutputStream, Operation)}.
	 * </p>
	 * <p>
	 * Subclasses should override this method to perform additional checks 
	 * against the operation being performed, example: version.
	 * </p>
	 * @param operation The operation being performed.
	 * 
	 * @return <code>true</code> if the response can handle the operation, 
	 * otherwise <code>false</code>
	 */
	public boolean canHandle( Operation operation ) {
		return true;
	}

	/**
	 * Returns the mime type to be uses when writing the response.
	 * 
	 * @param operation The operation being performed.
	 * 
	 * @return The mime type of the response.
	 */
	abstract public String getMimeType( Operation operation ) throws ServiceException;
	
	/**
	 * Serializes <code>value</code> to <code>output</code>.
	 * <p>
	 * The <code>operation</code> bean is provided for context.
	 * </p>
	 * @param value The value to serialize.
	 * @param output The output stream.
	 * @param operation The operation which resulted in <code>value</code>
	 * 
	 * @throws IOException Any I/O errors that occur
	 * @throws ServiceException Any service errors that occur
	 */
	abstract public void write( Object value, OutputStream output, Operation operation ) 
		throws IOException, ServiceException;

	public void abort( Object value, OutputStream output, Operation operation ) 
		throws IOException, ServiceException {
		//do nothing
	}
}
