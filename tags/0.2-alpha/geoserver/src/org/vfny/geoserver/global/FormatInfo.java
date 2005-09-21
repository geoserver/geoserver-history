/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.opengis.coverage.grid.*;
import org.geotools.data.coverage.grid.GridFormatFinder;
import org.vfny.geoserver.global.dto.FormatInfoDTO;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Logger;


/**
 * This is the configuration iformation for one DataStore. This class can also
 * generate real datastores.
 *
 * @author Gabriel Roldán
 * @author dzwiers
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss_ml@tiscali.it) $ (last modification)
 * @version $Id: DataStoreInfo.java,v 1.14 2004/06/26 19:51:24 jive Exp $
 */
public class FormatInfo extends GlobalLayerSupertype {
	/** for logging */
	private static final Logger LOGGER = Logger.getLogger(
	"org.vfny.geoserver.config");
	
	/** FormatInfo we are representing */
	private Format format = null;
	
	/** ref to the parent class's collection */
	private Data data;
	private String id;
	private String type;
	private String url;
	private boolean enabled;
	private String title;
	private String _abstract;
	private Map parameters;
	
	/** Storage for metadata */
	private Map meta;
	
	/**
	 * Directory associated with this DataStore.
	 * 
	 * <p>
	 * This directory may be used for File based relative paths.
	 * </p>
	 */
	File baseDir;
	
	/**
	 * URL associated with this DataStore.
	 * 
	 * <p>
	 * This directory may be used for URL based relative paths.
	 * </p>
	 */
	URL baseURL;
	
	/**
	 * DataStoreInfo constructor.
	 * 
	 * <p>
	 * Stores the specified data for later use.
	 * </p>
	 *
	 * @param config DataStoreInfoDTO the current configuration to use.
	 * @param data Data a ref to use later to look up related informtion
	 */
	public FormatInfo(FormatInfoDTO config, Data data) {
		this.data = data;
		meta = new HashMap();
		
		parameters = config.getParameters();
		enabled = config.isEnabled();
		id = config.getId();
		//nameSpaceId = config.getNameSpaceId();
		type = config.getType();
		url = config.getUrl();
		title = config.getTitle();
		_abstract = config.getAbstract();
	}
	
	/**
	 * toDTO purpose.
	 * 
	 * <p>
	 * This method is package visible only, and returns a reference to the
	 * GeoServerDTO. This method is unsafe, and should only be used with
	 * extreme caution.
	 * </p>
	 *
	 * @return DataStoreInfoDTO the generated object
	 */
	Object toDTO() {
		FormatInfoDTO dto = new FormatInfoDTO();
		dto.setAbstract(_abstract);
		dto.setParameters(parameters);
		dto.setEnabled(enabled);
		dto.setId(id);
		//dto.setNameSpaceId(nameSpaceId);
		dto.setType(type);
		dto.setUrl(url);
		dto.setTitle(title);
		
		return dto;
	}
	
	/**
	 * getId purpose.
	 * 
	 * <p>
	 * Returns the dataStore's id.
	 * </p>
	 *
	 * @return String the id.
	 */
	public String getId() {
		return id;
	}
	
	protected Map getParams() {
		Map params = new HashMap(parameters);
		//params.put("namespace", getNameSpace().getURI());
		return getParams(params, data.getBaseDir().toString());
	}
	
	/**
	 * Get Connect params.
	 * 
	 * <p>
	 * This is used to smooth any relative path kind of issues for any file
	 * URLS. This code should be expanded to deal with any other context
	 * sensitve isses dataStores tend to have.
	 * </p>
	 *
	 * @return DOCUMENT ME!
	 *
	 * @task REVISIT: cache these?
	 */
	
	public static Map getParams(Map m, String baseDir) {
		Map params = Collections.synchronizedMap(new HashMap(m));
		
		for (Iterator i = params.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			String key = (String) entry.getKey();
			Object value = entry.getValue();
			
			try {
				if ("url".equals(key) && value instanceof String) {
					String path = (String) value;
					LOGGER.info("in string url");
					if (path.startsWith("file:data/")) {
						path = path.substring(5); // remove 'file:' prefix
						
						File file = new File(baseDir, path);
						entry.setValue(file.toURL().toExternalForm());
					}
					//Not sure about this
				} else if (value instanceof URL
						&& ((URL) value).getProtocol().equals("file")) {
					LOGGER.info("in URL url");
					URL url = (URL) value;
					String path = url.getPath();
					LOGGER.info("path is " + path);
					if (path.startsWith("data/")){
						File file = new File(baseDir, path);
						entry.setValue(file.toURL());
					}
				} /*else if ("dbtype".equals(key) && value instanceof String) {
				String val = (String) value;
				
				if ((val != null) && val.equals("postgis")) {
				if (!params.containsKey("charset")) {
				params.put("charset",
				data.getGeoServer().getCharSet().toString());
				}
				}
				} */
			} catch (MalformedURLException ignore) {
				// ignore attempt to fix relative paths
			}
		}
		
		return params;
	}
	
	/**
	 * By now just uses DataStoreFinder to find a new instance of a
	 * DataStoreInfo capable of process <code>connectionParams</code>. In the
	 * future we can see if it is better to cache or pool DataStores for
	 * performance, but definitely we shouldn't maintain a single
	 * DataStoreInfo as instance variable for synchronizing reassons
	 * 
	 * <p>
	 * JG: Umm we actually require a single DataStoreInfo for for locking &
	 * transaction support to work. DataStoreInfo is expected to be thread
	 * aware (that is why it has Transaction Support).
	 * </p>
	 *
	 * @return DataStore
	 *
	 * @throws IllegalStateException if this DataStoreInfo is disabled by
	 *         configuration
	 * @throws NoSuchElementException if no DataStoreInfo is found
	 */
	public synchronized Format getFormat()
	throws IllegalStateException, NoSuchElementException {
		if (!isEnabled()) {
			throw new IllegalStateException(
			"this format is not enabled, check your configuration");
		}
		
		//        Map m = getParams();
		
		if (format == null) {
			try {
				//format = GridFormatFinder.getDataStore(m);
				for( Iterator f_iT = GridFormatFinder.getAvailableFormats(); f_iT.hasNext(); ) {
					Format fTmp = (Format) f_iT.next();
					if( fTmp.getName().equals(type)) {
						format = fTmp;
						
						break;
					}
					
				}
				LOGGER.fine("connection established by " + toString());
			} catch (Throwable ex) {
				throw new IllegalStateException("can't create the format "
						+ getId() + ": " + ex.getClass().getName() + ": "
						+ ex.getMessage() + "\n" + ex.toString());
			}
			
			if (format == null) {
				LOGGER.fine("failed to establish connection with " + toString());
				throw new NoSuchElementException(
						"No format found capable of managing " + toString());
			}
		}
		
		return format;
	}
	
	/**
	 * getTitle purpose.
	 * 
	 * <p>
	 * Returns the dataStore's title.
	 * </p>
	 *
	 * @return String the title.
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * getAbstract purpose.
	 * 
	 * <p>
	 * Returns the dataStore's abstract.
	 * </p>
	 *
	 * @return String the abstract.
	 */
	public String getAbstract() {
		return _abstract;
	}
	
	/**
	 * isEnabled purpose.
	 * 
	 * <p>
	 * Returns true when the data store is enabled.
	 * </p>
	 *
	 * @return true when the data store is enabled.
	 */
	public boolean isEnabled() {
		return enabled;
	}
	
	/**
	 * Implement toString.
	 *
	 * @return String
	 *
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new StringBuffer("FormatConfig[type=").append(getType())
		.append(", enabled=")
		.append(isEnabled())
		.append(", abstract=")
		.append(getAbstract())
		.append(", parameters=")
		.append(getParams())
		.append("]")
		.toString();
	}
	
	/**
	 * Implement containsMetaData.
	 *
	 * @param key
	 *
	 * @return
	 *
	 * @see org.geotools.data.MetaData#containsMetaData(java.lang.String)
	 */
	public boolean containsMetaData(String key) {
		return meta.containsKey(key);
	}
	
	/**
	 * Implement putMetaData.
	 *
	 * @param key
	 * @param value
	 *
	 * @see org.geotools.data.MetaData#putMetaData(java.lang.String,
	 *      java.lang.Object)
	 */
	public void putMetaData(String key, Object value) {
		meta.put(key, value);
	}
	
	/**
	 * Implement getMetaData.
	 *
	 * @param key
	 *
	 * @return
	 *
	 * @see org.geotools.data.MetaData#getMetaData(java.lang.String)
	 */
	public Object getMetaData(String key) {
		return meta.get(key);
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
}
