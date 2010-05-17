/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.catalog.rest;

import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.config.util.XStreamPersister;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.DataFormat;
import org.restlet.Context;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class FeatureTypeResource extends AbstractCatalogResource {

    public FeatureTypeResource(Context context, Request request,Response response, Catalog catalog) {
        super(context, request, response, FeatureTypeInfo.class, catalog);
    }

    @Override
    protected DataFormat createHTMLFormat(Request request, Response response) {
        return new ResourceHTMLFormat(FeatureTypeInfo.class,request,response,this);
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
                return catalog.getFeatureTypeByName(ns,featureType);
            }

            throw new RestletException( "", Status.CLIENT_ERROR_NOT_FOUND );
        }

        LOGGER.fine( "GET feature type" + datastore + "," + featureType );
        DataStoreInfo ds = catalog.getDataStoreByName(workspace, datastore);
        return catalog.getFeatureTypeByDataStore( ds, featureType );
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
         
        //ensure the store matches up
        if ( featureType.getStore() != null ) {
            if ( !dataStore.equals( featureType.getStore().getName() ) ) {
                throw new RestletException( "Expected datastore " + dataStore +
                " but client specified " + featureType.getStore().getName(), Status.CLIENT_ERROR_FORBIDDEN );
            }
        }
        else {
            featureType.setStore( catalog.getDataStoreByName( workspace, dataStore ) );
        }
        
        //ensure workspace/namespace matches up
        if ( featureType.getNamespace() != null ) {
            if ( !workspace.equals( featureType.getNamespace().getPrefix() ) ) {
                throw new RestletException( "Expected workspace " + workspace +
                    " but client specified " + featureType.getNamespace().getPrefix(), Status.CLIENT_ERROR_FORBIDDEN );
            }
        }
        else {
            featureType.setNamespace( catalog.getNamespaceByPrefix( workspace ) );
        }
        featureType.setEnabled(true);
        
        CatalogBuilder cb = new CatalogBuilder(catalog);
        cb.initFeatureType( featureType );
        
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
        
        LOGGER.info( "DELETE feature type" + datastore + "," + featuretype );
    }

    @Override
    protected void configurePersister(XStreamPersister persister, DataFormat format) {
        persister.setCallback( new XStreamPersister.Callback() {
            @Override
            protected void postEncodeReference(Object obj, String ref,
                    HierarchicalStreamWriter writer, MarshallingContext context) {
                if ( obj instanceof NamespaceInfo ) {
                    NamespaceInfo ns = (NamespaceInfo) obj;
                    encodeLink( "/namespaces/" + ns.getPrefix(), writer);
                }
                if ( obj instanceof DataStoreInfo ) {
                    DataStoreInfo ds = (DataStoreInfo) obj;
                    encodeLink( "/workspaces/" + ds.getWorkspace().getName() + "/datastores/" + 
                        ds.getName(), writer );
                }
            }
        });
    }
}
