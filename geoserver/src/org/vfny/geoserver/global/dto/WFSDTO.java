/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;



/**
 * Data Transfer Object for GeoServer's Web Feature Service.
 * 
 * <p></p>
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WFSDTO.java,v 1.1.2.1 2004/01/05 23:26:26 dmzwiers Exp $
 */
public final class WFSDTO implements DataStructure {
    //public static final String WFS_FOLDER = "wfs/1.0.0/";
    //public static final String WFS_BASIC_LOC = WFS_FOLDER + "WFS-basic.xsd";
    //public static final String WFS_CAP_LOC = WFS_FOLDER
    //	+ "WFS-capabilities.xsd";

    /**
     * Constant when loaded. Describes where to find the service on the server.
     */
    private String describeUrl;

    /** The service parameters for this instance. */
    private ServiceDTO service;

    /**
     * WFS constructor.
     * 
     * <p>
     * Creates a WFS to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public WFSDTO() {
        describeUrl = "";
        service = new ServiceDTO();
    }

    /**
     * WFS constructor.
     * 
     * <p>
     * Creates a copy of the WFS provided. If the WFS provided  is
     * null then default values are used. All the data structures are cloned.
     * </p>
     *
     * @param w The WFS to copy.
     */
    public WFSDTO(WFSDTO w) {
        if (w == null) {
            describeUrl = "";
            service = new ServiceDTO();

            return;
        }

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
     * @return A copy of this WFS
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new WFSDTO(this);
    }

    /**
     * Implement equals.
     * 
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The WFS object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        WFSDTO w = (WFSDTO) obj;

        return (describeUrl.equals(w.getDescribeUrl())
        && service.equals(w.getService()));
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
}
