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

import java.io.File;
/**
 * Style purpose.
 * <p>
 * Description of Style ...
 * <p>
 * Capabilities:
 * <ul>
 * </li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * Style x = new Style(...);
 * </code></pre>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: Style.java,v 1.1.2.1 2003/12/30 23:39:14 dmzwiers Exp $
 */
public class Style implements Cloneable {
	private String id = "";
	private File filename = null;
	private boolean _default = false;

	public Style(){
		defaultSettings();
	}
	
	public Style(Style style){
		if(style == null){
			defaultSettings();
			return;
		}
		id = style.getId();
		filename = new File(style.getFilename().toString());
		_default = style.isDefault();
	}
	
	private void defaultSettings(){
		id = "";
		filename = null;
		_default = false;
	}
	
	public Object clone(){
		return new Style(this);
	}
	
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
