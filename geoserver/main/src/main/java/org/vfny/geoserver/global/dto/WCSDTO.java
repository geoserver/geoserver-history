/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;


/**
 * Data Transfer Object for communication GeoServer Web Coverage Server information.
 *
 * <p>
 * Information required for GeoServer to set up a Web Coverage Service.
 * </p>
 *
 * <p>
 * Data Transfer object are used to communicate between the GeoServer
 * application and its configuration and persistent layers. As such the class
 * is final - to allow for its future use as an on-the-wire message.
 * </p>
 *
 * @author $Author: Alessio Fabiani (alessio.fabiani@gmail.com) $ (last modification)
 * @author $Author: Simone Giannecchini (simboss1@gmail.com) $ (last modification)
 * @version $Id$
 */
public class WCSDTO implements DataTransferObject {
    /**
     * For the writer!
     *
     * @uml.property name="gmlPrefixing" multiplicity="(0 1)"
     */
    private boolean gmlPrefixing;

    /**
     * The service parameters for this instance.
     *
     * @uml.property name="service"
     * @uml.associationEnd multiplicity="(0 1)"
     */
    private ServiceDTO service;

    /**
     * WCS constructor.  does nothing
     */
    public WCSDTO() {
    }

    /**
     * WCS constructor.
     *
     * <p>
     * Creates a copy of the WCS provided. If the WCS provided  is null then
     * default values are used. All the data structures are cloned.
     * </p>
     *
     * @param other The WCS to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public WCSDTO(WCSDTO other) {
        if (other == null) {
            throw new NullPointerException("Data Transfer Object required");
        }

        service = (ServiceDTO) other.getService().clone();
        gmlPrefixing = other.isGmlPrefixing();
    }

    /**
     * Implement clone as a DeepCopy.
     *
     * @return A Deep Copy of this WCSDTO
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new WCSDTO(this);
    }

    /**
     * Implement equals.
     *
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param other The WCS object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof WCSDTO)) {
            return false;
        }

        WCSDTO dto = (WCSDTO) other;

        return ((gmlPrefixing == dto.gmlPrefixing) && (service == null)) ? (dto.getService() == null)
                                                                         : service.equals(dto
            .getService());
    }

    /**
     * Implement hashCode.
     *
     * @return Service hashcode or 0
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (gmlPrefixing ? 1 : 0) | ((service == null) ? 0 : service.hashCode());
    }

    /**
     * getService purpose.
     *
     * <p>
     * Description ...
     * </p>
     *
     * @return
     *
     * @uml.property name="service"
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
     *
     * @uml.property name="service"
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
     *
     * @uml.property name="gmlPrefixing"
     */
    public void setGmlPrefixing(boolean b) {
        gmlPrefixing = b;
    }
}
