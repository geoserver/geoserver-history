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

import org.vfny.geoserver.config.Service;
import org.vfny.geoserver.config.DataStructure;

/**
 * WFS purpose.
 * <p>
 * Description of WFS 
 * Used to store WFS data. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WFS.java,v 1.1.2.1 2003/12/31 20:05:33 dmzwiers Exp $
 */
public class WFS implements DataStructure{
	//public static final String WFS_FOLDER = "wfs/1.0.0/";
	//public static final String WFS_BASIC_LOC = WFS_FOLDER + "WFS-basic.xsd";
	//public static final String WFS_CAP_LOC = WFS_FOLDER
	//	+ "WFS-capabilities.xsd";

	/**
	 * Constant when loaded. Describes where to find the service on the server.
	 */
	private String describeUrl;
	
	/**
	 * The service parameters for this instance.
	 */
	private Service service;	

	/**
	 * WFS constructor.
	 * <p>
	 * Creates a WFS to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public WFS(){
		describeUrl = "";
		service = new Service();
	}

	/**
	 * WFS constructor.
	 * <p>
	 * Creates a copy of the WFS provided. If the WFS provided 
	 * is null then default values are used. All the data structures are cloned. 
	 * </p>
	 * @param f The WFS to copy.
	 */
	public WFS(WFS w){
		if(w == null){
			describeUrl = "";
			service = new Service();
			return;
		}
		describeUrl = w.getDescribeUrl();
		service = (Service)w.getService().clone();
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
		return new WFS(this);
	}

	/**
	 * Implement equals.
	 * <p>
	 * recursively tests to determine if the object passed in is a copy of this object.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj The WFS object to test.
	 * @return true when the object passed is the same as this object.
	 */
	public boolean equals(Object obj) {
		WFS w = (WFS)obj;
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
	public Service getService() {
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
	public void setService(Service service) {
		if(service == null)
			service = new Service();
		this.service = service;
	}
}
