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

/**
 * NameSpace purpose.
 * <p>
 * Description of NameSpace ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: NameSpace.java,v 1.1.2.1 2003/12/30 23:39:14 dmzwiers Exp $
 */
public class NameSpace implements Cloneable {
	//public static final String PREFIX_DELIMITER = ":";
	private String prefix;
	private String uri;
	private boolean _default;
	
	public NameSpace(){
		defaultSettings();
	}
	
	private void defaultSettings(){
		prefix = "";
		uri = "";
		_default = false;
	}
	
	public NameSpace(NameSpace ns){
		if(ns == null){
			defaultSettings();
			return;
		}
		prefix = ns.getPrefix();
		uri = ns.getUri();
		_default = ns.isDefault();
	}
	
	public Object clone(){
		return new NameSpace(this);
	}
	
	public boolean equals(Object obj){
		NameSpace ns = (NameSpace)obj;
		return (prefix == ns.getPrefix() &&
		(uri == ns.getUri() && 
		_default == ns.isDefault()));
	}
	/**
	 * is_default purpose.
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
	 * set_default purpose.
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
