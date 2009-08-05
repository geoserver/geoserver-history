/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.rest.RestletException;
import org.restlet.data.Method;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

public class CatalogReloader extends AbstractCatalogFinder {

    protected CatalogReloader(Catalog catalog) {
        super(catalog);
    }

    @Override
    public Resource findTarget(Request request, Response response) {
        if (!(request.getMethod() == Method.POST || request.getMethod() == Method.PUT)) {
            return null;
        }
        
        try {
            CatalogResourceBase.reloadCatalogAndConfiguration();
            CatalogResourceBase.saveCatalog(catalog);
            CatalogResourceBase.saveConfiguration(catalog);
            response.setStatus(Status.SUCCESS_OK);
        } 
        catch (Exception e) {
            throw new RestletException("Error reloading catalog", Status.SERVER_ERROR_INTERNAL, e);
        }
        return null;
    }
}