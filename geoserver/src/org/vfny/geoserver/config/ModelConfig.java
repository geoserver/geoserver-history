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

import org.vfny.geoserver.config.data.CatalogConfig;
import org.vfny.geoserver.config.wfs.WFSConfig;
import org.vfny.geoserver.config.wms.WMSConfig;

/**
 * ModelConfig purpose.
 * <p>
 * This class is intended to be used as a set of references for 
 * the other major portions of the configuration to be represented in memory.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: ModelConfig.java,v 1.1.2.4 2004/01/05 22:14:42 dmzwiers Exp $
 */
public class ModelConfig implements DataStructure {
	
	/**
	 * The catalog configuration data structure represented in memory.
	 * @see org.vfny.geoserver.config.data.Data
	 */
	private CatalogConfig catalog;
	
	/**
	 * The server configuration data structure represented in memory.
	 * @see org.vfny.geoserver.config.GlobalData
	 */
	private GlobalConfig global;
	
	/**
	 * The wfs configuration data structure represented in memory.
	 * @see org.vfny.geoserver.config.wfs.WFS
	 */
	private WFSConfig wfs;
	
	/**
	 * The wms configuration data structure represented in memory.
	 * @see org.vfny.geoserver.config.wms.WMS
	 */
	private WMSConfig wms;
	
	/**
	 * ModelConfig constructor.
	 * <p>
	 * Creates and returns a ModelConfig object which contains the default settings.
	 * </p>
	 * @see defaultSettings()
	 */
	public ModelConfig(){
		defaultSettings();
	}
	
	/**
	 * defaultSettings purpose.
	 * <p>
	 * Sets up the class with default values. This method should only be 
	 * called by a class constructor. 
	 * </p>
	 *
	 */
	private void defaultSettings(){
		catalog = new CatalogConfig();
		global = new GlobalConfig();
		wfs = new WFSConfig();
		wms = new WMSConfig();
	}
	
	/**
	 * ModelConfig constructor.
	 * <p>
	 * Creates a copy of the ModelConfig object provided. If the object provided is null, 
	 * default values are used. The data contained in the old ModelConfig object is cloned 
	 * rather than reference copied.
	 * </p>
	 * @param m The ModelConfig object to copy.
	 */
	public ModelConfig(ModelConfig m){
		if(m == null){
			defaultSettings();
			return;
		}
		catalog = (CatalogConfig)m.getCatalog().clone();
		global = (GlobalConfig)m.getGlobal().clone();
		wfs = (WFSConfig)m.getWfs().clone();
		wms = (WMSConfig)m.getWms().clone();
	}

	/**
	 * Implement clone.
	 * <p>
	 * Creates a clone of the current object. 
	 * </p>
	 * @see java.lang.Object#clone()
	 * @return A clone of this object.
	 * @see org.vfny.geoserver.config.GlobalData#clone()
	 * @see org.vfny.geoserver.config.data.Data#clone()
	 * @see org.vfny.geoserver.config.wfs.WFS#clone()
	 * @see org.vfny.geoserver.config.wms.WMS#clone()
	 */
	public Object clone(){
		return new ModelConfig(this);
	}
	
	/**
	 * Implement equals.
	 * <p>
	 * Recursively determines if the two objects are equal.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj An instance of a ModelConfig Object to be tested for equality.
	 * @return true when the two objects are recursively equal.
	 * @see org.vfny.geoserver.config.GlobalData#equals()
	 * @see org.vfny.geoserver.config.data.Data#equals()
	 * @see org.vfny.geoserver.config.wfs.WFS#equals()
	 * @see org.vfny.geoserver.config.wms.WMS#equals()
	 */
	public boolean equals(Object obj){
		if(obj == null || !(obj instanceof ModelConfig))
			return false;
		ModelConfig m = (ModelConfig)obj;
		if(m == null) return false;
		boolean r = true;
		if(catalog != null)
			r = r && catalog.equals(m.getCatalog());
		else if(m.getCatalog() != null) return false;
		if(global != null)
			r = r && global.equals(m.getGlobal());
		else if(m.getGlobal() != null) return false;
		if(wfs != null)
			r = r && wfs.equals(m.getWfs());
		else if(m.getWfs() != null) return false;
		if(wms != null)
			r = r && wms.equals(m.getWms());
		else if(m.getWms() != null) return false;
		return r;
	}
	
	/**
	 * getCatalog purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public CatalogConfig getCatalog() {
		return catalog;
	}

	/**
	 * getGlobal purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public GlobalConfig getGlobal() {
		return global;
	}

	/**
	 * getWfs purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public WFSConfig getWfs() {
		return wfs;
	}

	/**
	 * getWms purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public WMSConfig getWms() {
		return wms;
	}

	/**
	 * setCatalog purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param catalog
	 */
	public void setCatalog(CatalogConfig catalog) {
		this.catalog = catalog;
	}

	/**
	 * setGlobal purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param global
	 */
	public void setGlobal(GlobalConfig global) {
		this.global = global;
	}

	/**
	 * setWfs purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param wfs
	 */
	public void setWfs(WFSConfig wfs) {
		this.wfs = wfs;
	}

	/**
	 * setWms purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param wms
	 */
	public void setWms(WMSConfig wms) {
		this.wms = wms;
	}

}
