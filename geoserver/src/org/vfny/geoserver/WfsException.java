/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

/**
 * This defines an exception that can be turned into a valid xml service
 * exception that wfs clients will expect.  All errors should be wrapped in
 * this before returning to clients.
 *
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * @version $Id: WfsException.java,v 1.1.2.2 2003/11/14 20:39:04 groldan Exp $
 *
 * @task REVISIT: refactor this to be named ServiceException?  To reflect the
 *       1.0 spec better?  Backwards compatible exceptions for .14 and .15
 */
public class WfsException extends ServiceException
{
    /**
     * Empty constructor.
     */
    public WfsException()
    {
        super();
    }

    /**
     * Empty constructor.
     *
     * @param message The message for the .
     */
    public WfsException(String message)
    {
        super(message);
    }

    /**
     * Empty constructor.
     *
     * @param e The message for the .
     */
    public WfsException(Throwable e)
    {
        super(e);
    }

    /**
     * Empty constructor.
     *
     * @param message The message for the .
     * @param locator The message for the .
     */
    public WfsException(String message, String locator)
    {
        super(message);
        this.locator = locator;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e The message for the .
     * @param preMessage The message to tack on the front.
     * @param locator The message for the .
     */
    public WfsException(Throwable e, String preMessage, String locator)
    {
        super(e, preMessage, locator);
    }
}
