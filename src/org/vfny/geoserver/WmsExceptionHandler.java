/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

/**
 * ServiceConfig exception handler for WMS services
 * @author Gabriel Roldán
 * @version $Id: WmsExceptionHandler.java,v 1.3 2004/01/12 21:01:32 dmzwiers Exp $
 */
public class WmsExceptionHandler implements ExceptionHandler {
    /** DOCUMENT ME!  */
    private static final WmsExceptionHandler instance = new WmsExceptionHandler();
    private WmsException lnkWmsException;

    private WmsExceptionHandler() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static final WmsExceptionHandler getInstance() {
        return instance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(String message) {
        return new WmsException(message);
    }

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param locator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(String message, String locator) {
        return new WmsException(message, locator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(Throwable e) {
        return new WmsException(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @param preMessage DOCUMENT ME!
     * @param locator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(Throwable e, String preMessage,
        String locator) {
        return new WmsException(e, preMessage, locator);
    }
}
