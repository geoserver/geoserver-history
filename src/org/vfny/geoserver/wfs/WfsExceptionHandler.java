/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wfs;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.ServiceException;

/**
 * ServiceConfig exception handler for WFS services GR: should we rename it?
 * ExceptionHandler don't seems appropiate.
 *
 * @author Gabriel Rold?n
 * @version $Id: WfsExceptionHandler.java,v 1.5 2004/01/31 00:27:27 jive Exp $
 */
public class WfsExceptionHandler implements ExceptionHandler {
    /** DOCUMENT ME! */
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
