/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.logging.Logger;

import org.vfny.geoserver.global.ServerConfig;
import org.vfny.geoserver.responses.ResponseUtils;


/**
 * Represents a standard OGC service exception.  Able to turn itself into the
 * proper xml response.
 * 
 * <p>
 * JG - here is my guess on what the parameters do:
 * </p>
 * <pre><code>
 * [?xml version="1.0" ?
 * [ServiceExceptionReport
 *    version="1.2.0"
 *    xmlns="http://www.opengis.net/ogc"
 *    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
 *    xsi:schemaLocation="http://www.opengis.net/ogc <i>SchemaBaseUrl</i> wfs/1.0.0/OGC-exception.xsd"]
 *   [ServiceException code="<i>code</i>"
 *                     locator="<i>locator</i>"]
 *     </i>preMessage<i>:<i>getMessage()</i>
 *     <i>stack trace</i>
 *   [/ServiceException]
 * [/ServiceExceptionReport]     
 * </code></pre>
 * 
 * <p>
 * Where:
 * </p>
 * 
 * <ul>
 * <li>
 * code: is a diagnostic code
 * </li>
 * <li>
 * locator: is the java class that caused the problem
 * </li>
 * <li>
 * preMessage: is your chance to place things in user terms
 * </li>
 * <li>
 * message: is the exception message
 * </li>
 * <li>
 * stack trace: is the exception strack trace
 * </li>
 * </ul>
 * 
 * <p>
 * Java Exception have recently developed the ability to contain other
 * exceptions. By calling initCause on your Service Exception you can get the
 * real exception included in the stacktrace above.
 * </p>
 *
 * @author Gabriel Roldán
 * @author Chris Holmes
 * @version $Id: ServiceException.java,v 1.3.2.2 2003/12/30 23:08:28 dmzwiers Exp $
 *
 * @task TODO: print directly to an output stream for getXmlResponse.
 */
public class ServiceException extends Exception {
    /** Class logger */
    private static Logger LOGGER = Logger.getLogger(
            "org.vfny.geoserver.responses");

    /** message inserted by GeoServer as to what it thinks happened */
    protected String preMessage = new String();

    /** full classpath of originating GeoServer class */
    protected String locator = new String();

    /** Diagnostic code */
    protected String code = new String();

    /**
     * Empty constructor.
     */
    public ServiceException() {
        super();
    }

    /**
     * Empty constructor.
     *
     * @param message The message for the .
     */
    public ServiceException(String message) {
        super(message);

        LOGGER.fine(this.getMessage());
    }

    /**
     * This should be the most used entry point.
     *
     * @param message User message
     * @param cause The origional exception that caused failure
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Empty constructor.
     *
     * @param e The message for the .
     */
    public ServiceException(Throwable e) {
        super(e);

        LOGGER.fine(this.getMessage());
    }

    /**
     * Empty constructor.
     *
     * @param message The message for the .
     * @param locator The message for the .
     */
    public ServiceException(String message, String locator) {
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
    public ServiceException(Throwable e, String preMessage, String locator) {
        super(e);

        this.preMessage = preMessage;

        this.locator = locator;
    }

    /**
     * Assigns a diagnostic code to this exception.
     *
     * @param code The diagnostic code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * DOCUMENT ME!
     *
     * @param testString DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected boolean isEmpty(String testString) {
        return (testString == null) || testString.equals("");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXmlResponse() {
        return getXmlResponse(true);
    }

    /**
     * gets the message, encoding it with the proper escaped xml characters. If
     * requested it prints the whole stack trace in the response.
     *
     * @param printStackTrace set to <tt>true</tt> if the full stack trace
     *        should be returned to client apps.
     *
     * @return The message of this error, with xml escapes.
     *
     * @task REVISIT: The stack trace printing is not that efficient, but it
     *       should be relatively small.  Once we convert errors to print
     *       directly to the servlet output stream we can make it faster.
     */
    public String getXmlMessage(boolean printStackTrace) {
        String indent = "   ";
        StringBuffer mesg = new StringBuffer();

        if (!isEmpty(this.preMessage)) {
            mesg.append(this.preMessage + ": ");
        }

        //mesg.append(ResponseUtils.encodeXML(this.getMessage()) + "\n");
        if (printStackTrace) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(baos);
            Throwable cause = getCause();

            if (cause == null) {
                this.printStackTrace(writer);
            } else {
                cause.printStackTrace(writer);
            }

            writer.flush();
            mesg.append(baos.toString());
        } else {
            mesg.append(this.getMessage());
        }

        return ResponseUtils.encodeXML(mesg.toString());
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
    public String getXmlResponse(boolean printStackTrace) {
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
