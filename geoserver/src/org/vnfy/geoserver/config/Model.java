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
package org.vnfy.geoserver.config;

import org.vnfy.geoserver.config.data.*;
import org.vnfy.geoserver.config.wfs.*;
import org.vnfy.geoserver.config.wms.*;

/**
 * Model purpose.
 * <p>
 * Description of Model ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: Model.java,v 1.1.2.2 2003/12/31 00:35:05 dmzwiers Exp $
 */
public class Model implements Cloneable {
	private Catalog catalog;
	private Global global;
	private WFS wfs;
	private WMS wms;
	
	public Model(){
		defaultSettings();
	}
	
	private void defaultSettings(){
		catalog = new Catalog();
		global = new Global();
		wfs = new WFS();
		wms = new WMS();
	}
	
	public Model(Model m){
		if(m == null){
			defaultSettings();
			return;
		}
		catalog = (Catalog)m.getCatalog().clone();
		global = (Global)m.getGlobal().clone();
		wfs = (WFS)m.getWfs().clone();
		wms = (WMS)m.getWms().clone();
	}

	public Object clone(){
		return new Model(this);
	}
	
	public boolean equals(Object obj){
		Model m = (Model)obj;
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
