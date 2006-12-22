package org.geoserver.ows;

import java.util.ArrayList;
import java.util.List;

/**
 *  A standard OGC service exception.
 *  
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 */
public class ServiceException extends Exception {

	/**
	 * Application specfic code.
	 */
	String code;
	/**
	 * Application specific locator
	 */
	String locator;
	/**
	 * List of text recording information about the exception
	 */
	List exceptionText = new ArrayList();
	
	public ServiceException( String message ) {
		super( message );
	}
	
	public ServiceException( String message, Throwable cause ) {
		super( message, cause );
	}

	public ServiceException( String message, Throwable cause, String code  ) {
		this( message, cause );
		this.code = code;
	}

	public ServiceException( String message, Throwable cause, String code, String locator ) {
		this( message, cause, code );
		this.locator = locator;
	}

	public ServiceException( String message, String code ) {
		super( message );
		this.code = code;
	}
	
	public ServiceException( String message, String code, String locator ) {
		this( message, code );
		this.locator = locator;
	}

	public ServiceException( Throwable cause ) {
		super( cause );
	}
	
	public ServiceException( Throwable cause, String code ) {
		this( cause );
		this.code = code;
	}
	
	public ServiceException( Throwable cause, String code, String locator ) {
		this( cause, code );
		this.locator = locator;
	}
	
	/**
	 * @return The application specifc code of the exception.
	 */
	public String getCode() {
		return code;
	}
	
	/**
	 * Sets the code for the exception.
	 * 
	 * @param code The application specific code.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	/**
	 * @return The application specific locator. 
	 */
	public String getLocator() {
		return locator;
	}
	
	/**
	 * Sets the locator for the exception.
	 * 
	 * @return The application specific locator.
	 */
	public void setLocator(String locator) {
		this.locator = locator;
	}
	
	/**
	 * @return A list of String recording information about the exception.
	 */
	public List getExceptionText() {
		return exceptionText;
	}
}
