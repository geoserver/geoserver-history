/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;

/**
 * Data Transfer Object for communication with GeoServer's Web Feature Service.
 * 
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WFSDTO.java,v 1.1.2.4 2004/01/09 09:52:44 jive Exp $
 */
public final class WFSDTO implements DataTransferObject {

    /** The service parameters for this instance. */
    private ServiceDTO service;

    /**
     * WFS Data Transfer Object constructor.
     */
    public WFSDTO() {
        service = new ServiceDTO();
    }

    /**
     * WFS constructor.
     * 
     * <p>
     * Creates a copy of the WFS provided. If the WFS provided  is null then
     * default values are used. All the data structures are cloned.
     * </p>
     *
     * @param w The WFS to copy.
     */
    public WFSDTO(WFSDTO other) {
        if (other == null) {
            throw new NullPointerException("Data Transfer Object required");            
        }
        service = (ServiceDTO) new ServiceDTO( other.getService() );
    }

    /**
     * Implement clone as a DeepCopy.
     *
     * @return A Deep Copy of this WFSDTO
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new WFSDTO(this);
    }

    /**
     * Implement equals.
     * 
     * @param other Other object to test for equality
     * @return true when the object passed is equal to this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if( other == null || !(other instanceof WFSDTO) ){
            return false;
        }
        WFSDTO dto = (WFSDTO) other;
        return service == null ? dto.getService() == null
                               : service.equals( dto.getService() );
    }

    /**
     * Implement hashCode.
     * 
     * @see java.lang.Object#hashCode()
     * 
     * @return Service hashcode or 0
     */
    public int hashCode() {
        return service == null ? 0 : service.hashCode();
    }

    /**
     * Provides access to the Service DTO object.
     * <p>
     * Note well that this is the internal ServiceDTO object used
     * by the WFSDTO - any changes made to the result of this method will
     * change the state of this WFSDTO object.
     * </p>
     * @return ServericeDTO used by this WFSDTO
     */
    public ServiceDTO getService() {
        return service;
    }

    /**
     * Set this WFS Data Tranfer Object to use the provided Service DTO.
     * <p>
     * A copy of the provided dto is made.
     * </p>
     *
     * @param dto ServiceDTO used to configure this WFSDTO
     */
    public void setService(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException("ServiceDTO requrired");            
        }
        service = dto;    
    }
}
