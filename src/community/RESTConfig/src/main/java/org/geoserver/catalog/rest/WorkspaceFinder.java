package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.rest.RestletException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

public class WorkspaceFinder extends AbstractCatalogFinder {

    public WorkspaceFinder(Catalog catalog) {
        super(catalog);
        
    }
    
    @Override
    public AbstractCatalogResource findTarget(Request request, Response response) {
        String workspace = (String) request.getAttributes().get( "workspace" );
        if ( workspace != null ) {
            //ensure it exists
            if ( !"default".equals( workspace) && catalog.getWorkspace( workspace ) == null ) {
                throw new RestletException( "No such workspace: " + workspace, Status.CLIENT_ERROR_NOT_FOUND );
            }
        }
        
        return new WorkspaceResource( null, request, response, catalog );
    }

}
