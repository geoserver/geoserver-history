/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.catalog.servlets;

import org.vfny.geoserver.ExceptionHandler;
import org.vfny.geoserver.catalog.CatalogExceptionHandler;
import org.vfny.geoserver.global.CATALOG;
import org.vfny.geoserver.servlets.AbstractService;
import javax.servlet.http.HttpServletRequest;


/**
 * Base servlet for all Catalog requests.
 *
 * <p>
 * Subclasses should supply the handler, request and response mapping for the
 * service they implement.
 * </p>
 *
 * @author $Author: Alessio Fabiani (GeoSolutions)
 */
abstract public class CATALOGService extends AbstractService {
    /**
         * Constructor for CATALOG service.
         *
         * @param request The service request being made (UpdateCatalog,...)
         * @param catalog The CATALOG service reference.
         */
    public CATALOGService(String request, CATALOG catalog) {
        super("CATALOG", request, catalog);
    }

    /**
     * @return The catalog service ref.
     */
    public CATALOG getCATALOG() {
        return (CATALOG) getServiceRef();
    }

    /**
     * Sets the catalog service ref.
     * @param catalog
     */
    public void setCATALOG(CATALOG catalog) {
        setServiceRef(catalog);
    }

    /**
     * a Web Coverage ServiceConfig exception handler
     *
     * @return an instance of CatalogExceptionHandler
     */
    protected ExceptionHandler getExceptionHandler() {
        return CatalogExceptionHandler.getInstance();
    }

    protected boolean isServiceEnabled(HttpServletRequest req) {
        return getCATALOG().isEnabled();
    }
}
