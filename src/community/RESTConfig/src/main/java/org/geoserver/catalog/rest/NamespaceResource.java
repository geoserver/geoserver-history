package org.geoserver.catalog.rest;

import java.util.List;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.catalog.ResourceInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import freemarker.ext.beans.CollectionModel;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;

public class NamespaceResource extends AbstractCatalogResource {

    public NamespaceResource(Context context, Request request,
            Response response, Catalog catalog) {
        super(context, request, response, NamespaceInfo.class, catalog);
    }

    @Override
    protected DataFormat createHTMLFormat(Request request, Response response) {
        return new CatalogFreemarkerHTMLFormat( NamespaceInfo.class,request,response,this ) {
          
            @Override
            protected Configuration createConfiguration(Object data, Class clazz) {
                Configuration cfg = 
                    super.createConfiguration(data, clazz);
                cfg.setObjectWrapper(new ObjectToMapWrapper<NamespaceInfo>(NamespaceInfo.class) {
                    @Override
                    protected void wrapInternal(Map properties, SimpleHash model, NamespaceInfo object) {
                        List<ResourceInfo> stores = catalog.getResourcesByNamespace(object, ResourceInfo.class);
                        properties.put( "resources", new CollectionModel( stores, new ObjectToMapWrapper(ResourceInfo.class) ) );
                        properties.put( "isDefault",  object.equals( catalog.getDefaultNamespace() ) );
                    }
                });
                
                return cfg;
            }
        };
    }
    
    
    @Override
    protected Object handleObjectGet() throws Exception {
        String ns = getAttribute( "namespace" );
        
        LOGGER.fine( "GET namespace" + ns == null ? "s" : " " + ns);
        
        //if no namespace specified, return all
        if ( ns == null ) {
            return catalog.getNamespaces();
        }
        else if ( "default".equals( ns ) ) {
            return catalog.getDefaultNamespace();
        }
        else {
            return catalog.getNamespaceByPrefix( ns );
        }
    }

    @Override
    public boolean allowPost() {
        return getAttribute( "namespace") == null;
    }
    
    @Override
    protected String handleObjectPost(Object object) throws Exception {
        NamespaceInfo namespace = (NamespaceInfo) object;
        catalog.add( namespace );
        
        //JD: we need to keep namespace and workspace in sync, so create a worksapce
        // if one does not already exists, we can remove this once we get to a point
        // where namespace is just an attribute on a layer, and not a containing element
        if ( catalog.getWorkspaceByName( namespace.getPrefix() ) == null ) {
            WorkspaceInfo ws = catalog.getFactory().createWorkspace();
            ws.setName( namespace.getPrefix() );
            catalog.add( ws );
        }
        
        saveCatalog();
        LOGGER.info( "POST namespace " + namespace );
        return namespace.getPrefix();
    }
    
    @Override
    public boolean allowPut() {
        return getAttribute( "namespace") != null;
    }
    
    @Override
    protected void handleObjectPut(Object object) throws Exception {
        NamespaceInfo namespace = (NamespaceInfo) object;
        String ns = (String) getRequest().getAttributes().get( "namespace" );
        
        if ( "default".equals( ns ) ) {
            catalog.setDefaultNamespace( namespace );
        }
        else {
            NamespaceInfo original = catalog.getNamespaceByPrefix( ns );
            //ensure this is not a prefix change
            if ( namespace.getPrefix() != null && !namespace.getPrefix().equals( original.getPrefix() ) ) {
                throw new RestletException( "Can't change the prefix of a namespace.", Status.CLIENT_ERROR_FORBIDDEN );
            }

            new CatalogBuilder(catalog).updateNamespace( original, namespace );
            catalog.save( original );
        }
        LOGGER.info( "PUT namespace " + namespace);
        saveCatalog();
    }
    
    @Override
    public boolean allowDelete() {
        return getAttribute( "namespace" ) != null;
    }
    
    @Override
    protected void handleObjectDelete() throws Exception {
        String namespace = (String) getRequest().getAttributes().get( "namespace");
        NamespaceInfo ns = catalog.getNamespace( namespace );
        
        if ( !catalog.getResourcesByNamespace(ns, ResourceInfo.class).isEmpty() ) {
            throw new RestletException( "Namespace not empty", Status.CLIENT_ERROR_UNAUTHORIZED );
        }
        
        catalog.remove( ns );
        
        LOGGER.info( "DELETE namespace " + namespace);
        saveCatalog();
    }

}
