package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.rest.RestletException;
import org.restlet.data.Form;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

public class FeatureTypeFinder extends AbstractCatalogFinder {

    protected FeatureTypeFinder(Catalog catalog) {
        super(catalog);
    }
    
    @Override
    public Resource findTarget(Request request, Response response) {
        String ws = (String) request.getAttributes().get( "workspace" );
        String ds = (String) request.getAttributes().get( "datastore" );
        String ft = (String) request.getAttributes().get( "featuretype");
        
        //ensure referenced resources exist
        if ( ws != null && catalog.getWorkspaceByName( ws ) == null ) {
            throw new RestletException( "No such workspace: " + ws, Status.CLIENT_ERROR_NOT_FOUND );
        }
        if ( ds != null && catalog.getDataStoreByName(ws, ds) == null ) {
            throw new RestletException( "No such datastore: " + ws + "," + ds, Status.CLIENT_ERROR_NOT_FOUND );
        }
        if ( ft != null ) {
            if ( ds != null &&
                    catalog.getFeatureTypeByDataStore(catalog.getDataStoreByName(ws, ds), ft) == null) {
                throw new RestletException( "No such feature type: "+ws+","+ds+","+ft, Status.CLIENT_ERROR_NOT_FOUND );
            }
            else {
                //look up by workspace/namespace
                NamespaceInfo ns = catalog.getNamespaceByPrefix( ws );
                if ( ns == null || catalog.getFeatureTypeByName( ns, ft ) == null ) {
                    throw new RestletException( "No such feature type: "+ws+","+ft, Status.CLIENT_ERROR_NOT_FOUND );
                }
            
            }
        }
            
        Form form = request.getResourceRef().getQueryAsForm();
        if ( "available".equalsIgnoreCase( form.getFirstValue( "list" ) ) ) {
            return new AvailableFeatureTypeResource(null,request,response,catalog);
        }
        return new FeatureTypeResource(null,request,response,catalog);
    }

}
