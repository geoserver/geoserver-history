/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.ows;


/**
 * A standard OGC service exception.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class ServiceException extends Exception {
    /** Application specfic code. */
    String code;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ServiceException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    /**
     * DOCUMENT ME!
     *
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
}
