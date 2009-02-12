package org.geoserver.catalog.rest;

import java.util.List;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.StoreInfo;
import org.geoserver.catalog.WorkspaceInfo;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.rest.PageInfo;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import freemarker.ext.beans.CollectionModel;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;

public class WorkspaceResource extends AbstractCatalogResource {

    public WorkspaceResource(Context context, Request request, Response response, Catalog catalog) {
        super(context, request, response, WorkspaceInfo.class, catalog);
    }
    
    @Override
    protected DataFormat createHTMLFormat(Request request, Response response) {
        return new WorkspaceHTMLFormat(request,response,this,catalog);
    }
    
    @Override
    protected void configurePersister(XStreamPersister persister) {
        persister.setCallback( new XStreamPersister.Callback() {
            @Override
            protected void postEncodeWorkspace(WorkspaceInfo ws,
                    HierarchicalStreamWriter writer, MarshallingContext context) {
                PageInfo pg = getPageInfo();
                
                //add a link to the stores
                writer.startNode( "dataStores");
                encodeAlternateAtomLink("datastores", writer);
                writer.endNode();
                
                writer.startNode( "coverageStores");
                encodeAlternateAtomLink("coveragestores", writer);
                writer.endNode();
            }
        });
    }
    
    @Override
    public boolean allowGet() {
        return getAttribute( "workspace" ) != null;
    }
    
    @Override
    protected Object handleObjectGet() {
        String ws = getAttribute( "workspace" );
       
        LOGGER.fine( "GET workspace " + ws);
        
        //if no workspace specified, return all
        if ( "default".equals( ws ) ) {
            return catalog.getDefaultWorkspace();
        }
        else {
            return catalog.getWorkspaceByName( ws );
        }
    }
    
    @Override
    public boolean allowPost() {
        return getAttribute( "workspace") == null;
    }
    
    @Override
    protected String handleObjectPost(Object object) throws Exception {
        WorkspaceInfo workspace = (WorkspaceInfo) object;
        catalog.add( workspace );
        saveCatalog();
        
        LOGGER.info( "POST workspace " + workspace.getName() );
        return workspace.getName();
    }
    
    @Override
    public boolean allowPut() {
        return getAttribute( "workspace") != null;
    }
    
    @Override
    protected void handleObjectPut(Object object) throws Exception {
        WorkspaceInfo workspace = (WorkspaceInfo) object;
        String ws = (String) getRequest().getAttributes().get( "workspace" );
        
        if ( "default".equals( ws ) ) {
            catalog.setDefaultWorkspace( workspace );
        }
        else {
            WorkspaceInfo original = catalog.getWorkspaceByName( ws );
            
            //ensure this is not a name change
            if ( workspace.getName() != null && !workspace.getName().equals( original.getName() ) ) {
                throw new RestletException( "Can't change the name of a workspace.", Status.CLIENT_ERROR_FORBIDDEN );
            }
            
            new CatalogBuilder(catalog).updateWorkspace( original, workspace );
            catalog.save( original );
        }
        
        saveCatalog();
        LOGGER.info( "PUT workspace " + ws );
    }
    
    @Override
    public boolean allowDelete() {
        return getAttribute( "workspace" ) != null && !"default".equals( getAttribute("workspace"));
    }
    
    @Override
    protected void handleObjectDelete() throws Exception {
        String workspace = (String) getRequest().getAttributes().get( "workspace");
        WorkspaceInfo ws = catalog.getWorkspaceByName( workspace );
        
        if ( !catalog.getStoresByWorkspace(ws, StoreInfo.class).isEmpty() ) {
            throw new RestletException( "Workspace not empty", Status.CLIENT_ERROR_FORBIDDEN );
        }
        
        catalog.remove( ws );
        saveCatalog();
        LOGGER.info( "DELETE workspace " + ws );
    }
    
    static class WorkspaceHTMLFormat extends CatalogFreemarkerHTMLFormat {
    
        Catalog catalog;
        
        public WorkspaceHTMLFormat(Request request, Response response, Resource resource, Catalog catalog ) {
            super(WorkspaceInfo.class, request, response, resource);
            this.catalog = catalog;
        }
        
        @Override
        protected Configuration createConfiguration(Object data, Class clazz) {
            Configuration cfg = 
                super.createConfiguration(data, clazz);
            cfg.setObjectWrapper(new ObjectToMapWrapper<WorkspaceInfo>(WorkspaceInfo.class) {
                @Override
                protected void wrapInternal(Map properties, SimpleHash model, WorkspaceInfo object) {
                    List<StoreInfo> stores = catalog.getStoresByWorkspace(object, StoreInfo.class);
                    properties.put( "stores", new CollectionModel( stores, new ObjectToMapWrapper(StoreInfo.class) ) );
                    properties.put( "isDefault",  object.equals( catalog.getDefaultWorkspace() ) );
                }
            });
            
            return cfg;
        }
    };
}
