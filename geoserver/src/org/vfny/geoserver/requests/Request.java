/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Defines a general Request type and provides accessor methods for universal
 * request information.
 * <p>
 * Also provides access to the HttpRequest that spawned this GeoServer Request.
 * This HttpRequest is most often used to lookup information stored in the
 * Web Container (such as the GeoServer Global information).
 * </p>
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldan
 * @version $Id: Request.java,v 1.5.2.3 2004/01/06 08:41:05 jive Exp $
 */
abstract public class Request {
	/**
	 * Servlet Request responsible for generating this GeoServer Request.
	 * <p>
	 * For all current GeoServer requests this may be safely cast to an HttpServletRequest. 
	 * </p>
	 */
	protected ServletRequest servletRequest;
	
    /**
     * Request service
     * <p>
     * @todo Explain! What does this mean? Is it the Name of the Service being requested?
     * Is it look 
     * </p>
     */
    protected String service;

    /** Request type */
    protected String request = new String();

    /** Request version */
    protected String version = new String();

    /**
     * ServiceConfig indentifying constructor.
     *
     * @param serviceType Name of services (like wms)
     */
    protected Request(String serviceType) {
        this.service = serviceType;
    }
    
    /**
     * ServiceConfig & Request indentifying constructor.
     *
     * @param serviceType Name of services (like wfs)
     * @param requestType Name of request (like Transaction)
     */
    protected Request(String serviceType, String requestType) {
        this.service = serviceType;
        this.request = requestType;
    }

     
    /**
     * Gets requested service.
     *
     * @return The requested service.
     */
    public String getService() {
        return this.service;
    }

    /**
     * Gets requested service.
     *
     * @param service The requested service.
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * Gets requested request type.
     * <p>
     * TODO: Could this bre renamed getType() for clarity?
     * </p>
     * 
     * @return The type of request.
     */
    public String getRequest() {
        return this.request;
    }

    /**
     * Sets requested request type.
     *
     * @param reqeust The type of request.
     */
    public void setRequest(String requestType) {
        this.request = requestType;
    }

    /**
     * Return version type.
     *
     * @return The request type version.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * Sets version type.
     *
     * @param version The request type version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Request)) {
            return false;
        }

        Request req = (Request) o;
        boolean equals = true;
        equals = ((request == null) ? (req.getRequest() == null)
                                    : request.equals(req.getRequest()))
            && equals;
        equals = ((version == null) ? (req.getVersion() == null)
                                    : version.equals(req.getVersion()))
            && equals;
        equals = ((service == null) ? (req.getService() == null)
                                    : service.equals(req.getService()))
            && equals;

        return equals;
    }
    /**
     * Generate a hashCode based on this Request Object.
     */
    public int hashCode() {
        int result = 17;
        result = (23 * result) + ((request == null) ? 0 : request.hashCode());
        result = (23 * result) + ((request == null) ? 0 : version.hashCode());
        result = (23 * result) + ((request == null) ? 0 : service.hashCode());

        return result;
    }

    /**
     * Retrive the ServletRequest that generated this GeoServer request.
     * <p>
     * The ServletRequest is often used to:
     * </p>
	 * <ul>
	 * <li>Access the Sesssion and WebContainer by execute opperations
	 *     </li>
	 * <li>Of special importance is the use of the ServletRequest to locate the GeoServer Application
	 *     </li> 
	 * </p>
	 * <p>
	 * This method is called by AbstractServlet during the processing of a Request.
	 * </p>
	 * @return Returns the servletRequest.
	 */
	public ServletRequest getServletRequest() {
		return servletRequest;
	}
	/**
	 * Convience method casting getServletRequest() into a HttpServletRequest.
	 *  
	 * @return Returns the servletRequest as an HttpServletRequest
	 * @throws ClassCastException If the servletRequest is not an HttpServletRequest
	 */
	public HttpServletRequest getHttpServletRequest() throws ClassCastException {
		return (HttpServletRequest) getServletRequest();
	}
	
	/**
	 * Sets the servletRequest that generated this GeoServer request.
	 * <p>
	 * The ServletRequest is often used to:
	 * </p>
	 * <ul>
	 * <li>Access the Sesssion and WebContainer by execute opperations
	 *     </li>
	 * <li>Of special importance is the use of the ServletRequest to locate the GeoServer Application
	 *     </li> 
	 * </p>
	 * @param servletRequest The servletRequest to set.
	 */
	public void setServletRequest(ServletRequest servletRequest) {
		this.servletRequest = servletRequest;
	}

}
