package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.LayerInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.StyleInfo;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class LayerResource extends AbstractCatalogResource {

    public LayerResource(Context context, Request request, Response response,
         Catalog catalog) {
        super(context, request, response, LayerInfo.class, catalog);
        
    }
    
    @Override
    protected Object handleObjectGet() throws Exception {
        String layer = getAttribute( "layer" );
        
        if ( layer == null ) {
            //return all layers
            return catalog.getLayers();
        }
        
        return catalog.getLayerByName( layer ); 
        
    }

    @Override
    protected String handleObjectPost(Object object) throws Exception {
        return null;
    }

    @Override
    public boolean allowPut() {
        return getAttribute( "layer" ) != null;
    }
    
    @Override
    protected void handleObjectPut(Object object) throws Exception {
        String l = getAttribute( "layer" );
        LayerInfo original = catalog.getLayerByName(l);
        LayerInfo layer = (LayerInfo) object;
        
        //ensure this is not a name change
        if ( layer.getName() != null && !layer.getName().equals( original.getName() ) ) {
            throw new RestletException( "Can't change name of a layer", Status.CLIENT_ERROR_FORBIDDEN );
        }
        
        new CatalogBuilder( catalog ).updateLayer( original, layer );
        catalog.save( original );
        saveCatalog();
        
        LOGGER.info( "PUT layer " + l);
    }
    
    @Override
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
        persister.setCallback(new XStreamPersister.Callback() {
            @Override
            protected void postEncodeReference(Object obj, String ref,
                    HierarchicalStreamWriter writer, MarshallingContext context) {
                if ( obj instanceof StyleInfo ) {
                    encodeLink( "/styles/" + ref, writer);    
                }
                if ( obj instanceof ResourceInfo ) {
                    ResourceInfo r = (ResourceInfo) obj;
                    StringBuffer link = new StringBuffer( "/workspaces/" ).
                        append( r.getStore().getWorkspace().getName() ).append( "/" );
                    if ( r instanceof FeatureTypeInfo ) {
                        link.append( "datastores/").append( r.getStore().getName() )
                            .append( "/featuretypes/");
                    }
                    else if ( r instanceof CoverageInfo ) {
                        link.append( "coveragestores/").append( r.getStore().getName() )
                        .append( "/coverages/");
                    }
                    else {
                        return;
                    }
                    
                    link.append( r.getName() );
                    encodeLink(link.toString(), writer);
                }
            }
        });
        
    }

}
