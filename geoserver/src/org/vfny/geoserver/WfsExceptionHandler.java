/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

/**
 * ServiceConfig exception handler for WFSConfig services
 * GR: should we rename it? ExceptionHandler don't seems appropiate.
 * @author Gabriel Roldán
 * @version $Id: WfsExceptionHandler.java,v 1.2.2.1 2003/12/31 23:36:46 dmzwiers Exp $
 */
public class WfsExceptionHandler implements ExceptionHandler {
    /** DOCUMENT ME!  */
    private static final WfsExceptionHandler instance = new WfsExceptionHandler();
    private WfsException lnkWfsException;

    private WfsExceptionHandler() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static WfsExceptionHandler getInstance() {
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
        return new WfsException(message);
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
        return new WfsException(message, locator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(Throwable e) {
        return new WfsException(e);
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
        return new WfsException(e, preMessage, locator);
    }
}
