package org.geoserver.ows.http;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.geoserver.ows.Operation;
import org.geoserver.ows.Service;
import org.geoserver.ows.ServiceException;

/**
 * Handles a service exception.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 */
public abstract class ServiceExceptionHandler {

	/**
	 * The services this handler handles exceptions for.
	 */
	List/*<Service>*/ services;
	
	public ServiceExceptionHandler( List services ) {
		this.services = services;
	}
	
	/**
	 * @return The services this handler handles exceptions for.
	 */
	public List getServices() {
		return services;
	}
	
	/**
	 * Handles the service exception.
	 * 
	 * @param exception The service exception.
	 * @param operation The operation that generated the exception.
	 * @param response The response to report the exception to.
	 */
	public abstract void handleServiceException( 
		ServiceException exception, Operation operation, HttpServletResponse response
	);
	
}
