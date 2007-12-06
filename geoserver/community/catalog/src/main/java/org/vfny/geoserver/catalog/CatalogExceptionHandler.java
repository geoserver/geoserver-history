/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.ServiceException;


/**
 * ...
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
public class CatalogExceptionHandler implements ExceptionHandler {
    /** DOCUMENT ME! */
    private static final CatalogExceptionHandler instance = new CatalogExceptionHandler();

    /**
     *
     * @uml.property name="lnkCatalogException"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private CatalogException lnkCatalogException;

    private CatalogExceptionHandler() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CatalogExceptionHandler getInstance() {
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
        return new CatalogException(message);
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
        return new CatalogException(message, locator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ServiceException newServiceException(Throwable e) {
        return new CatalogException(e);
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
        return new CatalogException(e, preMessage, locator);
    }
}
