/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.wcs;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.ServiceException;


/**
 * ServiceConfig exception handler for WCS services GR: should we rename it?
 * ExceptionHandler don't seems appropiate.
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public class WcsExceptionHandler implements ExceptionHandler {
    /** DOCUMENT ME! */
    private static final WcsExceptionHandler instance = new WcsExceptionHandler();

    /**
     *
     * @uml.property name="lnkWcsException"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private WcsException lnkWcsException;

    private WcsExceptionHandler() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static WcsExceptionHandler getInstance() {
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
        return new WcsException(message);
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
        return new WcsException(message, locator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(Throwable e) {
        return new WcsException(e);
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
    public ServiceException newServiceException(Throwable e, String preMessage, String locator) {
        return new WcsException(e, preMessage, locator);
    }
}
