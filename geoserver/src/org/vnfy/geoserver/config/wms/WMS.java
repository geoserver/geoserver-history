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
package org.vnfy.geoserver.config.wms;

import java.util.Date;

import org.vnfy.geoserver.config.Service;
/**
 * WMS purpose.
 * <p>
 * Description of WMS 
 * Used to store WMS data. 
 * <p>
 * 
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WMS.java,v 1.1.2.1 2003/12/30 23:39:43 dmzwiers Exp $
 */
public class WMS implements Cloneable{

	private static final String WMS_VERSION = "1.1.1";

	/** WMS spec specifies this fixed service name */
	//private static final String FIXED_SERVICE_NAME = "OGC:WMS";
	//private static final String[] EXCEPTION_FORMATS = {
	//	"application/vnd.ogc.se_xml", "application/vnd.ogc.se_inimage",
	//	"application/vnd.ogc.se_blank"
	//};
	private Date updateTime = new Date();
	
	/**
	 * Constant when loaded.
	 */
	private String describeUrl;
	private Service service;

	public WMS(){
		service = new Service();
		describeUrl = "";
		updateTime = new Date();
	}
	
	public WMS(WMS w){
		if(w == null){
			service = new Service();
			return;
		}
		updateTime = (Date)w.getUpdateTime().clone();
		describeUrl = w.getDescribeUrl();
		service = (Service)w.getService().clone();
	}
	
	public Object clone(){
		return new WMS(this);
	}
	
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