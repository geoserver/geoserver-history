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
package org.vfny.geoserver.config.data;

import java.util.Map;
import java.util.HashMap;

import org.vfny.geoserver.config.CloneLibrary;
import org.vfny.geoserver.config.EqualsLibrary;
import org.vfny.geoserver.config.DataStructure;
/**
 * Catalog purpose.
 * <p>
 * Represents an instance of the catalog.xml file in the configuration of the 
 * server, along with associated configuration files for the feature types.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @see DataSource
 * @see FeatureType
 * @see Style 
 * @version $Id: Catalog.java,v 1.1.2.1 2003/12/31 20:05:31 dmzwiers Exp $
 */
public class Catalog implements DataStructure{
	
	/**
	 * A set of datastores and their names.
	 * @see org.vfny.geoserver.config.data.DataStore
	 */
	private Map dataStores;
	
	/**
	 * A set of namespaces and their names.
	 * @see org.vfny.geoserver.config.data.NameSpace
	 */
	private Map nameSpaces;
	
	/**
	 * A set of featuretypes and their names.
	 * @see org.vfny.geoserver.config.data.FeatureType
	 */
	private Map featuresTypes;
	
	/**
	 * A set of styles and their names.
	 * @see org.vfny.geoserver.config.data.Style
	 */
	private Map styles;
	
	/**
	 * the default namespace for the server instance. 
	 * @see org.vfny.geoserver.config.data.NameSpace
	 */
	private NameSpace defaultNameSpace;
	
	/**
	 * Catalog constructor.
	 * <p>
	 * Creates a Catalog to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public Catalog(){
		defaultSettings();
	}
	
	/**
	 * defaultSettings purpose.
	 * <p>
	 * This method creates default values for the class. This method 
	 * should noly be called by class constructors.
	 * </p>
	 */
	private void defaultSettings(){
		dataStores = new HashMap();
		nameSpaces = new HashMap();
		styles = new HashMap();
		featuresTypes = new HashMap();
		defaultNameSpace = new NameSpace();
	}
	
	/**
	 * Catalog constructor.
	 * <p>
	 * Creates a copy of the Catalog provided. If the Catalog provided 
	 * is null then default values are used. All the datastructures are cloned. 
	 * </p>
	 * @param c The catalog to copy.
	 */
	public Catalog(Catalog c){
		try{
			dataStores = CloneLibrary.clone(c.getDataStores());
		}catch(Exception e){
			dataStores = new HashMap();
		}
		try{
			nameSpaces = CloneLibrary.clone(c.getNameSpaces());
		}catch(Exception e){
			nameSpaces = new HashMap();
		}
		try{
			featuresTypes = CloneLibrary.clone(c.getFeaturesTypes());
		}catch(Exception e){
			featuresTypes = new HashMap();
		}
		try{
			styles = CloneLibrary.clone(c.getStyles());
		}catch(Exception e){
			styles = new HashMap();
		}
		defaultNameSpace = (NameSpace)c.getDefaultNameSpace().clone();
	}
	
	/**
	 * Implement clone.
	 * <p>
	 * creates a clone of this object
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A copy of this Catalog
	 */
	public Object clone(){
		return new Catalog(this);
	}
	
	/**
	 * Implement equals.
	 * <p>
	 * recursively tests to determine if the object passed in is a copy of this object.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj The Catalog object to test.
	 * @return true when the object passed is the same as this object.
	 */
	public boolean equals(Object obj){
		if(obj == null || !(obj instanceof Catalog))
			return false;
		Catalog c = (Catalog)obj;
		boolean r = true;
		if(dataStores != null)
			r = r && EqualsLibrary.equals(dataStores,c.getDataStores());
		else if(c.getDataStores()!=null) return false;
		if(nameSpaces != null)
			r = r && EqualsLibrary.equals(nameSpaces,c.getNameSpaces());
		else if(c.getNameSpaces()!=null) return false;
		if(featuresTypes != null)
			r = r && EqualsLibrary.equals(featuresTypes,c.getFeaturesTypes());
		else if(c.getFeaturesTypes()!=null) return false;
		if(defaultNameSpace != null)
			r = r && defaultNameSpace.equals(c.getDefaultNameSpace());
		else if(c.getDefaultNameSpace()!=null) return false;
		
		return r;
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
	public DataStore getDataStore(String key) {
		return (DataStore)dataStores.get(key);
	}

	/**
	 * getDefaultNameSpace purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public NameSpace getDefaultNameSpace() {
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
	public FeatureType getFeature(String key) {
		return (FeatureType)featuresTypes.get(key);
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
	public NameSpace getNameSpace(String key) {
		return (NameSpace)nameSpaces.get(key);
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
	public Style getStyle(String key) {
		return (Style)styles.get(key);
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
	public void addDataStore(String key, DataStore ds) {
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
	public DataStore removeDataStore(String key) {
		if(dataStores == null)
			dataStores = new HashMap();
		return (DataStore)dataStores.remove(key);
	}

	/**
	 * setDefaultNameSpace purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param support
	 */
	public void setDefaultNameSpace(NameSpace support) {
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
	public void addFeature(String key, FeatureType ft) {
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
	public FeatureType removeFeature(String key) {
		if(featuresTypes == null)
			featuresTypes = new HashMap();
		return (FeatureType)featuresTypes.remove(key);
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
	public void addNameSpace(String key, NameSpace ns) {
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
	public NameSpace removeNameSpace(String key) {
		if(nameSpaces == null)
			nameSpaces = new HashMap();
		return (NameSpace)nameSpaces.remove(key);
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
	public void addStyle(String key, Style s) {
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
	public Style removeStyle(String key) {
		if(styles == null)
			styles = new HashMap();
		return (Style)styles.remove(key);
	}

}
