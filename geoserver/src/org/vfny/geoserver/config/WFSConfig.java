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

import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WFSDTO;
/**
 * WFS purpose.
 * <p>
 * Description of WFS 
 * Used to store WFS data. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WFSConfig.java,v 1.2.2.3 2004/01/07 21:36:13 dmzwiers Exp $
 */
public class WFSConfig extends ServiceConfig{

	public static final String CONFIG_KEY = "Config.WFS";
	/**
	 * WFS constructor.
	 * <p>
	 * Creates a WFS to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public WFSConfig(){
		super();
	}

	/**
	 * WFS constructor.
	 * <p>
	 * Creates a copy of the WFS provided. If the WFS provided 
	 * is null then default values are used. All the data structures are cloned. 
	 * </p>
	 * @param f The WFS to copy.
	 */
	public WFSConfig(WFSConfig w){
		super(w);
	}

	/**
	 * WFS constructor.
	 * <p>
	 * Creates a copy of the WFS provided. If the WFS provided 
	 * is null then default values are used. All the data structures are cloned. 
	 * </p>
	 * @param f The WFS to copy.
	 */
	public WFSConfig(WFSDTO w){
		super(w.getService());
	}

	/**
	 * Implement clone.
	 * <p>
	 * creates a clone of this object
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A copy of this WFS
	 */
	public Object clone(){
		return new WFSConfig(this);
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
	public boolean updateDTO(Object dto){
	 if(dto==null || !(dto instanceof WFSDTO))
		return false;
		WFSDTO s = (WFSDTO)dto;
	 return super.updateDTO(s.getService());
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
	public Object toDTO(){
		WFSDTO wfsDto = new WFSDTO();
		wfsDto.setService((ServiceDTO)super.toDTO());
		return wfsDto;
	}
}
