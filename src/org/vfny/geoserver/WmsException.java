/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

import javax.servlet.http.HttpServletRequest;
import org.vfny.geoserver.requests.Requests;

/**
 * This defines an exception that can be turned into a valid xml service
 * exception that wms clients will expect.  All errors should be wrapped in
 * this before returning to clients.
 *
 * @author Gabriel Roldán
 * @version $Id: WmsException.java,v 1.7 2004/09/08 17:34:04 cholmesny Exp $
 */
public class WmsException extends ServiceException {
    /**
     * Empty constructor.
     */
    public WmsException() {
        super();
    }

    /**
     * constructor with exception message
     *
     * @param message The message for the exception
     */
    public WmsException(String message) {
        super(message);
    }

    /**
     * Empty constructor.
     *
     * @param e The message for the .
     */
    public WmsException(Throwable e) {
        super(e);
    }

    /**
     * Empty constructor.
     *
     * @param message The message for the .
     * @param locator The message for the .
     */
    public WmsException(String message, String code) {
        super(message);

        this.code = code;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e The message for the .
     * @param preMessage The message to tack on the front.
     * @param locator The message for the .
     */
    public WmsException(Throwable e, String preMessage, String locator) {
        super(e, preMessage, locator);
    }

    /**
     * Return request type.
     *
     * @param printStackTrace whether the stack trace should be included.
     *
     * @return The ServiceExceptionReport of this error.
     *
     * @task REVISIT: adapt it to handle WMS too
     */
    public String getXmlResponse(boolean printStackTrace, HttpServletRequest request) {
        StringBuffer returnXml = new StringBuffer("<?xml version=\"1.0\"");
        returnXml.append(" encoding=\"UTF-8\" standalone=\"no\" ?>");
        String dtdUrl = Requests.getSchemaBaseUrl(request) + 
			"/wms/1.1.1/WMS_exception_1_1_1.dtd";
        returnXml.append(
            "<!DOCTYPE ServiceExceptionReport SYSTEM \"" + dtdUrl + "\"> ");
        returnXml.append("<ServiceExceptionReport version=\"1.1.1\">");

        // Write exception code
        returnXml.append("    <ServiceException"
            + ((code != null) ? (" code=\"" + code + "\"") : "") + ">"
            + getXmlMessage(printStackTrace) + "</ServiceException>");

        // Write footer
        returnXml.append("  </ServiceExceptionReport>");

        return returnXml.toString();
    }
}
