/* Copyright (c) 2005 NATO - Undersea Research Centre.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.util.Requests;

/**
 * This defines an exception that can be turned into a valid xml service
 * exception that wfs clients will expect.  All errors should be wrapped in
 * this before returning to clients.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: WcsException.java,v 0.1 Feb 15, 2005 11:11:26 AM $
 */
public class WcsException extends ServiceException {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

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
    
    /**
     * Return request type.
     *
     * @param printStackTrace whether the stack trace should be included.
     * @param request DOCUMENT ME!
     *
     * @return The ServiceExceptionReport of this error.
     *
     * @task REVISIT: Our error handling should actually have knowledge of the
     *       app configuration, so that we can set the ogc error report to
     *       validate right (reference our own schema), and to put the correct
     *       mime type here.
     */
    public String getXmlResponse(boolean printStackTrace,
        HttpServletRequest request) {
        //Perhaps not the best place to do this, but it's by far the best place to ensure
        //that all logged errors get recorded in the same way, as there all must return
        //xml responses.

        String indent = "   ";

        StringBuffer returnXml = new StringBuffer("<?xml version=\"1.0\" ?>\n");

        returnXml.append("<ServiceExceptionReport\n");

        returnXml.append(indent + "version=\"1.2.0\"\n");

        returnXml.append(indent + "xmlns=\"http://www.opengis.net/ogc\"\n");

        returnXml.append(indent + "xmlns:xsi=\"http://www.w3.org/2001/"
            + "XMLSchema-instance\"\n");

        returnXml.append(indent);

        returnXml.append("xsi:schemaLocation=\"http://www.opengis.net/ogc ");

        returnXml.append(Requests.getSchemaBaseUrl(request)
            + "wcs/1.0.0/OGC-exception.xsd\">\n");

        //REVISIT: handle multiple service exceptions?  must refactor class.
        returnXml.append(indent + "<ServiceException");

        if (!isEmpty(this.code)) {
            returnXml.append(" code=\"" + this.code + "\"");
        }

        if (!isEmpty(this.locator)) {
            returnXml.append(" locator=\"" + this.locator + "\"");
        }

        returnXml.append(">\n" + indent + indent);
        returnXml.append(getXmlMessage(printStackTrace));

        returnXml.append(indent + "</ServiceException>\n");

        returnXml.append("</ServiceExceptionReport>");

        LOGGER.fine("return service exception is " + returnXml);

        return returnXml.toString();
    }
}
