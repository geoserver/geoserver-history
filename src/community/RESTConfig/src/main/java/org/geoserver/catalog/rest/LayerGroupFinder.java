package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.rest.RestletException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

public class LayerGroupFinder extends AbstractCatalogFinder {

    public LayerGroupFinder(Catalog catalog) {
        super(catalog);
    }
    
    @Override
    public Resource findTarget(Request request, Response response) {
        String lg = (String) request.getAttributes().get( "layergroup" );
        if ( lg != null && catalog.getLayerGroupByName( lg ) == null ) {
            throw new RestletException( "No such layer group " + lg, Status.CLIENT_ERROR_NOT_FOUND );
        }
        
        return new LayerGroupResource( getContext(), request, response, catalog );
    }

}
