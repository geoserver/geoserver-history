package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.rest.RestletException;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

public class FeatureTypeResource extends AbstractCatalogResource {

    public FeatureTypeResource(Context context, Request request,Response response, Catalog catalog) {
        super(context, request, response, FeatureTypeInfo.class, catalog);
    }

    @Override
    protected Object handleObjectGet() {
        String workspace = getAttribute( "workspace");
        String datastore = getAttribute( "datastore");
        String featureType = getAttribute( "featuretype" );

        if ( datastore == null ) {
            LOGGER.fine( "GET feature type" + workspace + "," + featureType );
            
            //grab the corresponding namespace for this workspace
            NamespaceInfo ns = catalog.getNamespaceByPrefix( workspace );
            if ( ns != null ) {

                if ( featureType != null ) {
                    return catalog.getFeatureTypeByName(ns,featureType);
                }

                return catalog.getFeatureTypesByNamespace(ns);    
            }

            throw new RestletException( "", Status.CLIENT_ERROR_NOT_FOUND );
        }

        LOGGER.fine( "GET feature type" + datastore + "," + featureType );
        DataStoreInfo ds = catalog.getDataStoreByName(workspace, datastore);
        if ( featureType != null ) {
            return catalog.getFeatureTypeByDataStore( ds, featureType );
        }

        return catalog.getFeatureTypesByDataStore(ds);
    }

    @Override
    public boolean allowPost() {
        return getAttribute("featuretype") == null;
    }
    
    @Override
    protected String handleObjectPost(Object object) throws Exception {
        String workspace = getAttribute( "workspace");
        String dataStore = getAttribute( "datastore");

        FeatureTypeInfo featureType = (FeatureTypeInfo) object;
        if ( featureType.getStore() == null ) {
            //get from requests
            DataStoreInfo ds = catalog.getDataStoreByName( workspace, dataStore );
            featureType.setStore( ds );
        }
        
        NamespaceInfo ns = featureType.getNamespace();
        if ( ns != null && !ns.getPrefix().equals( workspace ) ) {
            //TODO: change this once the two can be different and we untie namespace
            // from workspace
            LOGGER.warning( "Namespace: " + ns.getPrefix() + " does not match workspace: " + workspace + ", overriding." );
            ns = null;
        }
        
        if ( ns == null){
            //infer from workspace
            ns = catalog.getNamespaceByPrefix( workspace );
            featureType.setNamespace( ns );
        }
        
        featureType.setEnabled(true);
        catalog.add( featureType );
        
        //create a layer for the feature type
        catalog.add(new CatalogBuilder(catalog).buildLayer(featureType));
        saveCatalog();
        
        LOGGER.info( "POST feature type" + dataStore + "," + featureType.getName() );
        return featureType.getName();
    }

    @Override
    public boolean allowPut() {
        return getAttribute("featuretype") != null;
    }

    @Override
    protected void handleObjectPut(Object object) throws Exception {
        FeatureTypeInfo ft = (FeatureTypeInfo) object;
        
        String workspace = getAttribute("workspace");
        String datastore = getAttribute("datastore");
        String featuretype = getAttribute("featuretype");
        
        DataStoreInfo ds = catalog.getDataStoreByName(workspace, datastore);
        FeatureTypeInfo original = catalog.getFeatureTypeByDataStore( ds,  featuretype );
        new CatalogBuilder(catalog).updateFeatureType(original,ft);
        catalog.save( original );
        
        LOGGER.info( "PUT feature type" + datastore + "," + featuretype );
        saveCatalog();
    }
    
    @Override
    public boolean allowDelete() {
        return getAttribute("featuretype") != null;
    }
    
    @Override
    public void handleObjectDelete() throws Exception {
        String workspace = getAttribute("workspace");
        String datastore = getAttribute("datastore");
        String featuretype = getAttribute("featuretype");
        
        DataStoreInfo ds = catalog.getDataStoreByName(workspace, datastore);
        FeatureTypeInfo ft = catalog.getFeatureTypeByDataStore( ds,  featuretype );
        catalog.remove( ft );
        
        saveCatalog();
        
        LOGGER.info( "DELETE feature type" + datastore + "," + featuretype );
    }

}
