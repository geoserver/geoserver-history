/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wms;

import org.geoserver.platform.ServiceException;


/**
 * This defines an exception that can be turned into a valid xml service
 * exception that wms clients will expect.  All errors should be wrapped in
 * this before returning to clients.
 *
 * @author Gabriel Rold?n
 * @version $Id$
 * @deprecated use {@link ServiceException}
 */
public class WmsException extends ServiceException {
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
     * constructor with exception message
     *
     * @param message The message for the exception
     */
    public WmsException(String message) {
        super(message);
    }

    public WmsException(ServiceException e) {
        super(e);
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
        setCode(code);
    }
    
    /**
     * Empty constructor.
     *
     * @param message The message for the .
     * @param locator The message for the .
     */
    public WmsException(String message, String code, String locator) {
        super(message);
        setCode(code);
        setLocator(locator);
    }
    
    /**
     * Empty constructor.
     *
     * @param message The message for the .
     * @param locator The message for the .
     */
    public WmsException(String message, String code, Exception e) {
        super(message, e);
        setCode(code);
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

    public WmsException(Throwable e, String preMessage, String locator, String code) {
        this(e, preMessage, locator);
        setCode(code);
    }
}
