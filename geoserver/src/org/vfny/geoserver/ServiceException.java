/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

import org.vfny.geoserver.config.*;
import org.vfny.geoserver.responses.ResponseUtils;
import java.io.IOException;
import java.util.logging.*;


/**
 * Represents a standard OGC service exception.  Able to turn itself into the
 * proper xml response.
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: ServiceException.java,v 1.1.2.3 2003/11/14 20:39:04 groldan Exp $
 */
public class ServiceException extends Exception
{
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** message inserted by GeoServer as to what it thinks happened */
    protected String preMessage = new String();

    /** full classpath of originating GeoServer class */
    protected String locator = new String();

    /** the standard exception that was thrown */
    protected Exception standardException = new Exception();

    /** DOCUMENT ME!  */
    protected String code = new String();

    /**
     * Empty constructor.
     */
    public ServiceException()
    {
        super();
    }

    /**
     * Empty constructor.
     *
     * @param message The message for the .
     */
    public ServiceException(String message)
    {
        super(message);

        LOGGER.fine(this.getMessage());
    }

    /**
     * Empty constructor.
     *
     * @param e The message for the .
     */
    public ServiceException(Throwable e)
    {
        super(e);

        LOGGER.fine(this.getMessage());
    }

    /**
     * Empty constructor.
     *
     * @param message The message for the .
     * @param locator The message for the .
     */
    public ServiceException(String message, String locator)
    {
        super(message);

        this.locator = locator;

        LOGGER.fine("> [" + this.locator + "]:\n  " + this.getMessage());
    }

    /**
     * DOCUMENT ME!
     *
     * @param e The message for the .
     * @param preMessage The message to tack on the front.
     * @param locator The message for the .
     */
    public ServiceException(Throwable e, String preMessage, String locator)
    {
        super(e);

        this.preMessage = preMessage;

        this.locator = locator;
    }

    /**
     * Assigns a diagnostic code to this exception.
     *
     * @param code The diagnostic code.
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * DOCUMENT ME!
     *
     * @param testString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean isEmpty(String testString)
    {
        return (testString == null) || testString.equals("");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXmlResponse()
    {
        return getXmlResponse(true);
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
    public String getXmlResponse(boolean printStackTrace)
    {
        String indent = "   ";

        StringBuffer returnXml = new StringBuffer("<?xml version=\"1.0\" ?>\n");

        returnXml.append("<ServiceExceptionReport\n");

        returnXml.append(indent + "version=\"1.2.0\"\n");

        returnXml.append(indent + "xmlns=\"http://www.opengis.net/ogc\"\n");

        returnXml.append(indent + "xmlns:xsi=\"http://www.w3.org/2001/"
            + "XMLSchema-instance\"\n");

        //REVISIT: this probably isn't right, need to learn about xml schemas
        returnXml.append(indent);

        returnXml.append("xsi:schemaLocation=\"http://www.opengis.net/ogc ");

        returnXml.append(ServerConfig.getInstance().getWFSConfig()
                                     .getSchemaBaseUrl());

        returnXml.append("wfs/1.0.0/OGC-exception.xsd\">\n");

        //REVISIT: handle multiple service exceptions?  must refactor class.
        returnXml.append(indent + "<ServiceException");

        if (!isEmpty(this.code))
        {
            returnXml.append(" code=\"" + this.code + "\"");
        }

        if (!isEmpty(this.locator))
        {
            returnXml.append(" locator=\"" + this.locator + "\"");
        }

        returnXml.append(">\n" + indent + indent);

        if (!isEmpty(this.preMessage))
        {
            returnXml.append(this.preMessage + ": ");
        }

        returnXml.append(this.getMessage() + "\n");

        if (printStackTrace)
        {
            Throwable cause = getCause();

            StackTraceElement[] trace = (cause == null) ? getStackTrace()
                                                        : cause.getStackTrace();

            for (int i = 0; i < trace.length; i++)
            {
                String line = indent + indent + "at " + trace[i].toString();

                returnXml.append(line + "\n");

                returnXml.append(ResponseUtils.encodeXML(line) + "\n");
            }
        }

        returnXml.append(indent + "</ServiceException>\n");

        returnXml.append("</ServiceExceptionReport>");

        LOGGER.fine("return wfs exception is " + returnXml);

        return returnXml.toString();

        //this is code from .14 and .15.  It can be reused if we need
        //backwards compatibility.

        /*String returnXml;
           returnXml = "<WFS_Exception>\n";
           returnXml = returnXml + "   <Exception>\n";
           returnXml = returnXml + "      <Locator>" + this.locator +
               "</Locator>\n";
           returnXml = returnXml + "      <Message>" + this.preMessage + ": " +
               this.getMessage() + "</Message>\n";
           returnXml = returnXml + "   </Exception>\n";
           returnXml = returnXml + "</WFS_Exception>\n";
           return returnXml;*/
    }
}
