/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

/**
 * DOCUMENT ME!
 *
 * @author Gabriel Roldán
 * @version 0.1
 */
public class WfsExceptionHandler implements ExceptionHandler {
    private static final WfsExceptionHandler instance = new WfsExceptionHandler();
    private WfsException lnkWfsException;

    private WfsExceptionHandler() {
    }

    public static WfsExceptionHandler getInstance() {
        return instance;
    }

    public ServiceException newServiceException(String message) {
        return new WfsException(message);
    }

    public ServiceException newServiceException(String message, String locator) {
        return new WfsException(message, locator);
    }

    public ServiceException newServiceException(Throwable e) {
        return new WfsException(e);
    }

    public ServiceException newServiceException(Throwable e, String preMessage,
        String locator) {
        return new WfsException(e, preMessage, locator);
    }
}
