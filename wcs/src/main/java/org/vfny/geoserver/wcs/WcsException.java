/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.util.Requests;

/**
 * This defines an exception that can be turned into a valid xml service
 * exception that wcs clients will expect. All errors should be wrapped in this
 * before returning to clients.
 * 
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last
 *         modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last
 *         modification)
 * @version $Id: WcsException.java,v 0.1 Feb 15, 2005 11:11:26 AM $
 */
public class WcsException extends ServiceException {
    /**
     * Empty constructor.
     */
    public WcsException() {
        super();
    }

    /**
     * Message constructor.
     *
     * @param message The message for the .
     */
    public WcsException(String message) {
        super(message);
    }

    /**
     * Throwable constructor.
     *
     * @param e The message for the .
     */
    public WcsException(Throwable e) {
        super(e);
    }

    /**
     * Message Locator constructor.
     *
     * @param message The message for the .
     * @param locator The java class that caused the problem
     */
    public WcsException(String message, String locator) {
        super(message, locator);
    }

    public WcsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e The cause of failure
     * @param preMessage The message to tack on the front.
     * @param locator The java class that caused the problem
     */
    public WcsException(Throwable e, String preMessage, String locator) {
        super(e, preMessage, locator);
    }
}
