/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2003, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 */
package org.vfny.geoserver.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.vfny.geoserver.global.dto.DataDTO;
import org.vfny.geoserver.global.dto.DataStoreInfoDTO;
import org.vfny.geoserver.global.dto.FeatureTypeInfoDTO;
import org.vfny.geoserver.global.dto.NameSpaceDTO;
import org.vfny.geoserver.global.dto.StyleDTO;

/**
 * Data purpose.
 * <p>
 * Represents an instance of the catalog.xml file in the configuration of the 
 * server, along with associated configuration files for the feature types.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @see DataSource
 * @see FeatureTypeInfo
 * @see StyleConfig 
 * @version $Id: DataConfig.java,v 1.1.2.4 2004/01/08 17:36:40 dmzwiers Exp $
 */
public class DataConfig{
	public static final String CONFIG_KEY = "Config.Data";
	
	/**
	 * A set of datastores and their names.
	 * @see org.vfny.geoserver.config.data.DataStoreInfo
	 */
	private Map dataStores;
	
	/**
	 * A set of namespaces and their names.
	 * @see org.vfny.geoserver.config.data.NameSpaceConfig
	 */
	private Map nameSpaces;
	
	/**
	 * A set of featuretypes and their names.
	 * @see org.vfny.geoserver.config.data.FeatureTypeInfo
	 */
	private Map featuresTypes;
	
	/**
	 * A set of styles and their names.
	 * @see org.vfny.geoserver.config.data.StyleConfig
	 */
	private Map styles;
	
	/**
	 * the default namespace for the server instance. 
	 * @see org.vfny.geoserver.config.data.NameSpaceConfig
	 */
	private NameSpaceConfig defaultNameSpace;
	
	/**
	 * Data constructor.
	 * <p>
	 * Creates a Data to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public DataConfig(){
		dataStores = new HashMap();
		nameSpaces = new HashMap();
		styles = new HashMap();
		featuresTypes = new HashMap();
		defaultNameSpace = new NameSpaceConfig();
	}
	
	/**
	 * Data constructor.
	 * <p>
	 * Creates a copy of the DataDTO provided. If the Data provided 
	 * is null then default values are used. All the datastructures are cloned. 
	 * </p>
	 * @param data The catalog to copy.
	 */
	public DataConfig(DataDTO data){
		Iterator i = null;
		
		i = data.getDataStores().keySet().iterator();
		dataStores = new HashMap();
		while(i.hasNext()){
			Object key = i.next();
			dataStores.put(key,new DataStoreConfig((DataStoreInfoDTO)data.getDataStores().get(key)));
		}

		i = data.getNameSpaces().keySet().iterator();
		nameSpaces = new HashMap();
		while(i.hasNext()){
			Object key = i.next();
			nameSpaces.put(key,new NameSpaceConfig((NameSpaceDTO)data.getNameSpaces().get(key)));
			if(((NameSpaceConfig)nameSpaces.get(key)).isDefault())
				defaultNameSpace = (NameSpaceConfig)nameSpaces.get(key);
		}

		i = data.getFeaturesTypes().keySet().iterator();
		featuresTypes = new HashMap();
		while(i.hasNext()){
			Object key = i.next();
			featuresTypes.put(key,new FeatureTypeConfig((FeatureTypeInfoDTO)data.getFeaturesTypes().get(key)));
		}

		i = data.getStyles().keySet().iterator();
		styles = new HashMap();
		while(i.hasNext()){
			Object key = i.next();
			styles.put(key,new StyleConfig((StyleDTO)data.getStyles().get(key)));
		}
	}
	

	
	/**
	 * Implement loadDTO.
	 * <p>
	 * Populates the object with the param passed. 
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
	 * 
	 * @param obj An instance of DataDTO to populate this object
	 * @return true when a valid parameter is passed and stored.
	 */
	public void update(DataDTO data){
		if(data == null)
			throw new NullPointerException("Data Data Transfer Object required");
		Iterator i = null;
		
		i = data.getDataStores().keySet().iterator();
		dataStores = new HashMap();
		while(i.hasNext()){
			Object key = i.next();
			dataStores.put(key,new DataStoreConfig((DataStoreInfoDTO)data.getDataStores().get(key)));
		}

		i = data.getNameSpaces().keySet().iterator();
		nameSpaces = new HashMap();
		while(i.hasNext()){
			Object key = i.next();
			nameSpaces.put(key,new NameSpaceConfig((NameSpaceDTO)data.getNameSpaces().get(key)));
			if(((NameSpaceConfig)nameSpaces.get(key)).isDefault())
				defaultNameSpace = (NameSpaceConfig)nameSpaces.get(key);
		}

		i = data.getFeaturesTypes().keySet().iterator();
		featuresTypes = new HashMap();
		while(i.hasNext()){
			Object key = i.next();
			featuresTypes.put(key,new FeatureTypeConfig((FeatureTypeInfoDTO)data.getFeaturesTypes().get(key)));
		}

		i = data.getStyles().keySet().iterator();
		styles = new HashMap();
		while(i.hasNext()){
			Object key = i.next();
			styles.put(key,new StyleConfig((StyleDTO)data.getStyles().get(key)));
		}
	}
	
	public DataDTO toDTO(){
		DataDTO dt = new DataDTO();
		HashMap tmp = null;
		Iterator i = null;
		
		tmp = new HashMap();
		i = dataStores.keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			tmp.put(key,((DataStoreConfig)dataStores.get(key)).toDTO());
		}
		dt.setDataStores(tmp);
		
		tmp = new HashMap();
		i = featuresTypes.keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			tmp.put(key,((FeatureTypeConfig)featuresTypes.get(key)).toDTO());
		}
		dt.setFeaturesTypes(tmp);
		
		tmp = new HashMap();
		i = styles.keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			tmp.put(key,((StyleConfig)styles.get(key)).toDTO());
		}
		dt.setStyles(tmp);
		
		tmp = new HashMap();
		i = nameSpaces.keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			tmp.put(key,((NameSpaceConfig)nameSpaces.get(key)).toDTO());
			if(((NameSpaceDTO)tmp.get(key)).isDefault())
				dt.setDefaultNameSpace((NameSpaceDTO)tmp.get(key));
		}
		dt.setNameSpaces(tmp);
		
		return dt;
	}
	
	public List getFeatureTypeConfigKeys(){
		return new ArrayList( featuresTypes.keySet() );
	}
	/**
	 * Lookup FeatureTypeConfig for things like WMS.
	 * 
	 * @param key Key based on <code>dataStoreID.typeName</code>
	 * @return FeatureTypeInfo or null if not found
	 */
	public FeatureTypeConfig lookupFeatureTypeConfig( String key ){
		if( featuresTypes.containsKey( key ) ){
			return (FeatureTypeConfig) featuresTypes.get( key );
		}
		else {
			throw new NoSuchElementException("Could not find FeatureTypeConfig '"+key+"'." );
		}
	}
	/**
	 * getDataStores purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Map getDataStores() {
		return dataStores;
	}
	
	/**
	 * getDataStores purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public DataStoreConfig getDataStore(String key) {
		return (DataStoreConfig)dataStores.get(key);
	}

	/**
	 * getDefaultNameSpace purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public NameSpaceConfig getDefaultNameSpace() {
		return defaultNameSpace;
	}

	/**
	 * getFeatures purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Map getFeaturesTypes() {
		return featuresTypes;
	}

	/**
	 * getFeatures purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public FeatureTypeConfig getFeature(String key) {
		return (FeatureTypeConfig)featuresTypes.get(key);
	}

	/**
	 * getNameSpaces purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Map getNameSpaces() {
		return nameSpaces;
	}

	/**
	 * getNameSpaces purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public NameSpaceConfig getNameSpace(String key) {
		return (NameSpaceConfig)nameSpaces.get(key);
	}

	/**
	 * getStyles purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Map getStyles() {
		return styles;
	}

	/**
	 * getStyles purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public StyleConfig getStyle(String key) {
		return (StyleConfig)styles.get(key);
	}

	/**
	 * setDataStores purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void setDataStores(Map map) {
		if(map != null)
			dataStores = map;
	}

	/**
	 * setDataStores purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void addDataStore(String key, DataStoreConfig ds) {
		if(dataStores == null)
			dataStores = new HashMap();
		if(key != null && ds != null)
			dataStores.put(key,ds);
	}

	/**
	 * setDataStores purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public DataStoreConfig removeDataStore(String key) {
		if(dataStores == null)
			dataStores = new HashMap();
		return (DataStoreConfig)dataStores.remove(key);
	}

	/**
	 * setDefaultNameSpace purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param support
	 */
	public void setDefaultNameSpace(NameSpaceConfig support) {
		if(support!= null)
			defaultNameSpace = support;
	}

	/**
	 * setFeatures purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void setFeaturesTypes(Map map) {
		if(map != null)
		featuresTypes = map;
	}

	/**
	 * setFeatures purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void addFeature(String key, FeatureTypeConfig ft) {
		if(featuresTypes == null)
			featuresTypes = new HashMap();
		if(key != null && ft != null)
			featuresTypes.put(key,ft);
	}

	/**
	 * setFeatures purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public FeatureTypeConfig removeFeature(String key) {
		if(featuresTypes == null)
			featuresTypes = new HashMap();
		return (FeatureTypeConfig)featuresTypes.remove(key);
	}

	/**
	 * setNameSpaces purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void setNameSpaces(Map map) {
		if(map != null)
		nameSpaces = map;
	}

	/**
	 * setNameSpaces purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void addNameSpace(String key, NameSpaceConfig ns) {
		if(nameSpaces == null)
			nameSpaces = new HashMap();
		if(key != null && ns != null)
			nameSpaces.put(key,ns);
	}

	/**
	 * setNameSpaces purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public NameSpaceConfig removeNameSpace(String key) {
		if(nameSpaces == null)
			nameSpaces = new HashMap();
		return (NameSpaceConfig)nameSpaces.remove(key);
	}

	/**
	 * setStyles purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void setStyles(Map map) {
		if(map != null)
		styles = map;
	}

	/**
	 * setStyles purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public void addStyle(String key, StyleConfig s) {
		if(styles == null)
			styles = new HashMap();
		if(key != null && s != null)
			styles.put(key,s);
	}

	/**
	 * setStyles purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param map
	 */
	public StyleConfig removeStyle(String key) {
		if(styles == null)
			styles = new HashMap();
		return (StyleConfig)styles.remove(key);
	}

}
