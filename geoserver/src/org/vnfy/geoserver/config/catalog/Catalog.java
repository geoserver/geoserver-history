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
package org.vnfy.geoserver.config.catalog;

import java.util.Map;
import java.util.HashMap;

import org.vnfy.geoserver.config.CloneLibrary;
import org.vnfy.geoserver.config.EqualsLibrary;
/**
 * Catalog purpose.
 * <p>
 * Description of Catalog ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @see DataSource
 * @see FeatureType
 * @see Style 
 * @version $Id: Catalog.java,v 1.1.2.1 2003/12/30 23:39:14 dmzwiers Exp $
 */
public class Catalog implements Cloneable{
	private Map dataStores;
	private Map nameSpaces;
	private Map featuresTypes;
	private Map styles;
	private NameSpace defaultNameSpace;
	
	public Catalog(){
		defaultSettings();
	}
	
	private void defaultSettings(){
		dataStores = new HashMap();
		nameSpaces = new HashMap();
		styles = new HashMap();
		featuresTypes = new HashMap();
		defaultNameSpace = new NameSpace();
	}
	
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
	
	public Object clone(){
		return new Catalog(this);
	}
	
	public boolean equals(Object obj){
		if(obj == null)
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
