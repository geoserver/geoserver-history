/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
/* Copyright (c) 2001 - 2004 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

/**
 * Data Transfer Object for communication GeoServer Web Map Server information.
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
 * @version $Id: WMSDTO.java,v 1.3 2004/01/21 00:26:09 dmzwiers Exp $
 */
public final class WMSDTO implements DataTransferObject {
    /** For the writer! */
    private boolean gmlPrefixing;

    /** The service parameters for this instance. */
    private ServiceDTO service;

    /**
     * WMS constructor.  does nothing
     */
    public WMSDTO() {
    }

    /**
     * WMS constructor.
     * 
     * <p>
     * Creates a copy of the WMS provided. If the WMS provided  is null then
     * default values are used. All the data structures are cloned.
     * </p>
     *
     * @param other The WMS to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public WMSDTO(WMSDTO other) {
        if (other == null) {
            throw new NullPointerException("Data Transfer Object required");
        }

        service = (ServiceDTO) other.getService().clone();
        gmlPrefixing = other.isGmlPrefixing();
    }

    /**
     * Implement clone as a DeepCopy.
     *
     * @return A Deep Copy of this WMSDTO
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
     * @param other The WMS object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof WMSDTO)) {
            return false;
        }

        WMSDTO dto = (WMSDTO) other;

        return ((gmlPrefixing == dto.gmlPrefixing) && (service == null))
        ? (dto.getService() == null) : service.equals(dto.getService());
    }

    /**
     * Implement hashCode.
     *
     * @return Service hashcode or 0
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (gmlPrefixing ? 1 : 0)
        | ((service == null) ? 0 : service.hashCode());
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
     * setService purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param service
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public void setService(ServiceDTO service) {
        if (service == null) {
            throw new NullPointerException("ServiceDTO required");
        }

        this.service = service;
    }

    /**
     * isGmlPrefixing purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public boolean isGmlPrefixing() {
        return gmlPrefixing;
    }

    /**
     * setGmlPrefixing purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param b
     */
    public void setGmlPrefixing(boolean b) {
        gmlPrefixing = b;
    }
}
