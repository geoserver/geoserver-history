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

import org.vfny.geoserver.config.data.*;
import org.vfny.geoserver.config.wfs.*;
import org.vfny.geoserver.config.wms.*;

/**
 * Config purpose.
 * <p>
 * This class is intended to be used as a set of references for 
 * the other major portions of the configuration to be represented in memory.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: Config.java,v 1.1.2.1 2003/12/31 20:05:33 dmzwiers Exp $
 */
public class Config implements DataStructure {
	
	/**
	 * The catalog configuration data structure represented in memory.
	 * @see org.vfny.geoserver.config.data.Catalog
	 */
	private Catalog catalog;
	
	/**
	 * The server configuration data structure represented in memory.
	 * @see org.vfny.geoserver.config.Global
	 */
	private Global global;
	
	/**
	 * The wfs configuration data structure represented in memory.
	 * @see org.vfny.geoserver.config.wfs.WFS
	 */
	private WFS wfs;
	
	/**
	 * The wms configuration data structure represented in memory.
	 * @see org.vfny.geoserver.config.wms.WMS
	 */
	private WMS wms;
	
	/**
	 * Config constructor.
	 * <p>
	 * Creates and returns a Config object which contains the default settings.
	 * </p>
	 * @see defaultSettings()
	 */
	public Config(){
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
		catalog = new Catalog();
		global = new Global();
		wfs = new WFS();
		wms = new WMS();
	}
	
	/**
	 * Config constructor.
	 * <p>
	 * Creates a copy of the Config object provided. If the object provided is null, 
	 * default values are used. The data contained in the old Config object is cloned 
	 * rather than reference copied.
	 * </p>
	 * @param m The Config object to copy.
	 */
	public Config(Config m){
		if(m == null){
			defaultSettings();
			return;
		}
		catalog = (Catalog)m.getCatalog().clone();
		global = (Global)m.getGlobal().clone();
		wfs = (WFS)m.getWfs().clone();
		wms = (WMS)m.getWms().clone();
	}

	/**
	 * Implement clone.
	 * <p>
	 * Creates a clone of the current object. 
	 * </p>
	 * @see java.lang.Object#clone()
	 * @return A clone of this object.
	 * @see org.vfny.geoserver.config.Global#clone()
	 * @see org.vfny.geoserver.config.data.Catalog#clone()
	 * @see org.vfny.geoserver.config.wfs.WFS#clone()
	 * @see org.vfny.geoserver.config.wms.WMS#clone()
	 */
	public Object clone(){
		return new Config(this);
	}
	
	/**
	 * Implement equals.
	 * <p>
	 * Recursively determines if the two objects are equal.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj An instance of a Config Object to be tested for equality.
	 * @return true when the two objects are recursively equal.
	 * @see org.vfny.geoserver.config.Global#equals()
	 * @see org.vfny.geoserver.config.data.Catalog#equals()
	 * @see org.vfny.geoserver.config.wfs.WFS#equals()
	 * @see org.vfny.geoserver.config.wms.WMS#equals()
	 */
	public boolean equals(Object obj){
		if(obj == null || !(obj instanceof Config))
			return false;
		Config m = (Config)obj;
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
	public Catalog getCatalog() {
		return catalog;
	}

	/**
	 * getGlobal purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Global getGlobal() {
		return global;
	}

	/**
	 * getWfs purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public WFS getWfs() {
		return wfs;
	}

	/**
	 * getWms purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public WMS getWms() {
		return wms;
	}

	/**
	 * setCatalog purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param catalog
	 */
	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

	/**
	 * setGlobal purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param global
	 */
	public void setGlobal(Global global) {
		this.global = global;
	}

	/**
	 * setWfs purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param wfs
	 */
	public void setWfs(WFS wfs) {
		this.wfs = wfs;
	}

	/**
	 * setWms purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param wms
	 */
	public void setWms(WMS wms) {
		this.wms = wms;
	}

}
