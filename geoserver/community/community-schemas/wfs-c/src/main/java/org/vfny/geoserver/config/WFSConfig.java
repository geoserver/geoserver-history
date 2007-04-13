/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.geoserver.wfs.WFS;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WFSDTO;


/**
 * WFS purpose.
 *
 * <p>
 * Description of WFS  Used to store WFS data.
 * </p>
 *
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id$
 */
public class WFSConfig extends ServiceConfig {
    public static final String CONFIG_KEY = "Config.WFS";
    private int serviceLevel;
    private boolean citeConformanceHacks = false; // see WFSDTO for more info
    private boolean featureBounding = false;
    private boolean srsXmlStyle = true;

    /**
     * WFS constructor.
     *
     * <p>
     * Creates a WFS to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public WFSConfig() {
        super();
        srsXmlStyle = true;
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
    public WFSConfig(WFSDTO w) {
        super(w.getService());
        serviceLevel = w.getServiceLevel();
        srsXmlStyle = w.isSrsXmlStyle();
        citeConformanceHacks = w.getCiteConformanceHacks();
        featureBounding = w.isFeatureBounding();
    }

    /**
     * Creates the WFSConfig.
     *
     * @param wfs The wfs module.
     *
     */
    public WFSConfig(WFS wfs) {
        this((WFSDTO) wfs.toDTO());
    }

    /**
     * Implement loadDTO.
     *
     * <p>
     * Takes a WMSDTO and loads it into this WMSConfig Object
     * </p>
     *
     * @param dto an instance of WMSDTO
     *
     * @throws NullPointerException DOCUMENT ME!
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(WFSDTO dto) {
        if (dto == null) {
            throw new NullPointerException("WFS Data Transfer Object required");
        }

        super.update(dto.getService());
        srsXmlStyle = dto.isSrsXmlStyle();
        serviceLevel = dto.getServiceLevel();

        citeConformanceHacks = dto.getCiteConformanceHacks();
        featureBounding = dto.isFeatureBounding();
    }

    /**
     * Implement toDTO.
     *
     * <p>
     * Returns a copy of the data in a ServiceDTO object
     * </p>
     *
     * @return a copy of the data in a ServiceDTO object
     *
     * @see org.vfny.geoserver.config.DataStructure#toDTO()
     */
    public WFSDTO toDTO() {
        WFSDTO wfsDto = new WFSDTO();
        wfsDto.setService((ServiceDTO) super.toServDTO());
        wfsDto.setServiceLevel(serviceLevel);
        wfsDto.setSrsXmlStyle(srsXmlStyle);

        wfsDto.setCiteConformanceHacks(citeConformanceHacks);
        wfsDto.setFeatureBounding(featureBounding);

        return wfsDto;
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
