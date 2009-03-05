package org.geoserver.catalog.rest;

import java.util.List;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.LayerInfo;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;

public class LayerListResource extends AbstractCatalogListResource {

    protected LayerListResource(Context context, Request request,
            Response response, Catalog catalog) {
        super(context, request, response, LayerInfo.class, catalog);
        
    }

    @Override
    protected List handleListGet() throws Exception {
        LOGGER.fine( "GET all layers");
        return catalog.getLayers();
    }

}
