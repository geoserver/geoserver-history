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
public interface ExceptionHandler {
    public ServiceException newServiceException(String message);

    public ServiceException newServiceException(String message, String locator);

    public ServiceException newServiceException(Throwable e);

    public ServiceException newServiceException(Throwable e, String preMessage,
        String locator);

    /*# ServiceException lnkServiceException; */
}
