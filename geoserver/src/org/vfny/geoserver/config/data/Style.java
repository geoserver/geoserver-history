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
import org.vfny.geoserver.config.DataStructure;

import java.io.File;
/**
 * Style purpose.
 * <p>
 * Defines the style ids to be used by the wms.  The files 
 * must be contained in geoserver/misc/wms/styles.  We're 
 * working on finding a better place for them, but for now 
 * that's where you must put them if you want them on the 
 * server.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: Style.java,v 1.1.2.1 2003/12/31 20:05:31 dmzwiers Exp $
 */
public class Style implements DataStructure {
	
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
	 * Style constructor.
	 * <p>
	 * Creates a Style to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public Style(){
		defaultSettings();
	}

	/**
	 * Style constructor.
	 * <p>
	 * Creates a copy of the Style provided. If the Style provided 
	 * is null then default values are used. All the data structures are cloned. 
	 * </p>
	 * @param f The style to copy.
	 */
	public Style(Style style){
		if(style == null){
			defaultSettings();
			return;
		}
		id = style.getId();
		filename = new File(style.getFilename().toString());
		_default = style.isDefault();
	}

	/**
	 * defaultSettings purpose.
	 * <p>
	 * This method creates default values for the class. This method 
	 * should noly be called by class constructors.
	 * </p>
	 */
	private void defaultSettings(){
		id = "";
		filename = null;
		_default = false;
	}

	/**
	 * Implement clone.
	 * <p>
	 * creates a clone of this object
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A copy of this Style
	 */
	public Object clone(){
		return new Style(this);
	}

	/**
	 * Implement equals.
	 * <p>
	 * recursively tests to determine if the object passed in is a copy of this object.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj The Style object to test.
	 * @return true when the object passed is the same as this object.
	 */
	public boolean equals(Object obj){
		if(obj == null)
			return false;
		Style style = (Style)obj;
		boolean r = true;
		r = r && id == style.getId();
		if(filename !=null)
			r = r && filename.equals(style.getFilename());
		r = r && _default == style.isDefault();
		return r;
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
