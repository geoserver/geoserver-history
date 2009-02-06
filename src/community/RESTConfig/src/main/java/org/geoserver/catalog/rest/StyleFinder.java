package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.rest.RestletException;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

public class StyleFinder extends AbstractCatalogFinder {

    public StyleFinder(Catalog catalog) {
        super(catalog);
    }
    
    @Override
    public Resource findTarget(Request request, Response response) {
        String style = (String) request.getAttributes().get( "style" );
        String layer = (String) request.getAttributes().get( "layer" );

        //check style exists if specified
        if ( style != null && catalog.getStyleByName( style ) == null ) {
            throw new RestletException( "No such style: " + style, Status.CLIENT_ERROR_NOT_FOUND );
        }

        //check layer exists if specified
        if ( layer != null && catalog.getLayerByName( layer ) == null ) {
            throw new RestletException( "No such layer: " + layer, Status.CLIENT_ERROR_NOT_FOUND);
            /*
            String ns = null;
            String resource = null;
            
            if ( layer.contains( ":" ) ) {
                String[] split = layer.split(":");
                ns = split[0];
                resource = split[1];
            }
            else {
                ns = catalog.getDefaultNamespace().getPrefix();
                resource = layer;
            }
            
            if ( catalog.getResourceByName( ns, resource, ResourceInfo.class) == null ) {
                throw new RestletException( "No such layer: " + ns + "," + resource, Status.CLIENT_ERROR_NOT_FOUND);
            }
            
            //set the parsed result as request attributes
            request.getAttributes().put( "namespace", ns );
            request.getAttributes().put( "resource", resource );
            */
        }
        
        return new StyleResource(getContext(),request,response,catalog);
    }

}
