/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.requests;

/**
 * Defines a general Request type and provides accessor methods for universal
 * request information.
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @author Gabriel Roldan
 * @version $Id: Request.java,v 1.3.4.2 2003/11/16 07:38:52 jive Exp $
 */
abstract public class Request {
    /** Request service */
    protected String service;

    /** Request type */
    protected String request = new String();

    /** Request version */
    protected String version = new String();

    /**
     * Service indentifying constructor.
     *
     * @param serviceType Name of services (like wms)
     */
    protected Request(String serviceType) {
        this.service = serviceType;
    }

    /**
     * Service & Request indentifying constructor.
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

    public int hashCode() {
        int result = 17;
        result = (23 * result) + ((request == null) ? 0 : request.hashCode());
        result = (23 * result) + ((request == null) ? 0 : version.hashCode());
        result = (23 * result) + ((request == null) ? 0 : service.hashCode());

        return result;
    }
}
