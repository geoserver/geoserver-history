/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import javax.servlet.http.HttpServletRequest;

import org.vfny.geoserver.ServiceException;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.util.Requests;

/**
 * This defines an exception that can be turned into a valid xml service
 * exception that wms clients will expect.  All errors should be wrapped in
 * this before returning to clients.
 *
 * @author Gabriel Rold?n
 * @version $Id: WmsException.java,v 1.7 2004/09/08 17:34:04 cholmesny Exp $
 */
public class WmsException extends ServiceException {
	
	/**
	 * The fixed MIME type of a WMS exception.
	 */
	private static final String SE_XML = "application/vnd.ogc.se_xml";
	
	/**
	 * Enum of exception codes defined in Anex A.3 of WMS 1.1.1 spec 
	public static class ExceptionCodeEnum{
		private String exceptionCode;
		private ExceptionCodeEnum(String code){
			this.exceptionCode = code;
		}
		public String toString(){
			return exceptionCode;
		}
	}
	public static final ExceptionCodeEnum InvalidFormat = new ExceptionCodeEnum("InvalidFormat");
	public static final ExceptionCodeEnum InvalidSRS = new ExceptionCodeEnum("InvalidSRS");
	public static final ExceptionCodeEnum LayerNotDefined = new ExceptionCodeEnum("LayerNotDefined");
	public static final ExceptionCodeEnum StyleNotDefined = new ExceptionCodeEnum("StyleNotDefined");
	public static final ExceptionCodeEnum LayerNotQueryable = new ExceptionCodeEnum("LayerNotQueryable");
	public static final ExceptionCodeEnum CurrentUpdateSequence = new ExceptionCodeEnum("CurrentUpdateSequence");
	public static final ExceptionCodeEnum InvalidUpdateSequence = new ExceptionCodeEnum("InvalidUpdateSequence");
	public static final ExceptionCodeEnum MissingDimensionValue = new ExceptionCodeEnum("MissingDimensionValue");
	public static final ExceptionCodeEnum InvalidDimensionValue = new ExceptionCodeEnum("InvalidDimensionValue");
	 */
	
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
        super(e.getMessage(), e);
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
    
    /**
     * Returns the MIME type of a WMS exception.
     * 
     * @return <code>"application/vnd.ogc.se_xml"</code>
     */
    public String getMimeType(GeoServer geoserver){
    	return SE_XML;
    }
    
}
