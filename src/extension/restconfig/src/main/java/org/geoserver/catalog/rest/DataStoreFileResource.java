/* Copyright (c) 2001 - 2009 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.catalog.rest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.geoserver.catalog.Catalog;
import org.geoserver.catalog.CatalogBuilder;
import org.geoserver.catalog.DataStoreInfo;
import org.geoserver.catalog.FeatureTypeInfo;
import org.geoserver.catalog.NamespaceInfo;
import org.geoserver.rest.RestletException;
import org.geoserver.rest.format.StreamDataFormat;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.FeatureSource;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;

public class DataStoreFileResource extends StoreFileResource {

    DataStoreFactorySpi factory;
    
    public DataStoreFileResource( Request request, Response response, DataStoreFactorySpi factory, Catalog catalog ) {
        super( request, response, catalog );
        this.factory = factory;
    }
    
    @Override
    public void handleGet() {
        String workspace = (String)getRequest().getAttributes().get("workspace");
        String datastore = (String)getRequest().getAttributes().get("datastore");
        String format = (String)getRequest().getAttributes().get("format");

        //find the directory containig the files
        File directory;
        try {
            directory = catalog.getResourceLoader().find( "data/" + datastore );
        } 
        catch (IOException e) {
            throw new RestletException( e.getMessage(), Status.SERVER_ERROR_INTERNAL, e );
        }
        
        if ( directory == null || !directory.exists() || !directory.isDirectory() ) {
            throw new RestletException( "No files for datastore " + datastore, Status.CLIENT_ERROR_NOT_FOUND );
        }
        
        //zip up all the files in the directory
        StreamDataFormat fmt = new StreamDataFormat(MediaType.APPLICATION_ZIP) {

            @Override
            protected Object read(InputStream in) throws IOException {
                return null;
            }

            @Override
            protected void write(Object object, OutputStream out)
                    throws IOException {
                ZipOutputStream zout = new ZipOutputStream( out );
            
                File directory = (File) object;
                for ( File f : directory.listFiles() ) {
                    ZipEntry entry = new ZipEntry( f.getName() );
                    zout.putNextEntry(entry);
                    IOUtils.copy( new FileInputStream( f ), zout );
                    zout.closeEntry();
                }
                zout.flush();
                zout.close();
            }
        };
        getResponse().setEntity( fmt.toRepresentation( directory ) );
    }
    
    @Override
    public void handlePut() {
        String workspace = (String)getRequest().getAttributes().get("workspace");
        String datastore = (String)getRequest().getAttributes().get("datastore");
        String format = (String)getRequest().getAttributes().get("format");

        getResponse().setStatus(Status.SUCCESS_ACCEPTED);
        Form form = getRequest().getResourceRef().getQueryAsForm();

        //get the directory to put the file into 
        //TODO: add a method createDirectory(String...) so as not to specify the file seperator
        File directory;
        try {
            directory = catalog.getResourceLoader().findOrCreateDirectory("data/" + datastore );
        } 
        catch (IOException e) {
            throw new RestletException( e.getMessage(), Status.SERVER_ERROR_INTERNAL, e );
        }
        
        File uploadedFile = handleFileUpload(datastore, format, directory);

        //create a builder to help build catalog objects
        CatalogBuilder builder = new CatalogBuilder(catalog);
        builder.setWorkspace( catalog.getWorkspaceByName( workspace ) );
        
        //check if the datastore already exists, if not auto configure one
        DataStoreInfo info = catalog.getDataStoreByName( datastore );
        
        boolean add = false;
        if ( info == null ) {
            LOGGER.info("Auto-configuring datastore: " + datastore);
            
            info = builder.buildDataStore( datastore );
            add = true;
        }
        else {
            LOGGER.info("Using existing datastore: " + datastore);
        }
        
        builder.setStore(info);
        
        //update the connection parameters to point to the new file
        Map connectionParameters = info.getConnectionParameters();
        for ( Param p : factory.getParametersInfo() ) {
            //the nasty url / file hack
            if ( File.class == p.type || URL.class == p.type ) {
                File f = uploadedFile;
                
                if ( "directory".equals( p.key ) ) {
                    //set the value to be the directory
                    f = directory;
                }
                
                //convert to the required type
                //TODO: use geotools converter
                Object converted = null;
                if ( URI.class.equals( p.type  ) ) {
                    converted = f.toURI();
                }
                else if ( URL.class.equals( p.type ) ) {
                    try {
                        converted = f.toURL();
                    } 
                    catch (MalformedURLException e) {
                    }
                }
                //Converters.convert( f.getAbsolutePath(), p.type );
                
                if ( converted != null ) {
                    connectionParameters.put( p.key, converted );    
                }
                else {
                    connectionParameters.put( p.key, f );
                }
                
                continue;
            }
            
            if ( p.required ) {
                try {
                    p.lookUp( connectionParameters );
                }
                catch( Exception e ) {
                    //set the sample value
                    connectionParameters.put( p.key, p.sample );
                }    
            }
        }
        
        // set the namespace uri
        NamespaceInfo namespace = catalog.getNamespaceByPrefix( info.getWorkspace().getName() );
        connectionParameters.put( "namespace", namespace.getURI() );
        
        // ensure the parameters are valid
        if ( !factory.canProcess( connectionParameters ) ) {
            //TODO: log the parameters at the debug level
            throw new RestletException( "Unable to configure datastore, bad parameters.", Status.SERVER_ERROR_INTERNAL );
        }
        
        //add or update the datastore info
        if ( add ) {
            catalog.add( info );
        }
        else {
            catalog.save( info );
        }
        
        //check configure parameter, if set to none to not try to configure
        // data feature types
        String configure = form.getFirstValue( "configure" );
        if ( "none".equalsIgnoreCase( configure ) ) {
            getResponse().setStatus( Status.SUCCESS_CREATED );
            return;
        }
        
        //load the datastore
        try {
            DataStore ds = (DataStore) info.getDataStore(null);
            
            String[] featureTypeNames = ds.getTypeNames();
            for ( int i = 0; i < featureTypeNames.length; i++ ) {
                
                //unless configure specified "all", only configure the first feature type
                if ( !"all".equalsIgnoreCase( configure ) && i > 0 ) {
                    break;
                }
                
                FeatureSource fs = ds.getFeatureSource(featureTypeNames[i]); 
                FeatureTypeInfo ftinfo = null;
                if ( add ) {
                    //auto configure the feature type as well
                    ftinfo = builder.buildFeatureType(fs);
                    builder.lookupSRS(ftinfo, true);
                    builder.setupBounds(ftinfo);
                }
                else {
                    ftinfo = catalog.getFeatureTypeByName( namespace.getPrefix(), featureTypeNames[i] );
                }
                
                //update the bounds
                ReferencedEnvelope bounds = fs.getBounds();
                ftinfo.setNativeBoundingBox( bounds );
                
                //TODO: set lat lon bounding box
                if ( add ) {
                    catalog.add( ftinfo );
                    
                    //add a layer for the feature type as well
                    catalog.add(builder.buildLayer(ftinfo));
                   
                }
                else {
                    catalog.save( ftinfo );
                }
                
                getResponse().setStatus( Status.SUCCESS_CREATED );
            }
        } 
        catch (Exception e) {
            //TODO: report a proper error code
            throw new RuntimeException ( e );
        }
    }
}
