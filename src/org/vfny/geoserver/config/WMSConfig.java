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

import java.util.Set;
import java.util.TreeSet;

import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WMSDTO;

/**
 * WMS purpose.
 * <p>
 * Description of WMS 
 * Used to store WMS data. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WMSConfig.java,v 1.3 2004/01/12 23:55:27 dmzwiers Exp $
 */
public class WMSConfig extends ServiceConfig{

	private static final String WMS_VERSION = "1.1.1";
	public static final String CONFIG_KEY = "Config.WMS";
	
	/**
	 * This is a set of <code>dataStoreID.typeName</code> that is 
	 * enabled for use with WMS.
	 * <p>
	 * You can use this information to bother DataConfig for the details such as:
	 * </p>
	 * <ul>
	 * <li>Title</li>
	 * <li>Abstract</li>
	 * </ul>
	 * <p>
	 * Cool?
	 * </p>
	 */
	private Set enabledFeatures = new TreeSet(); // keep sorted
	
	/**
	 * A string representing the update time of the WMS
	 *
	 */
	private String updateTime;
	
	/**
	 * WMS constructor.
	 * <p>
	 * Creates a WMS to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public WMSConfig(){
		super();
	}

	/**
	 * WMS constructor.
	 * <p>
	 * Creates a copy of the WMSDTO provided. All the data structures are cloned. 
	 * </p>
	 * @param f The WMSDTO to copy.
	 */
	public WMSConfig(WMSDTO w){
		super(w.getService());
	}

	/**
	 * Implement loadDTO.
	 * <p>
	 * Takes a WMSDTO and loads it into this WMSConfig Object
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
	 * 
	 * @param dto an instance of WMSDTO
	 * @return false when obj null, or not correct class instance.
	 */
	public void update(WMSDTO dto){
	 if(dto==null)
	 throw new NullPointerException("WMS Data Transfer Object required");
	  super.update(dto.getService());
	}
	
	/**
	 * Implement toDTO.
	 * <p>
	 * Returns a copy of the data in a ServiceDTO object
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#toDTO()
	 * 
	 * @return a copy of the data in a ServiceDTO object
	 */
	public WMSDTO toDTO(){
		WMSDTO wmsDto = new WMSDTO();
		wmsDto.setService((ServiceDTO)super.toServDTO());
		return wmsDto;
	}
	/**
	 * @return
	 */
	public String getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param string
	 */
	public void setUpdateTime(String string) {
		updateTime = string;
	}

	/**
	 * @return
	 */
	public Set getEnabledFeatures() {
		return enabledFeatures;
	}

	/**
	 * @param set
	 */
	public void setEnabledFeatures(Set set) {
		enabledFeatures = set;
	}

}