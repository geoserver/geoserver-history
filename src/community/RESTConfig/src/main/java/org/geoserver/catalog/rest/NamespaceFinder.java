package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.rest.RestletException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

public class NamespaceFinder extends AbstractCatalogFinder {

    public NamespaceFinder(Catalog catalog) {
        super(catalog);
    }
    
    @Override
    public Resource findTarget(Request request, Response response) {
        String namespace = (String) request.getAttributes().get( "namespace" );
        if ( namespace != null ) {
            //ensure it exists
            if ( !"default".equals( namespace ) && catalog.getNamespace( namespace ) == null ) {
                throw new RestletException( "No such namespace: " + namespace, Status.CLIENT_ERROR_NOT_FOUND );
            }
        }
        
        return new NamespaceResource( null, request, response, catalog );
    }

}
