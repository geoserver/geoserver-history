/* Copyright (c) 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global;

import org.vfny.geoserver.global.dto.*;
/**
 * NameSpace purpose.
 * <p>
 * Description of NameSpace ...
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: NameSpace.java,v 1.1.2.7 2004/01/05 23:26:25 dmzwiers Exp $
 */
public class NameSpace extends Abstract{
	private NameSpaceDTO nsDTO;
	public static final String PREFIX_DELIMITER = ":";
	/**
	 * NameSpaceConfig constructor.
	 * 
	 * <p>
	 * Creates a NameSpaceConfig to represent an instance with default data.
	 * </p>
	 *
	 * @see defaultSettings()
	 */
	public NameSpace() {
		nsDTO = new NameSpaceDTO();
	}

	/**
	 * NameSpaceConfig constructor.
	 * 
	 * <p>
	 * Creates a copy of the NameSpaceConfig provided. If the NameSpaceConfig
	 * provided  is null then default values are used. All the data structures
	 * are cloned.
	 * </p>
	 *
	 * @param ns The namespace to copy.
	 */
	public NameSpace(NameSpaceDTO ns) {
		if (ns == null) {
			nsDTO = new NameSpaceDTO();
			return;
		}
		nsDTO = (NameSpaceDTO)ns.clone();
	}

	/**
	 * NameSpaceConfig constructor.
	 * 
	 * <p>
	 * Creates a copy of the NameSpaceConfig provided. If the NameSpaceConfig
	 * provided  is null then default values are used. All the data structures
	 * are cloned.
	 * </p>
	 *
	 * @param ns The namespace to copy.
	 */
	public NameSpace(NameSpace ns) {
		if (ns == null) {
			nsDTO = new NameSpaceDTO();
			return;
		}
		nsDTO = new NameSpaceDTO();
		nsDTO.setPrefix(ns.getPrefix());
		nsDTO.setUri(ns.getUri());
		nsDTO.setDefault(ns.isDefault());
	}
	
	Object getDTO(){
		return nsDTO;
	}

	/**
	 * Implement clone.
	 * 
	 * <p>
	 * creates a clone of this object
	 * </p>
	 *
	 * @return A copy of this NameSpaceConfig
	 *
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new NameSpace(this);
	}

	/**
	 * Implement equals.
	 * 
	 * <p>
	 * recursively tests to determine if the object passed in is a copy of this
	 * object.
	 * </p>
	 *
	 * @param obj The NameSpaceConfig object to test.
	 *
	 * @return true when the object passed is the same as this object.
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		NameSpace ns = (NameSpace) obj;

		return ((nsDTO.getPrefix() == ns.getPrefix())
		&& ((nsDTO.getUri() == ns.getUri()) && (nsDTO.isDefault() == ns.isDefault())));
	}

	/**
	 * isDefault purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 *
	 * @return
	 */
	public boolean isDefault() {
		return nsDTO.isDefault();
	}

	/**
	 * getPrefix purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 *
	 * @return
	 */
	public String getPrefix() {
		return nsDTO.getPrefix();
	}

	/**
	 * getUri purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 *
	 * @return
	 */
	public String getUri() {
		return nsDTO.getUri();
	}

	/**
	 * setDdefault purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 *
	 * @param b
	 */
	public void setDefault(boolean b) {
		nsDTO.setDefault(b);
	}

	/**
	 * setPrefix purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 *
	 * @param string
	 */
	public void setPrefix(String string) {
		nsDTO.setPrefix(string);
	}

	/**
	 * setUri purpose.
	 * 
	 * <p>
	 * Description ...
	 * </p>
	 *
	 * @param string
	 */
	public void setUri(String string) {
		nsDTO.setUri(string);
	}
}
