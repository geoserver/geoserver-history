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

import java.io.File;

import org.vfny.geoserver.global.dto.*;
/**
 * StyleConfig purpose.
 * <p>
 * Defines the style ids to be used by the wms.  The files 
 * must be contained in geoserver/misc/wms/styles.  We're 
 * working on finding a better place for them, but for now 
 * that's where you must put them if you want them on the 
 * server.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: StyleConfig.java,v 1.1.2.1 2004/01/07 21:23:08 dmzwiers Exp $
 */
public class StyleConfig implements DataStructure {
	
	/**
	 * The syle id.
	 */
	private String id = "";
	
	/**
	 * The file which contains more information about the style.
	 */
	private File filename = null;
	
	/**
	 * whether this is the system's default style.
	 */
	private boolean _default = false;

	/**
	 * StyleConfig constructor.
	 * <p>
	 * Creates a StyleConfig to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public StyleConfig(){
		id = "";
		filename = null;
		_default = false;
	}

	/**
	 * StyleConfig constructor.
	 * <p>
	 * Creates a copy of the StyleConfig provided. All the data structures are cloned. 
	 * </p>
	 * @param style The style to copy.
	 */
	public StyleConfig(StyleConfig style){
		if(style == null){
			throw new NullPointerException("");
		}
		id = style.getId();
		filename = new File(style.getFilename().toString());
		_default = style.isDefault();
	}

	/**
	 * StyleConfig constructor.
	 * <p>
	 * Creates a copy of the StyleDTO provided. All the data structures are cloned. 
	 * </p>
	 * @param style The style to copy.
	 */
	public StyleConfig(StyleDTO style){
		if(style == null){
			throw new NullPointerException("");
		}
		id = style.getId();
		filename = new File(style.getFilename().toString());
		_default = style.isDefault();
	}

	/**
	 * Implement clone.
	 * <p>
	 * creates a clone of this object
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A copy of this StyleConfig
	 */
	public Object clone(){
		return new StyleConfig(this);
	}

	/**
	 * Implement loadDTO.
	 * <p>
	 * Stores the data provided for the specified StyleDTO object
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
	 * 
	 * @param obj a StyleDTO object
	 * @return true when obj is valid and stored.
	 */
	public boolean updateDTO(Object obj){
		if(obj == null || !(obj instanceof StyleDTO))
			return false;
		StyleDTO sDto = (StyleDTO)obj;
		id = sDto.getId();
		filename = new File(sDto.getFilename().toString());
		_default = sDto.isDefault();
		return true;
	}
	
	/**
	 * Implement toDTO.
	 * <p>
	 * Creates a StyleDTO which represents the data in this config object.
	 * </p>
	 * @see org.vfny.geoserver.config.DataStructure#toDTO()
	 * @return a copy of this classes data in a StyleDTO object.
	 */
	public Object toDTO(){
		StyleDTO sDto = new StyleDTO();
		sDto.setDefault(_default);
		sDto.setFilename(new File(filename.toString()));
		sDto.setId(id);
		return sDto;
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
	 * getFilename purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public File getFilename() {
		return filename;
	}

	/**
	 * getId purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * setDefault purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param b
	 */
	public void setDefault(boolean b) {
		_default = b;
	}

	/**
	 * setFilename purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param file
	 */
	public void setFilename(File file) {
		filename = file;
	}

	/**
	 * setId purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setId(String string) {
		id = string;
	}

}
