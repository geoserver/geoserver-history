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

/**
 * NameSpaceConfig purpose.
 * <p>
 * Represents the portion of a namespace required for the configuration of geoserver.
 * Defines namespaces to be used by the datastores.
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: NameSpaceConfig.java,v 1.1.2.1 2003/12/31 23:35:14 dmzwiers Exp $
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
			defaultSettings();
			return;
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
	 * Implement equals.
	 * <p>
	 * recursively tests to determine if the object passed in is a copy of this object.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj The NameSpaceConfig object to test.
	 * @return true when the object passed is the same as this object.
	 */
	public boolean equals(Object obj){
		NameSpaceConfig ns = (NameSpaceConfig)obj;
		return (prefix == ns.getPrefix() &&
		(uri == ns.getUri() && 
		_default == ns.isDefault()));
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
