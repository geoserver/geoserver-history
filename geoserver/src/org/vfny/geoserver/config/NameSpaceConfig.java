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

import org.vfny.geoserver.global.dto.*;
/**
 * NameSpaceConfig purpose.
 * <p>
 * Represents the portion of a namespace required for the configuration of geoserver.
 * Defines namespaces to be used by the datastores.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: NameSpaceConfig.java,v 1.1.2.1 2004/01/07 21:23:08 dmzwiers Exp $
 */
public class NameSpaceConfig implements DataStructure {
	//public static final String PREFIX_DELIMITER = ":";
	
	/**
	 * The namespace prefix.
	 */
	private String prefix;
	
	/**
	 * The URI for this namespace.
	 */
	private String uri;
	
	/**
	 * Whether this is the default namespace.
	 */
	private boolean _default;

	/**
	 * NameSpaceConfig constructor.
	 * <p>
	 * Creates a NameSpaceConfig to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public NameSpaceConfig(){
		prefix = "";
		uri = "";
		_default = false;
	}

	/**
	 * NameSpaceConfig constructor.
	 * <p>
	 * Creates a copy of the NameSpaceConfig provided. If the NameSpaceConfig provided 
	 * is null then default values are used. All the data structures are cloned. 
	 * </p>
	 * @param f The namespace to copy.
	 */
	public NameSpaceConfig(NameSpaceConfig ns){
		if(ns == null){
			throw new NullPointerException("");
		}
		prefix = ns.getPrefix();
		uri = ns.getUri();
		_default = ns.isDefault();
	}

	/**
	 * NameSpaceConfig constructor.
	 * <p>
	 * Creates a copy of the NameSpaceConfig provided. If the NameSpaceConfig provided 
	 * is null then default values are used. All the data structures are cloned. 
	 * </p>
	 * @param f The namespace to copy.
	 */
	public NameSpaceConfig(NameSpaceDTO ns){
		if(ns == null){
			throw new NullPointerException("");
		}
		prefix = ns.getPrefix();
		uri = ns.getUri();
		_default = ns.isDefault();
	}

	/**
	 * Implement clone.
	 * <p>
	 * creates a clone of this object
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A copy of this NameSpaceConfig
	 */
	public Object clone(){
		return new NameSpaceConfig(this);
	}

	/**
	 * Implement loadDTO.
	 * <p>
	 * Imports the data contained in the NameSpaceDTO object provided.
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
	 * 
	 * @param dto An NameSpaceDTO object
	 * @return true when the instance provided is valid and stored.
	 */
	public boolean updateDTO(Object dto){
		if(dto == null || !(dto instanceof NameSpaceDTO))
			return false;
		NameSpaceDTO ns = (NameSpaceDTO)dto;
		prefix = ns.getPrefix();
		uri = ns.getUri();
		_default = ns.isDefault();
		return true;
	}
	
	/**
	 * Implement toDTO.
	 * <p>
	 * Creates a DTO representation of this Object as a NameSpaceDTO
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#toDTO()
	 * 
	 * @return a NameSpaceDTO which representts the data in this class.
	 */
	public Object toDTO(){
		NameSpaceDTO nsDto = new NameSpaceDTO();
		nsDto.setDefault(_default);
		nsDto.setPrefix(prefix);
		nsDto.setUri(uri);
		return nsDto;
	}
	
	/**
	 * isDefault purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public boolean isDefault() {
		return _default;
	}

	/**
	 * getPrefix purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * getUri purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * setDdefault purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param b
	 */
	public void setDefault(boolean b) {
		_default = b;
	}

	/**
	 * setPrefix purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setPrefix(String string) {
		prefix = string;
	}

	/**
	 * setUri purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setUri(String string) {
		uri = string;
	}

}
