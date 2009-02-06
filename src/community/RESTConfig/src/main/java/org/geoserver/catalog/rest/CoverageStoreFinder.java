package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.rest.RestletException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

public class CoverageStoreFinder extends AbstractCatalogFinder {

    public CoverageStoreFinder(Catalog catalog) {
        super(catalog);
    }
    
    @Override
    public Resource findTarget(Request request, Response response) {
        String ws = (String) request.getAttributes().get( "workspace" );
        String cs = (String) request.getAttributes().get( "coveragestore" );
        
        //ensure referenced resources exist
        if ( ws != null && catalog.getWorkspaceByName( ws ) == null ) {
            throw new RestletException( "No such workspace: " + ws, Status.CLIENT_ERROR_NOT_FOUND );
        }
        if ( cs != null && catalog.getCoverageStoreByName(ws, cs) == null ) {
            throw new RestletException( "No such coverage store: " + ws + "," + cs, Status.CLIENT_ERROR_NOT_FOUND );
        }
        
        return new CoverageStoreResource( null, request, response, catalog );
    }
}
