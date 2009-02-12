package org.geoserver.catalog.rest;

import java.util.List;
import java.util.Map;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.CoverageInfo;
import org.geoserver.catalog.CoverageStoreInfo;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.Resource;

import freemarker.ext.beans.CollectionModel;
import freemarker.template.Configuration;
import freemarker.template.SimpleHash;

public class CoverageStoreResource extends AbstractCatalogResource {

    public CoverageStoreResource(Context context, Request request,
            Response response, Catalog catalog) {
        super(context, request, response, CoverageStoreInfo.class, catalog);
    }
    
    @Override
    protected DataFormat createHTMLFormat(Request request, Response response) {
        return new CoverageStoreHTMLFormat( request, response, this, catalog ); 
    }

    @Override
    protected Object handleObjectGet() throws Exception {
        String ws = getAttribute( "workspace" );
        String cs = getAttribute( "coveragestore" );
        
        LOGGER.fine( "GET coverage store " + ws + "," + cs );
        
        if ( cs == null ) {
            return catalog.getCoverageStoresByWorkspace( ws );
        }
        
        return catalog.getCoverageStoreByName( ws, cs );
    }

    @Override
    public boolean allowPost() {
        return getAttribute("coveragestore") == null;
    }
    
    @Override
    protected String handleObjectPost(Object object) throws Exception {
        CoverageStoreInfo coverageStore = (CoverageStoreInfo) object;
        catalog.add( coverageStore );
        saveCatalog();
        
        LOGGER.info( "POST coverage store " + coverageStore.getName() );
        return coverageStore.getName();
    }

    @Override
    public boolean allowPut() {
        return getAttribute( "coveragestore" ) != null;
    }
    
    @Override
    protected void handleObjectPut(Object object) throws Exception {
        String workspace = getAttribute("workspace");
        String coveragestore = getAttribute("coveragestore");
        
        CoverageStoreInfo cs = (CoverageStoreInfo) object;
        CoverageStoreInfo original = catalog.getCoverageStoreByName(workspace, coveragestore);
        new CatalogBuilder( catalog ).updateCoverageStore( original, cs );
        
        catalog.save( original );
        saveCatalog();
        
        LOGGER.info( "PUT coverage store " + workspace + "," + coveragestore );
    }
    
    @Override
    public boolean allowDelete() {
        return getAttribute( "coveragestore" ) != null;
    }
    
    @Override
    protected void handleObjectDelete() throws Exception {
        String workspace = getAttribute("workspace");
        String coveragestore = getAttribute("coveragestore");
        
        CoverageStoreInfo cs = catalog.getCoverageStoreByName(workspace, coveragestore);
        if ( !catalog.getCoveragesByCoverageStore(cs).isEmpty() ) {
            throw new RestletException( "coveragestore not empty", Status.CLIENT_ERROR_UNAUTHORIZED);
        }
        catalog.remove( cs );
        saveCatalog();
        
        LOGGER.info( "DELETE coverage store " + workspace + "," + coveragestore );
    }
    
    static class CoverageStoreHTMLFormat extends CatalogFreemarkerHTMLFormat {
        Catalog catalog;
        
        public CoverageStoreHTMLFormat(Request request,
                Response response, Resource resource, Catalog catalog) {
            super(CoverageStoreInfo.class, request, response, resource);
            this.catalog = catalog;
        }

        @Override
        protected Configuration createConfiguration(Object data, Class clazz) {
            Configuration cfg = 
                super.createConfiguration(data, clazz);
            cfg.setObjectWrapper(new ObjectToMapWrapper<CoverageStoreInfo>(CoverageStoreInfo.class) {
                @Override
                protected void wrapInternal(Map properties, SimpleHash model, CoverageStoreInfo object) {
                    List<CoverageInfo> coverages = catalog.getCoveragesByCoverageStore(object);
                    
                    properties.put( "coverages", new CollectionModel( coverages, new ObjectToMapWrapper(CoverageInfo.class) ) );
                }
            });
            
            return cfg;
        }
    };

}
