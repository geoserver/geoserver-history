/* Copyright (c) 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.restconfig;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.geoserver.feature.FeatureSourceUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.FeatureSource;
import org.geotools.data.DataStoreFactorySpi.Param;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Status;
import org.restlet.resource.Resource;
import org.restlet.resource.StringRepresentation;
import org.vfny.geoserver.config.DataConfig;
import org.vfny.geoserver.config.DataStoreConfig;
import org.vfny.geoserver.config.FeatureTypeConfig;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.Data;
import org.vfny.geoserver.global.GeoserverDataDirectory;
import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.xml.XMLConfigReader;
import org.vfny.geoserver.global.xml.XMLConfigWriter;
import org.vfny.geoserver.util.DataStoreUtils;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class DataStoreFileResource extends Resource {
	
	private DataConfig myDataConfig;
	private Data myData;
	
	protected static Logger LOG = org.geotools.util.logging.Logging
			.getLogger("org.geoserver.community");

	
	/**
	 * A map from .xxx file extensions to datastorefactory id's. This will
	 * probably eventually be a map from .xxx file extensions to instances of
	 * some class that knows how to autoconfigure datastores. But you know, baby
	 * steps.
	 */
	
	private Map myFormats;

	public DataStoreFileResource() {
		myFormats = new HashMap();
		myFormats.put("shp", "Shapefile");
	}

	public DataStoreFileResource(Data data, DataConfig dataConfig) {
		this();
		setData(data);
		setDataConfig(dataConfig);
	}

	public void setDataConfig(DataConfig dc) {
		myDataConfig = dc;
	}

	public DataConfig getDataConfig() {
		return myDataConfig;
	}

	public void setData(Data d) {
		myData = d;
	}

	public Data getData() {
		return myData;
	}

	public boolean allowGet() {
		return true;
	}

	public void handleGet() {
		String storeName = (String) getRequest().getAttributes().get("datastore");

		DataStoreConfig dsc = (DataStoreConfig) getDataConfig().getDataStores().get(storeName);

		if (dsc == null) {
			getResponse().setEntity(
					new StringRepresentation("Giving up because datastore "
							+ storeName + " does not exist.",
							MediaType.TEXT_PLAIN));
			getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
			return;
		}

		getResponse().setEntity(
				new StringRepresentation(
						"Handling GET on a DataStoreFileResource",
						MediaType.TEXT_PLAIN));
		getResponse().setStatus(Status.SUCCESS_OK);
	}

	public boolean allowPut() {
		return true;
	}

	public void handlePut() {
		String storeName = (String) getRequest().getAttributes().get("datastore");
		String extension = (String) getRequest().getAttributes().get("type");
		String format = (String) myFormats.get(extension);

		LOG.info("Shapefile PUTted, mimetype was "
				+ getRequest().getEntity().getMediaType());

		getResponse().setStatus(Status.SUCCESS_ACCEPTED);

		if (format == null) {
			getResponse().setEntity(
					new StringRepresentation("Unrecognized extension: "
							+ extension, MediaType.TEXT_PLAIN));
			getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);

			return;
		}

		File uploadedFile = null;
		try {
			uploadedFile = handleUpload(storeName, extension, getRequest());
			
		} catch (Exception e) {
			getResponse().setEntity(
					new StringRepresentation(
							"Error while storing uploaded file: " + e,
							MediaType.TEXT_PLAIN));
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			return;
		}

		DataStoreConfig dsc = (DataStoreConfig) myDataConfig.getDataStores().get(storeName);

		if (dsc == null) {
			
			dsc = new DataStoreConfig(storeName, format);
			
			myDataConfig.addDataStore(dsc);
			
			dsc = (DataStoreConfig) myDataConfig.getDataStore(storeName);
			dsc.setEnabled(true);
			dsc.setNameSpaceId(myDataConfig.getDefaultNameSpace().getPrefix());
			
			// //////////////
			// TODO: something better exists, I hope
			// //////////////
			
			dsc.setAbstract("Autoconfigured by RESTConfig"); 		

			DataStoreFactorySpi factory = dsc.getFactory();
			Param[] parameters = factory.getParametersInfo();
			Map connectionParameters = new HashMap();

			for (int i = 0; i < parameters.length; i++) {
				Param p = parameters[i];
				if (p.required) {
					
					// /////////////////
					// TODO: would be nice to do better here as well
					// /////////////////
					
					connectionParameters.put(p.key, p.sample); 
				}
			}

			if (format.equals("Shapefile")) {
				try {
					
					unpackZippedShapefileSet(storeName, uploadedFile);
					connectionParameters.put("url", "file:data/" + storeName + "/" + storeName + ".shp");
					
				} catch (Exception mue) {
					getResponse().setEntity(
							new StringRepresentation(
									"Failure while setting up datastore: "
											+ mue, MediaType.TEXT_PLAIN));
					getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
				}
			}

			if (!factory.canProcess(connectionParameters)) {
				getResponse()
						.setEntity(
								new StringRepresentation(
										"Invalid Connection Parameters. Maybe the provided archive does not contain a valid Shapefile."));
				getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);

				return;
			}

			dsc.setConnectionParams(connectionParameters);
		}

		myDataConfig.removeDataStore(storeName);
		myDataConfig.addDataStore(dsc);

		try {
			Map params = new HashMap(dsc.getConnectionParams());

			DataStore theData = DataStoreUtils.acquireDataStore(params, null);

			String[] typeNames = theData.getTypeNames();
			final int numTypes = typeNames.length;
			for (int t = 0; t < numTypes; t++) { 
				System.out.println("Auto-configuring featuretype: " + storeName + ":" + typeNames[t]);
				myDataConfig.addFeatureType( storeName + ":" + typeNames[t], 
				  autoConfigure(theData, storeName, typeNames[t]) ); 
		    }
			 
		} catch (Exception e) {
			LOG.severe("Failure while autoconfiguring uploaded datastore of type " + format);
			LOG.severe("Exception: " + e);
		}

		myData.load(myDataConfig.toDTO());

		try {			
			saveConfiguration();
			reloadConfiguration();
			
		} catch (Exception e) {
			getResponse().setEntity(
					new StringRepresentation(
							"Failure while saving configuration: " + e,
							MediaType.TEXT_PLAIN));
			getResponse().setStatus(Status.SERVER_ERROR_INTERNAL);
			return;
		}

		getResponse().setEntity(
				new StringRepresentation(
						"Handling PUT on a DataStoreFileResource",
						MediaType.TEXT_PLAIN));
		
		getResponse().setStatus(Status.SUCCESS_OK);
		
	}

	private File handleUpload(String storeName, String extension, Request request) throws IOException, ConfigurationException {
		
		// /////////////////////////
		// TODO: don't manage the temp file manually, java can take care of it
		// /////////////////////////
		
		File dir = GeoserverDataDirectory.findCreateConfigDir("data");
		File tempFile = new File(dir, storeName + "." + extension + ".tmp");
		File newFile = new File(dir, storeName + "." + extension);
		InputStream in = new BufferedInputStream(request.getEntity().getStream());
		OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));

		copy(in, out);

		tempFile.renameTo(newFile);
		return newFile;
		
	}

	private void unpackZippedShapefileSet(String storeName, File zipFile)throws Exception {
		
		ZipFile archive = new ZipFile(zipFile);
		Enumeration entries = archive.entries();
		
		File outputDirectory = new File(GeoserverDataDirectory.findCreateConfigDir("data"), storeName);
		
		if (!outputDirectory.exists()) {
			outputDirectory.mkdir();
		}
		
		while (entries.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) entries.nextElement();

			if (!entry.isDirectory()) {
				String name = entry.getName();
				String extension = null;
				int extensionStartIndex = name.lastIndexOf(".");
				if (extensionStartIndex != -1) {
					extension = name.substring(extensionStartIndex + 1);
				}

				// ///////////////////////
				// TODO: make sure only 'good' stuff gets uploaded
				// ///////////////////////
				
				if (extension != null && extension.length() > 0) {
					InputStream in = new BufferedInputStream(archive.getInputStream(entry));
					File outFile = new File(outputDirectory, storeName + "." + extension);
					OutputStream out = new BufferedOutputStream(new FileOutputStream(outFile));

					copy(in, out);
				}
			}
		}
		archive.close();
		zipFile.delete();
		
		File[] contents = outputDirectory.listFiles();
		
		for (int i = 0; i < contents.length; i++) {
			System.out.println("Got " + contents[i] + " from the zip archive.");
		}
		
	}

	private void copy(InputStream in, OutputStream out) throws IOException {
		
		byte[] data = new byte[1024];		
		int amountRead = 0;
		
		while ((amountRead = in.read(data)) != -1) {
			out.write(data, 0, amountRead);
		}
		
		out.flush();
		out.close();
	}

	private void saveConfiguration() throws ConfigurationException {
		
		getData().load(getDataConfig().toDTO());
		XMLConfigWriter.store((DataDTO) getData().toDTO(),
				GeoserverDataDirectory.getGeoserverDataDirectory());
		
	}

	private void reloadConfiguration() throws Exception {
		
		XMLConfigReader reader = new XMLConfigReader(GeoserverDataDirectory
				.getGeoserverDataDirectory(), null);
		
		if (reader.isInitialized()) {
			getData().load(reader.getData());
		}
		
	}

	private FeatureTypeConfig autoConfigure(DataStore store, String storeName, String featureTypeName) throws Exception{
        
    	try{
    		FeatureTypeConfig ftc = new FeatureTypeConfig(
                    storeName,
                    store.getSchema(featureTypeName),
                    true
                    );

            FeatureSource source = store.getFeatureSource(featureTypeName);

            CoordinateReferenceSystem crs = null;
            if (source.getSchema() != null && source.getBounds() != null) {
            	crs = source.getSchema().getDefaultGeometry().getCoordinateSystem();
            } else {
            	crs = source.getFeatures().getSchema().getDefaultGeometry().getCoordinateSystem();
            }

            if (source == null) {
            	crs = CRS.decode("EPSG:4326", true);
            }
            
            LOG.info("Trying to autoconfigure " + featureTypeName + "; found CRS " + crs);
            String s = CRS.lookupIdentifier(crs, true);
            if (s == null){
                ftc.setSRS(4326); // TODO: Don't be so lame.
            } else if (s.indexOf(':') != -1) {
                ftc.setSRS(Integer.parseInt(s.substring(s.indexOf(':') + 1)));
            } else {
                ftc.setSRS(Integer.parseInt(s));
            }

            Envelope latLonBbox = FeatureSourceUtils.getBoundingBoxEnvelope(source);
            if (latLonBbox.isNull()){
                latLonBbox = new Envelope(-180, 180, -90, 90);
            }

            ftc.setLatLongBBox(latLonBbox);
            Envelope nativeBBox = convertBBoxFromLatLon(latLonBbox, "EPSG:" + ftc.getSRS());
            ftc.setNativeBBox(nativeBBox);
		
    		Class geomType = source.getSchema().getDefaultGeometry().getBinding();
    		
    		if (geomType == LineString.class || geomType == MultiLineString.class) {
    			ftc.setDefaultStyle("line");
    		}

    		if (geomType == Point.class || geomType == MultiPoint.class) {
    			ftc.setDefaultStyle("point");
    		}

    		if (geomType == Polygon.class || geomType == MultiPolygon.class) {
    			ftc.setDefaultStyle("polygon");
    		}
    		
    		return ftc;

    	}catch (IOException e) {
    		getResponse().setEntity(new StringRepresentation("DataStore - IOException occurred: " + e.getLocalizedMessage()));
    		getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                   
    		return null;
    	} catch (NoSuchAuthorityCodeException e) {
    		getResponse().setEntity(new StringRepresentation("CRS - NoSuchAuthorityCodeException occurred: " + e.getLocalizedMessage()));
    		getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
                   
    		return null;
    	} catch (FactoryException e) {
    		getResponse().setEntity(new StringRepresentation("CRS - FactoryException occurred: " + e.getLocalizedMessage()));
    		getResponse().setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
            
    		return null;
    	}   
    }
	
	/**
     * Convert a bounding box in latitude/longitude coordinates to another CRS, specified by name.
     * @param latLonBbox the latitude/longitude bounding box
     * @param crsName the name of the CRS to which it should be converted
     * @return the converted bounding box
     * @throws Exception if anything goes wrong
     */
    private Envelope convertBBoxFromLatLon(Envelope latLonBbox, String crsName) throws Exception {
            CoordinateReferenceSystem latLon = CRS.decode("EPSG:4326");
            CoordinateReferenceSystem nativeCRS = CRS.decode(crsName);
            Envelope env = null;

            if (!CRS.equalsIgnoreMetadata(latLon, nativeCRS)) {
                MathTransform xform = CRS.findMathTransform(latLon, nativeCRS, true);
                env = JTS.transform(latLonBbox, null, xform, 10); //convert data bbox to lat/long
            } else {
                env = latLonBbox;
            }

            return env;
    }
}
