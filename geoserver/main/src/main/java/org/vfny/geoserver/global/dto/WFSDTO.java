/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
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
 * @version $Id$
 */
public final class WFSDTO implements DataTransferObject {
    /** ServiceLevel bit used to indicate Basic support */
    public static final int SERVICE_BASIC = 1;

    /** ServiceLevel bit used to indicate Transaction Insert support */
    public static final int SERVICE_INSERT = 2;

    /** ServiceLevel bit used to indicate Transaction Update support */
    public static final int SERVICE_UPDATE = 4;

    /** ServiceLevel bit used to indicate Transaction Delete support */
    public static final int SERVICE_DELETE = 8;

    /** ServiceLevel bit used to indicate Locking support */
    public static final int SERVICE_LOCKING = 16;

    /** ServiceLevel mask equivilent to basic WFS conformance */
    public static final int BASIC = 1;

    /** ServiceLevel mask for transactional WFS conformance. */
    public static final int TRANSACTIONAL = SERVICE_BASIC | SERVICE_INSERT | SERVICE_UPDATE
        | SERVICE_DELETE;

    /** ServiceLevel mask equivilent to complete WFS conformance */
    public static final int COMPLETE = TRANSACTIONAL | SERVICE_LOCKING;

    /** The service parameters for this instance. */
    private ServiceDTO service;
    private boolean srsXmlStyle = true;
    private int serviceLevel = COMPLETE; //if not set then it should be complete
    private boolean featureBounding = true;

    /**
     * ie. citeConformanceHacks value equals true
     *
     * <p>
     * Currently there are certain legal things in the Geoserver WFS
     * GetCapabilities response that the CITE tests throw error if it finds.
     * An example of this is the supported GetFeature output formats.  CITE
     * only allows GML2, GML2-ZIP, and SHAPE-ZIP. We support GML2, GML2-GZIP,
     * GML2-ZIP, and SHAPE-ZIP, so CITE tests will complain that we're not
     * allowed to support GML2-GZIP! By setting this option to "true" the
     * GetCapabilities response will NOT show we support those extra
     * dataformats. In the future we may find other "silly" things.
     * </p>
     */
    private boolean citeConformanceHacks = false; //default to normal operations

    /**
     * WFS Data Transfer Object constructor.  does nothing
     */
    public WFSDTO() {
    }

    /**
     * WFS constructor.
     *
     * <p>
     * Creates a copy of the WFS provided. If the WFS provided  is null then
     * default values are used. All the data structures are cloned.
     * </p>
     *
     * @param other The WFS to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public WFSDTO(WFSDTO other) {
        if (other == null) {
            throw new NullPointerException("Data Transfer Object required");
        }

        service = (ServiceDTO) new ServiceDTO(other.getService());
        featureBounding = other.isFeatureBounding();
        serviceLevel = other.getServiceLevel();
        citeConformanceHacks = other.getCiteConformanceHacks();
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
     *
     * @return true when the object passed is equal to this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object other) {
        if ((other == null) || !(other instanceof WFSDTO)) {
            return false;
        }

        WFSDTO dto = (WFSDTO) other;

        if (citeConformanceHacks != dto.getCiteConformanceHacks()) {
            return false;
        }

        if (featureBounding != dto.isFeatureBounding()) {
            return false;
        }

        return (((serviceLevel == dto.getServiceLevel()) && (service == null))
        ? (dto.getService() == null) : service.equals(dto.getService()));
    }

    /**
     * Implement hashCode.
     *
     * @return Service hashcode or 0
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (service == null) ? 0 : service.hashCode();
    }

    /**
     * Provides access to the Service DTO object.
     *
     * <p>
     * Note well that this is the internal ServiceDTO object used by the WFSDTO
     * - any changes made to the result of this method will change the state
     * of this WFSDTO object.
     * </p>
     *
     * @return ServericeDTO used by this WFSDTO
     */
    public ServiceDTO getService() {
        return service;
    }

    /**
     * Set this WFS Data Tranfer Object to use the provided Service DTO.
     *
     * <p>
     * A copy of the provided dto is made.
     * </p>
     *
     * @param dto ServiceDTO used to configure this WFSDTO
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public void setService(ServiceDTO dto) {
        if (dto == null) {
            throw new NullPointerException("ServiceDTO requrired");
        }

        service = dto;
    }

    /**
     * Whether the srs xml attribute should be in the EPSG:4326 (non-xml)
     * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326 style.
     *
     * @return <tt>true</tt> if the srs is reported with the xml style
     */
    public boolean isSrsXmlStyle() {
        return srsXmlStyle;
    }

    /**
     * Sets whether the srs xml attribute should be in the EPSG:4326 (non-xml)
     * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326 style.
     *
     * @param doXmlStyle whether the srs style should be xml or not.
     */
    public void setSrsXmlStyle(boolean doXmlStyle) {
        this.srsXmlStyle = doXmlStyle;
    }

    /**
     * Access serviceLevel property.
     *
     * @return Returns the serviceLevel.
     */
    public int getServiceLevel() {
        return serviceLevel;
    }

    /**
     * Set serviceLevel to serviceLevel.
     *
     * @param serviceLevel The serviceLevel to set.
     */
    public void setServiceLevel(int serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    /**
     * turn on/off the citeConformanceHacks option.
     *
     * @param on
     */
    public void setCiteConformanceHacks(boolean on) {
        citeConformanceHacks = on;
    }

    /**
     * get the current value of the citeConformanceHacks
     *
     * @return
     */
    public boolean getCiteConformanceHacks() {
        return (citeConformanceHacks);
    }

    /**
     * Returns whether the gml returned by getFeature includes an
     * auto-calculated bounds element on each feature or not.
     *
     * @return <tt>true</tt> if the gml features will have boundedBy
     *         automatically generated.
     */
    public boolean isFeatureBounding() {
        return featureBounding;
    }

    /**
     * Sets whether the gml returned by getFeature includes an auto-calculated
     * bounds element on each feature or not.
     *
     * @param featureBounding <tt>true</tt> if gml features should have
     *        boundedBy automatically generated.
     */
    public void setFeatureBounding(boolean featureBounding) {
        this.featureBounding = featureBounding;
    }
}
