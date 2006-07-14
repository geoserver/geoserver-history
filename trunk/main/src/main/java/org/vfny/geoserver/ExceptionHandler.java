/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver;

/**
 * DOCUMENT ME!
 *
 * @author Gabriel Rold�n
 * @version $revision$
 */
public interface ExceptionHandler {
    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(String message);

    /**
     * DOCUMENT ME!
     *
     * @param message DOCUMENT ME!
     * @param locator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(String message, String locator);

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(Throwable e);

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
        String locator);

    /*# ServiceException lnkServiceException; */
}
