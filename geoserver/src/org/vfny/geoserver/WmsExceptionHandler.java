/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

/**
 * <p></p>
 * 
 * <p></p>
 * 
 * <p></p>
 * 
 * <p></p>
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public class WmsExceptionHandler implements ExceptionHandler {
    private static final WmsExceptionHandler instance = new WmsExceptionHandler();
    private WmsException lnkWmsException;

    private WmsExceptionHandler() {
    }

    public static final WmsExceptionHandler getInstance() {
        return instance;
    }

    public ServiceException newServiceException(String message) {
        return new WmsException(message);
    }

    public ServiceException newServiceException(String message, String locator) {
        return new WmsException(message, locator);
    }

    public ServiceException newServiceException(Throwable e) {
        return new WmsException(e);
    }

    public ServiceException newServiceException(Throwable e, String preMessage,
        String locator) {
        return new WmsException(e, preMessage, locator);
    }
}
