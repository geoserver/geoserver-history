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
package org.vfny.geoserver.config.wms;

import java.util.Date;

import org.vfny.geoserver.config.Service;
import org.vfny.geoserver.config.DataStructure;
/**
 * WMS purpose.
 * <p>
 * Description of WMS 
 * Used to store WMS data. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WMS.java,v 1.1.2.1 2003/12/31 20:05:37 dmzwiers Exp $
 */
public class WMS implements DataStructure{

	private static final String WMS_VERSION = "1.1.1";

	/** WMS spec specifies this fixed service name */
	//private static final String FIXED_SERVICE_NAME = "OGC:WMS";
	//private static final String[] EXCEPTION_FORMATS = {
	//	"application/vnd.ogc.se_xml", "application/vnd.ogc.se_inimage",
	//	"application/vnd.ogc.se_blank"
	//};
	
	/**
	 * when the configuration was loaded.
	 */
	private Date updateTime = new Date();
	
	/**
	 * Constant when loaded. Describes where to find the service on the server.
	 */
	private String describeUrl;
	
	/**
	 * The service parameters for this instance.
	 */
	private Service service;

	/**
	 * WMS constructor.
	 * <p>
	 * Creates a WMS to represent an instance with default data.
	 * </p>
	 * @see defaultSettings()
	 */
	public WMS(){
		service = new Service();
		describeUrl = "";
		updateTime = new Date();
	}

	/**
	 * WMS constructor.
	 * <p>
	 * Creates a copy of the WMS provided. If the WMS provided 
	 * is null then default values are used. All the data structures are cloned. 
	 * </p>
	 * @param f The WMS to copy.
	 */
	public WMS(WMS w){
		if(w == null){
			service = new Service();
			return;
		}
		updateTime = (Date)w.getUpdateTime().clone();
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
	 * @return A copy of this WMS
	 */
	public Object clone(){
		return new WMS(this);
	}

	/**
	 * Implement equals.
	 * <p>
	 * recursively tests to determine if the object passed in is a copy of this object.
	 * </p>
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 * @param obj The WMS object to test.
	 * @return true when the object passed is the same as this object.
	 */
	public boolean equals(Object obj){
		WMS w = (WMS)obj;
		//time was left out as it was not relevant for most comparisons
		return (describeUrl == w.getDescribeUrl() &&
		service.equals(w.getService()));
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
	 * getUpdateTime purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public Date getUpdateTime() {
		return updateTime;
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

	/**
	 * setUpdateTime purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param date
	 */
	public void setUpdateTime(Date date) {
		if(date == null)
			date = new Date();
		updateTime = date;
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
	 * setDescribeUrl purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param string
	 */
	public void setDescribeUrl(String string) {
		describeUrl = string;
	}

}