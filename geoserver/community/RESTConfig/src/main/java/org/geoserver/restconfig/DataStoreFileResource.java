/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.util.Map;
import org.restlet.Restlet;
import org.restlet.data.Request;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.restlet.data.Status;
import org.restlet.data.MediaType;

import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFactorySpi.Param;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

public class DataStoreFileResource extends Resource{
    private DataConfig myDataConfig;
    private Data myData;

    /**
     * A map from .xxx file extensions to datastorefactory id's.
     * This will probably eventually be a map from .xxx file extensions
     * to instances of some class that knows how to autoconfigure datastores.
     * But you know, baby steps.
     */
    private Map myFormats; 

    public DataStoreFileResource() {
        myFormats = new HashMap();
        myFormats.put("shp", "Shapefile");
    }

    public void setDataConfig(DataConfig dc){
        myDataConfig = dc;
    }

    public DataConfig getDataConfig(){
        return myDataConfig;
    }

    public void setData(Data d){
        myData = d;
    }

    public Data getData(){
        return myData;
    }

    public boolean allowGet(){
        return true;
    }

    public void handleGet(){
        String storeName = 
            (String)getRequest().getAttributes().get("datastore");

        DataStoreConfig dsc = 
            (DataStoreConfig)getDataConfig().getDataStores().get(storeName);

        if (dsc == null){
            getResponse().setEntity(
                    new StringRepresentation(
                        "Giving up because datastore " + storeName + " does not exist.",
                        MediaType.TEXT_PLAIN
                        )
                    );
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return;
        }

        getResponse().setEntity(
                new StringRepresentation(
                    "Handling GET on a DataStoreFileResource",
                    MediaType.TEXT_PLAIN
                    )
                );
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    public boolean allowPut(){
        return true;
    }

    public void handlePut(){
        String storeName = (String)getRequest().getAttributes().get("datastore");
        String extension = (String)getRequest().getAttributes().get("type");
        String format = (String) myFormats.get(extension);

        if (format == null){
            getResponse().setEntity(
                    new StringRepresentation(
                        "Unrecognized extension: " + extension,
                        MediaType.TEXT_PLAIN
                        )
                    );
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                    
            return;
        }

        File uploadedFile = null;
        try {
            uploadedFile = handleUpload(storeName, extension, getRequest());
        } catch (Exception e){
            getResponse().setEntity(
                    new StringRepresentation(
                        "Error while storing uploaded file: " + e,
                        MediaType.TEXT_PLAIN
                        )
                    );
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        DataStoreConfig dsc = 
            (DataStoreConfig)myDataConfig.getDataStores().get(storeName);

        if (dsc == null){
            dsc = new DataStoreConfig(storeName, format);
            dsc.setEnabled(true);
            dsc.setNameSpaceId(myDataConfig.getDefaultNameSpace().getPrefix());
            dsc.setAbstract("Autoconfigured by RESTConfig"); // TODO: something better exists, I hope
            
            DataStoreFactorySpi factory = dsc.getFactory();
            Param[] parameters = factory.getParametersInfo();
            Map connectionParameters = new HashMap();
            for (int i = 0; i < parameters.length; i++){
                Param p = parameters[i];
                if (p.required){
                    connectionParameters.put(p.key, p.sample); // TODO: would be nice to do better here as well
                }
            }

            if (format.equals("Shapefile")){
                try{
                    connectionParameters.put("url", uploadedFile.toURL());
                } catch(MalformedURLException mue){
                    getResponse().setEntity(
                            new StringRepresentation("Failure while setting up datastore: " + mue,
                                MediaType.TEXT_PLAIN
                                )
                            );
                    getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                }
            }

            dsc.setConnectionParams(connectionParameters);
        }

        myDataConfig.addDataStore(dsc); 
        myData.load(myDataConfig.toDTO());
        try{
            saveConfiguration();
        } catch (Exception e){
            getResponse().setEntity(
                    new StringRepresentation(
                        "Failure while saving configuration: " + e,
                        MediaType.TEXT_PLAIN
                        )
                    );
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        getResponse().setEntity(
                new StringRepresentation(
                    "Handling PUT on a DataStoreFileResource",
                    MediaType.TEXT_PLAIN
                    )
                );
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    private File handleUpload(String storeName, String extension, Request request) 
        throws IOException, ConfigurationException{
            // TODO: don't manage the temp file manually, java can take care of it
            File dir = GeoserverDataDirectory.findCreateConfigDir("data");
            File tempFile = new File(dir, storeName + "." + extension + ".tmp");
            File newFile = new File(dir, storeName + "." + extension);
            BufferedReader reader = 
                new BufferedReader(
                        new InputStreamReader(
                            request.getEntity().getStream()
                            )
                        ); 
            BufferedWriter fw = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null){
                fw.write(line);
                fw.newLine();
            }

            fw.flush();
            fw.close();

            tempFile.renameTo(newFile);
            return newFile;
        }

    private void saveConfiguration() throws ConfigurationException{
        getData().load(getDataConfig().toDTO());
        XMLConfigWriter.store((DataDTO)getData().toDTO(),
        		GeoserverDataDirectory.getGeoserverDataDirectory()
        		);
    }
}
