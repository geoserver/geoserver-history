/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2001 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.responses.wfs;

import org.vfny.geoserver.*;
import org.vfny.geoserver.responses.ResponseUtils;
import java.util.logging.*;


/**
 * This exception can turn itself into the appropriate WFS_TransactionResponse
 * xml, allowing transaction classes to throw exceptions that can be reported
 * back to the clients.
 *
 * @author Chris Holmes, TOPP
 * @version $VERSION$
 */
public class WfsTransactionException extends WfsException {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** the standard exception that was thrown */
    protected Exception standardException = new Exception();

    /** handle of the transaction request */
    protected String handle = new String();

    /**
     * Empty constructor.
     */
    public WfsTransactionException() {
        super();
    }

    /**
     * Constructor for message and handle.
     *
     * @param message indicates to the user what went wrong.
     */
    public WfsTransactionException(String message) {
        super(message);
    }

    /**
     * Constructor for an exception and a handle.
     *
     * @param cause indicates to the user what went wrong.
     */
    public WfsTransactionException(Throwable cause) {
        super(cause);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message indicates to the user what went wrong.
     * @param locator The message for the .
     */
    public WfsTransactionException(String message, String locator) {
        super(message, locator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message indicates to the user what went wrong.
     * @param locator The message for the .
     * @param handle the string of the transaction that failed.
     */
    public WfsTransactionException(String message, String locator, String handle) {
        super(message, locator);
        this.handle = handle;
    }

    /**
     * Constructor for an exception, messages and handles.
     *
     * @param e the root exception.
     * @param preMessage more information about exception.
     * @param locator indicates to the user what went wrong.
     */
    public WfsTransactionException(Exception e, String preMessage,
        String locator) {
        super(e, preMessage, locator);
    }

    /**
     * sets the handle, can be used when the initial handle is wrong.
     *
     * @param handle DOCUMENT ME!
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * Returns a WFS_TransactionResponse xml string indicating the failure.
     *
     * @return DOCUMENT ME!
     */
    public String getXmlResponse() {
        WfsTransResponse response = new WfsTransResponse(WfsTransResponse.FAILED,
                handle);
        response.setLocator(locator);

        //right now defaults to full stack traces, should change before
        //production release.
        response.setMessage(this.getXmlMessage(true));

        return response.getXmlResponse();
    }
}
