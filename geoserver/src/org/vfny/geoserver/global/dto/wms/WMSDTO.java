/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto.wms;

import java.util.Date;

import org.vfny.geoserver.global.dto.DataStructure;
import org.vfny.geoserver.global.dto.ServiceDTO;


/**
 * Data Transfer Object for GeoServer Web Map Server information.
 * 
 * <p>
 * Information required for GeoServer to set up a Web Map Service.
 * </p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WMSDTO.java,v 1.1.2.1 2004/01/04 06:21:33 jive Exp $
 */
public final class WMSDTO implements DataStructure {
    private static final String WMS_VERSION = "1.1.1";

    /** GlobalWMS spec specifies this fixed service name */

    //private static final String FIXED_SERVICE_NAME = "OGC:GlobalWMS";
    //private static final String[] EXCEPTION_FORMATS = {
    //	"application/vnd.ogc.se_xml", "application/vnd.ogc.se_inimage",
    //	"application/vnd.ogc.se_blank"
    //};

    /** when the configuration was loaded. */
    private Date updateTime = new Date();

    /**
     * Constant when loaded. Describes where to find the service on the server.
     */
    private String describeUrl;

    /** The service parameters for this instance. */
    private ServiceDTO service;

    /**
     * GlobalWMS constructor.
     * 
     * <p>
     * Creates a GlobalWMS to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public WMSDTO() {
        service = new ServiceDTO();
        describeUrl = "";
        updateTime = new Date();
    }

    /**
     * GlobalWMS constructor.
     * 
     * <p>
     * Creates a copy of the GlobalWMS provided. If the GlobalWMS provided  is
     * null then default values are used. All the data structures are cloned.
     * </p>
     *
     * @param w The GlobalWMS to copy.
     */
    public WMSDTO(WMSDTO w) {
        if (w == null) {
            service = new ServiceDTO();

            return;
        }

        updateTime = (Date) w.getUpdateTime().clone();
        describeUrl = w.getDescribeUrl();
        service = (ServiceDTO) w.getService().clone();
    }

    /**
     * Implement clone.
     * 
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this GlobalWMS
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new WMSDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The GlobalWMS object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        WMSDTO w = (WMSDTO) obj;

        //time was left out as it was not relevant for most comparisons
        return ((describeUrl == w.getDescribeUrl())
        && service.equals(w.getService()));
    }

    /**
     * getService purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public ServiceDTO getService() {
        return service;
    }

    /**
     * getUpdateTime purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * setService purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param service
     */
    public void setService(ServiceDTO service) {
        if (service == null) {
            service = new ServiceDTO();
        }

        this.service = service;
    }

    /**
     * setUpdateTime purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param date
     */
    public void setUpdateTime(Date date) {
        if (date == null) {
            date = new Date();
        }

        updateTime = date;
    }

    /**
     * getDescribeUrl purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getDescribeUrl() {
        return describeUrl;
    }

    /**
     * setDescribeUrl purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setDescribeUrl(String string) {
        describeUrl = string;
    }
}
