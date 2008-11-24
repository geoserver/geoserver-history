/* Copyright (c) 2008 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerLoader;
import org.geoserver.platform.GeoServerExtensions;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.restlet.data.Request;
import org.vfny.geoserver.config.CoverageStoreConfig;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.xml.XMLConfigWriter;

import com.vividsolutions.jts.geom.Envelope;
/**
 * Simple helper class with a few utilities which should be specific to the REST interfaces.
 * 
 * @author Simone Giannecchini, GeoSolutions.
 *
 */
class RESTUtils {
	


	/**
	 * Singleton.
	 */
	private RESTUtils() {
		
	}


	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RESTUtils.class);
	
	/**
	 * Reload the configuration resetting all pending changes.ù
	 */
	static void reloadConfiguration(){
	    synchronized (GeoServer.CONFIGURATION_LOCK) {
    		GeoServerLoader loader = GeoServerExtensions.bean( GeoServerLoader.class );
    	    try {
    	        loader.reload();
    	    } 
    	    catch (Exception e) {
    	        throw new RuntimeException( e );
    	    }
	    }
    
//        // Update Config
//        gs.init();
//        config.update(gs.toDTO());
//        dataConfig.update(data.toDTO());
	}


	/** 
	 * Apply and persist the current changes to the GeoServer configuration.
	 * 
	 * @param dataConfig
	 * @param data
	 * @throws ConfigurationException
	 */
    static void saveConfiguration(DataConfig dataConfig, Data data) throws ConfigurationException {
        synchronized (GeoServer.CONFIGURATION_LOCK) {
    		data.load(dataConfig.toDTO());
    	    XMLConfigWriter.store((DataDTO) data.toDTO(), GeoserverDataDirectory.getGeoserverDataDirectory());
        }
	}
    
    /**
     * Convert a bounding box in latitude/longitude coordinates to another CRS, specified by name.
     * @param latLonBbox the latitude/longitude bounding box
     * @param crsName the name of the CRS to which it should be converted
     * @return the converted bounding box
     * @throws Exception if anything goes wrong
     */
     static ReferencedEnvelope convertBBoxFromLatLon(Envelope latLonBbox, String crsName) throws Exception {
            CoordinateReferenceSystem latLon = CRS.decode("EPSG:4326");
            CoordinateReferenceSystem nativeCRS = CRS.decode(crsName);
            Envelope env = null;

            if (!CRS.equalsIgnoreMetadata(latLon, nativeCRS)) {
                MathTransform xform = CRS.findMathTransform(latLon, nativeCRS, true);
                env = JTS.transform(latLonBbox, null, xform, 10); //convert data bbox to lat/long
            } else {
                env = latLonBbox;
            }
            final ReferencedEnvelope retValue = new ReferencedEnvelope(env,latLon);
            return retValue;
    }


     /**
      * Converts a {@link FeatureTypeConfig} into a map.
      * @param myFTC the input {@link FeatureTypeConfig}.
      * 
      * @return a {@link Map} instance that represents the input {@link FeatureTypeConfig}
      */
	 static Map getMap(FeatureTypeConfig myFTC) {
	    Map m = new HashMap();
	
	    m.put("Style", myFTC.getDefaultStyle());
	    m.put("AdditionalStyles", myFTC.getStyles());
	    m.put("SRS", myFTC.getSRS());
	    m.put("SRSHandling", getSRSHandling(myFTC));
	    m.put("Title", myFTC.getTitle());
	    m.put("BBox", getBoundingBox(myFTC)); 
	    m.put("Keywords", getKeywords(myFTC));
	    m.put("Abstract", myFTC.getAbstract());
	    m.put("WMSPath", myFTC.getWmsPath());
	    m.put("MetadataLinks", getMetadataLinks(myFTC));
	    m.put("CachingEnabled", Boolean.toString(myFTC.isCachingEnabled()));
	    m.put("CacheTime", (myFTC.isCachingEnabled() ? Integer.valueOf(myFTC.getCacheMaxAge()) : null));
	    m.put("SchemaBase", myFTC.getSchemaBase());
	
	    return m;
	}


	 static String getSRSHandling(FeatureTypeConfig myFTC){
	    try{
	        return (new String[]{"Force","Reproject","Ignore"})[myFTC.getSRSHandling()];
	    } catch (Exception e){
	        return "Ignore";
	    }
	}


	static List getBoundingBox(FeatureTypeConfig myFTC){
	    List l = new ArrayList();
	    Envelope e = myFTC.getLatLongBBox();
	    l.add(e.getMinX());
	    l.add(e.getMaxX());
	    l.add(e.getMinY());
	    l.add(e.getMaxY());
	    return l;
	}


	static List getKeywords(FeatureTypeConfig myFTC){
	    List l = new ArrayList();
	    l.addAll(myFTC.getKeywords());
	    return l;
	}


	 static List getMetadataLinks(FeatureTypeConfig myFTC){
	    List l = new ArrayList();
	    l.addAll(myFTC.getMetadataLinks());
	    return l;
	}


	static Map getVirtualFolderMap(DataConfig dc){
	    Map folders = new HashMap();
	    Iterator it =  dc.getDataStores().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry entry = (Map.Entry)it.next();
	        DataStoreConfig dsc = (DataStoreConfig)entry.getValue();
	        if (FILE_DS_TYPES.contains(dsc.getFactory().getDisplayName())
	                && dsc.getConnectionParams().get("url") != null) {
	            String value = dsc.getConnectionParams().get("url").toString();
	            value = findParentPath(value);
	            Map files = (Map)folders.get(value);
	            files = (files == null ? new HashMap() : files);
	            files.put(dsc.getId(), dsc);
	            folders.put(value, files);
	        } else {
	            folders.put(entry.getKey(), entry.getValue());
	        }
	    }
	
	    it = dc.getDataFormats().entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry entry = (Map.Entry)it.next();
	        CoverageStoreConfig csc = (CoverageStoreConfig)entry.getValue();
	        // if (FILE_CS_TYPES.contains(csc.getFactory().getName());
	        String path = findParentPath(csc.getUrl());
	        Map files = (Map)folders.get(path);
	        files = (files == null ? new HashMap() : files);
	        files.put(csc.getId(), csc);
	        folders.put(path, files);
	    }
	
	    return folders;
	}


	private static Set FILE_CS_TYPES;
	static Set FILE_DS_TYPES;


	static {
	    FILE_DS_TYPES = new HashSet<String>();
	    FILE_DS_TYPES.add("Shapefile");
	
	    FILE_CS_TYPES = new HashSet<String>();
	    FILE_CS_TYPES.add("GeoTIFF"); // TODO: Flesh out these lists properly.  Can we do it programmatically instead of hardcoding?
	}


	static String findParentPath(String value){
	    int lastSlash = value.lastIndexOf("/");
	    value = (lastSlash == -1) ? value : value.substring(0, lastSlash);
	    lastSlash = value.lastIndexOf("/");
	    value = value.substring(lastSlash + 1);
	    return value;
	}


	/**
	 * This function gets the stream of the request to copy it into a file.
	 * 
	 * @param datasetName
	 * @param extension
	 * @param request
	 * @return File
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	static File handleBinUpload(String datasetName, String extension,
	        Request request) throws IOException, ConfigurationException {
	
	    final File dir = GeoserverDataDirectory.findCreateConfigDir("data");
	    final File newFile = new File(dir, datasetName + "." + extension);
	    final ReadableByteChannel source =request.getEntity().getChannel();
	    final FileChannel outputChannel = IOUtils.getOuputChannel(newFile);
	    IOUtils.copyChannel(1024*1024, source,outputChannel );
	    IOUtils.closeQuietly(source);
	    IOUtils.closeQuietly(outputChannel);
	    return newFile;
	}


	/**
	 * Handles the upload of a dataset using the URL method.
	 * 
	 * @param datasetName the name of the uploaded dataset.
	 * @param extension the extension of the uploaded dataset.
	 * @param request the incoming request.
	 * @return a {@link File} that points to the final uploaded dataset.
	 * 
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	static File handleURLUpload(String datasetName, String extension, Request request) throws IOException, ConfigurationException {
	
		////
		//
		// Get the dir where to write and create a file there
		//
		////
	    File dir = GeoserverDataDirectory.findCreateConfigDir("data");
	    //this may exists already, but we don't fail here since 
	    //it might be old and unused, if needed we fail later while copying
	    File newFile  = new File(dir, datasetName + "." + extension);
	    
	    //get the URL for this file to upload
	    final InputStream inStream=request.getEntity().getStream();
	    final String stringURL=IOUtils.getStringFromStream(inStream);
	    final URL fileURL=new URL(stringURL);
	    
		////
		//
		// Now do the real upload
		//
		////
	    //check if it is a file
	    final File inputFile= IOUtils.URLToFile(fileURL);
	    if(inputFile!=null&&inputFile.exists()&&inputFile.canRead())
	    {
	    	IOUtils.copyFile(inputFile, newFile);
	    	
	    }
	    else {
	        final InputStream inputStream =  fileURL.openStream();
	        final OutputStream outStream = new FileOutputStream(newFile);
	        IOUtils.copyStream(inputStream, outStream, true, true);
	    }
	    return newFile;
	}
	

	/**
	 * Unzip a zipped dataset.
	 * 
	 * @param storeName the name of the store to handle.
	 * @param zipFile the zipped archive 
	 * @return null if the zip file does not point to a valid zip file, the output directory otherwise.
	 */
    static File unpackZippedDataset(String storeName, File zipFile){
    	try {
	        ZipFile archive = new ZipFile(zipFile);
	        File outputDirectory = new File(GeoserverDataDirectory.findCreateConfigDir("data"), storeName);
	        if (!outputDirectory.exists()){
	            outputDirectory.mkdir();
	        }
	        IOUtils.inflate(archive, outputDirectory, storeName);
	        IOUtils.deleteFile(zipFile);
	        return outputDirectory;
    	}catch (Throwable t) {
			if(LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE,t.getLocalizedMessage(),t);
		}
    	return null;
    }

}
