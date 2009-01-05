/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContext;

import org.geoserver.feature.FeatureSourceUtils;
import org.geoserver.rest.RestletException;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.FeatureSource;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.config.GlobalConfig;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoServer;
import org.vfny.geoserver.util.DataStoreUtils;

import com.vividsolutions.jts.geom.Envelope;

public class DataStoreFileResource extends Resource{
    private DataConfig myDataConfig;
    private Data myData;
    private GeoServer myGeoServer;
    private GlobalConfig myGlobalConfig; 
    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geoserver.community");

    /**
     * A map from .xxx file extensions to datastorefactory id's.
     * This will probably eventually be a map from .xxx file extensions
     * to instances of some class that knows how to autoconfigure datastores.
     * But you know, baby steps.
     */
    private static Map<String, String> myFormats = new HashMap<String, String>();
    static {
    	myFormats.put("shp", "Shapefile");
    }
    
    private DataStoreFileResource(){
    }

    public DataStoreFileResource(Data data, DataConfig dataConfig, GeoServer gs, GlobalConfig gc) {
        this();
        setData(data);
        setDataConfig(dataConfig);
        setGeoServer(gs);
        setGlobalConfig(gc);
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

    public void setGeoServer(GeoServer geoserver) {
        myGeoServer = geoserver;
    }

    public GeoServer getGeoServer(){
        return myGeoServer;
    }

    public void setGlobalConfig(GlobalConfig gc){
        myGlobalConfig = gc;
    }

    public GlobalConfig getGlobalConfig(){
        return myGlobalConfig;
    }

    public boolean allowGet(){
        return true;
    }

    public void handleGet(){
        String storeName = (String)getRequest().getAttributes().get("folder");

        DataStoreConfig dsc = (DataStoreConfig)getDataConfig().getDataStores().get(storeName);

        if (dsc == null){
            getResponse().setEntity(new StringRepresentation("Giving up because datastore " + storeName + " does not exist.", MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
            return;
        }

        getResponse().setEntity(new StringRepresentation("Handling GET on a DataStoreFileResource", MediaType.TEXT_PLAIN));
        getResponse().setStatus(Status.SUCCESS_OK);
    }

    public boolean allowPut(){
        return true;
    }

    public synchronized void handlePut(){
        String storeName = (String)getRequest().getAttributes().get("folder");
        String extension = (String)getRequest().getAttributes().get("type");
        String format = myFormats.get(extension);

        if(LOGGER.isLoggable(Level.INFO))
        		LOGGER.info("Shapefile PUTted, mimetype was " + getRequest().getEntity().getMediaType());

        getResponse().setStatus(Status.SUCCESS_ACCEPTED);
        Form form = getRequest().getResourceRef().getQueryAsForm();

        if (format == null){
        	final StringBuilder builder= new StringBuilder("Unrecognized extension: ").append(extension);
        	final String message=builder.toString();
        	if(LOGGER.isLoggable(Level.SEVERE))
        		LOGGER.severe(message);
            getResponse().setEntity(new StringRepresentation(message, MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            return;
        }

        File uploadedFile = null;
        try {
        	String method = (String) getRequest().getAttributes().get("method");
        	if (method != null && method.equalsIgnoreCase("file"))
        		uploadedFile = RESTUtils.handleBinUpload(storeName, extension, getRequest());
        	else if (method != null && method.equalsIgnoreCase("url"))
        		uploadedFile = RESTUtils.handleURLUpload(storeName, extension, getRequest());
        	else{
            	final StringBuilder builder= new StringBuilder("Unrecognized upload method: ").append(method);
            	final String message=builder.toString();        		
            	if(LOGGER.isLoggable(Level.SEVERE))
            		LOGGER.severe(message);
                getResponse().setEntity(new StringRepresentation(message, MediaType.TEXT_PLAIN));
                getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);    
                return;
        	}
        } catch (Throwable t) {
        	if(LOGGER.isLoggable(Level.SEVERE))
        		LOGGER.log(Level.SEVERE,t.getLocalizedMessage(),t);
            getResponse().setEntity(new StringRepresentation("Error while storing uploaded file: " + t, MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }        
        
        DataStoreConfig dsc = (DataStoreConfig)myDataConfig.getDataStores().get(storeName);

        if (dsc == null){
            dsc = new DataStoreConfig(storeName, format);
            myDataConfig.addDataStore(dsc);
            dsc = (DataStoreConfig)myDataConfig.getDataStore(storeName);
            dsc.setEnabled(true);
            if (form.getFirst("namespace") != null)
                dsc.setNameSpaceId(form.getFirstValue("namespace"));
            else
                dsc.setNameSpaceId(myDataConfig.getDefaultNameSpace().getPrefix());
            dsc.setAbstract("Autoconfigured by RESTConfig"); // TODO: something better exists, I hope
            
            DataStoreFactorySpi factory = dsc.getFactory();
            Param[] parameters = factory.getParametersInfo();
            Map<String, Serializable> connectionParameters = new HashMap<String, Serializable>();
            for (int i = 0; i < parameters.length; i++){
                Param p = parameters[i];
                if (p.required){
                    connectionParameters.put(p.key, (Serializable) p.sample); // TODO: would be nice to do better here as well
                }
            }

            if (format.equals("Shapefile")){
                final File outDir=RESTUtils.unpackZippedDataset(storeName, uploadedFile);
                try{
                    connectionParameters.put("url", outDir.toURL() + "/" + storeName + ".shp");
                } catch (Exception e) {
                    throw new RestletException(
                            "Malformed url when autoconfiguring shapefile.", 
                            Status.SERVER_ERROR_INTERNAL
                        );
                }
           
                if(outDir==null) {
	            	if(LOGGER.isLoggable(Level.SEVERE))
	            		LOGGER.severe("Failure while setting up datastore: Unable to unzip zipped dataset");
	                getResponse().setEntity(new StringRepresentation("Failure while setting up datastore: Unable to unzip zipped dataset"));
	                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
	                return;
                }
                
            }

            if (factory.canProcess(connectionParameters)){
            	if(LOGGER.isLoggable(Level.INFO))
            		LOGGER.info("Params look okay to me.");
            } else {
            	if(LOGGER.isLoggable(Level.SEVERE))
            		LOGGER.severe("Couldn't handle params.");
                getResponse().setEntity(new StringRepresentation("Couldn't handle params.",MediaType.TEXT_PLAIN));
                getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
                return;            	
            }

            dsc.setConnectionParams(connectionParameters);
        } else {
        	if(LOGGER.isLoggable(Level.INFO))
        		LOGGER.info("Not autoconfiguring since there's already a datastore here");
        }

        myDataConfig.addDataStore(dsc); 

        try{
            Map params = new HashMap(dsc.getConnectionParams());
            DataStore theData = DataStoreUtils.acquireDataStore(dsc.getConnectionParams(), (ServletContext)null);

            String[] typeNames = theData.getTypeNames();
            if (typeNames.length == 1){
            	if(LOGGER.isLoggable(Level.INFO))
            		LOGGER.info(new StringBuilder("Auto-configuring featuretype: ").append(storeName).append(":").append(typeNames[0]).toString());
                myDataConfig.addFeatureType(storeName + ":" + typeNames[0], autoConfigure(theData, storeName, typeNames[0]));
            }
        } catch (Exception e){
        	if(LOGGER.isLoggable(Level.SEVERE))
        		LOGGER.log(Level.SEVERE,e.getLocalizedMessage(),e);
            getResponse().setEntity(new StringRepresentation("Failure while setting up datastore: " + e,MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
            
        }

        myData.load(myDataConfig.toDTO());
        try{
        	RESTUtils.saveConfiguration(getDataConfig(), getData());
            RESTUtils.reloadConfiguration();
        } catch (Exception e){
        	if(LOGGER.isLoggable(Level.INFO))
        		LOGGER.log(Level.INFO,e.getLocalizedMessage(),e);
            getResponse().setEntity(new StringRepresentation("Failure while saving configuration: " + e, MediaType.TEXT_PLAIN));
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
            return;
        }

        getResponse().setEntity(new StringRepresentation("Handling PUT on a DataStoreFileResource", MediaType.TEXT_PLAIN));
        getResponse().setStatus(Status.SUCCESS_OK);
    }


    static FeatureTypeConfig autoConfigure(
            DataStore store, 
            String storeName, 
            String featureTypeName
            ) throws Exception{
        FeatureTypeConfig ftc = 
            new FeatureTypeConfig(storeName, store.getSchema(featureTypeName), true);

        ftc.setDefaultStyle("polygon");

        FeatureSource<SimpleFeatureType, SimpleFeature> source =
            store.getFeatureSource(featureTypeName);

        CoordinateReferenceSystem crs = source.getSchema().getCoordinateReferenceSystem();
        if(LOGGER.isLoggable(Level.INFO))
    		LOGGER.info("Trying to autoconfigure " + featureTypeName + "; found CRS " + crs);
        String s = CRS.lookupIdentifier(crs, true);
        if (s == null){
            ftc.setSRS(4326); // TODO: Don't be so lame.
        } else if (s.indexOf(':') != -1) {
            ftc.setSRS(Integer.valueOf(s.substring(s.indexOf(':') + 1)));
        } else {
            ftc.setSRS(Integer.valueOf(s));
        }

        Envelope latLonBbox = FeatureSourceUtils.getBoundingBoxEnvelope(source);
        if (latLonBbox.isNull()){
            latLonBbox = new Envelope(-180, 180, -90, 90);
        }

        ftc.setLatLongBBox(latLonBbox);
        Envelope nativeBBox = RESTUtils.convertBBoxFromLatLon(latLonBbox, "EPSG:" + ftc.getSRS());
        ftc.setNativeBBox(nativeBBox);

        return ftc;
    }


    
	/**
	 * @return the myFormats
	 */
	public static Map<String, String> getAllowedFormats() {
		return myFormats;
	}

}
