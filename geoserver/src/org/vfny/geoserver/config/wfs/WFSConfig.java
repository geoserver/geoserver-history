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
package org.vfny.geoserver.config.wfs;

import org.vfny.geoserver.config.DataStructure;
import org.vfny.geoserver.config.ServiceConfig;

/**
 * GlobalWFS purpose.
 * <p>
 * Description of GlobalWFS 
 * Used to store GlobalWFS data. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WFSConfig.java,v 1.1.2.4 2004/01/05 18:09:35 dmzwiers Exp $
 */
public class WFSConfig implements DataStructure{
	//public static final String WFS_FOLDER = "wfs/1.0.0/";
	//public static final String WFS_BASIC_LOC = WFS_FOLDER + "GlobalWFS-basic.xsd";
	//public static final String WFS_CAP_LOC = WFS_FOLDER
	//	+ "GlobalWFS-capabilities.xsd";

	/**
	 * Constant when loaded. Describes where to find the service on the server.
	 */
	private String describeUrl;
	private boolean gmlPrefixing = false;
	/**
	 * The service parameters for this instance.
	 */
	private ServiceConfig service;	

	/**
	 * GlobalWFS constructor.
	 * <p>
	 * Creates a GlobalWFS to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public WFSConfig(){
		describeUrl = "";
		service = new ServiceConfig();
	}

	/**
	 * GlobalWFS constructor.
	 * <p>
	 * Creates a copy of the GlobalWFS provided. If the GlobalWFS provided 
	 * is null then default values are used. All the data structures are cloned. 
	 * </p>
	 * @param f The GlobalWFS to copy.
	 */
	public WFSConfig(WFSConfig w){
		if(w == null){
			describeUrl = "";
			service = new ServiceConfig();
			return;
		}
		describeUrl = w.getDescribeUrl();
		service = (ServiceConfig)w.getService().clone();
		gmlPrefixing = w.isGmlPrefixing();
	}

	/**
	 * Implement clone.
	 * <p>
	 * creates a clone of this object
	 * </p>
	 * @see java.lang.Object#clone()
	 * 
	 * @return A copy of this GlobalWFS
	 */
	public Object clone(){
		return new WFSConfig(this);
	}

	/**
	 * Implement equals.
	 * <p>
	 * recursively tests to determine if the object passed in is a copy of this object.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj The GlobalWFS object to test.
	 * @return true when the object passed is the same as this object.
	 */
	public boolean equals(Object obj) {
		WFSConfig w = (WFSConfig)obj;
		return (describeUrl.equals(w.getDescribeUrl()) &&
		service.equals(w.getService()));
	}

	/**
	 * getDescribeUrl purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public String getDescribeUrl() {
		return describeUrl;
	}

	/**
	 * getService purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public ServiceConfig getService() {
		return service;
	}

	/**
	 * setDescribeUrl purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setDescribeUrl(String string) {
		describeUrl = string;
	}

	/**
	 * setService purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param service
	 */
	public void setService(ServiceConfig service) {
		if(service == null)
			service = new ServiceConfig();
		this.service = service;
	}
	/**
	 * isGmlPrefixing purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public boolean isGmlPrefixing() {
		return gmlPrefixing;
	}

	/**
	 * setGmlPrefixing purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param b
	 */
	public void setGmlPrefixing(boolean b) {
		gmlPrefixing = b;
	}

}
