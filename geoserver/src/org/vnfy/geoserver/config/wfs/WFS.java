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
package org.vnfy.geoserver.config.wfs;

import org.vnfy.geoserver.config.Service;

/**
 * WFS purpose.
 * <p>
 * Description of WFS 
 * Used to store WFS data. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WFS.java,v 1.1.2.1 2003/12/30 23:39:21 dmzwiers Exp $
 */
public class WFS implements Cloneable{
	//public static final String WFS_FOLDER = "wfs/1.0.0/";
	//public static final String WFS_BASIC_LOC = WFS_FOLDER + "WFS-basic.xsd";
	//public static final String WFS_CAP_LOC = WFS_FOLDER
	//	+ "WFS-capabilities.xsd";

	/**
	 * Constant when loaded.
	 */
	private String describeUrl;
	private Service service;	
	
	public WFS(){
		describeUrl = "";
		service = new Service();
	}
	
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
	 * @see java.lang.Object#clone()
	 * 
	 * @return
	 * @throws java.lang.CloneNotSupportedException
	 */
	public Object clone(){
		return new WFS(this);
	}

	/**
	 * Implement equals.
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj
	 * @return
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
