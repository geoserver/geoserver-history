package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.rest.RestletException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

public class DataStoreFinder extends AbstractCatalogFinder {

    public DataStoreFinder(Catalog catalog) {
        super(catalog);
    }

    @Override
    public AbstractCatalogResource findTarget(Request request, Response response) {
        String ws = (String) request.getAttributes().get( "workspace" );
        String ds = (String) request.getAttributes().get( "datastore" );
        
        //ensure referenced resources exist
        if ( ws != null && catalog.getWorkspaceByName( ws ) == null ) {
            throw new RestletException( "No such workspace: " + ws, Status.CLIENT_ERROR_NOT_FOUND );
        }
        if ( ds != null && catalog.getDataStoreByName(ws, ds) == null ) {
            throw new RestletException( "No such datastore: " + ws + "," + ds, Status.CLIENT_ERROR_NOT_FOUND );
        }
        
        return new DataStoreResource( null, request, response, catalog );
    }

}
