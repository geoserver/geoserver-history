/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WMSDTO;
import java.util.Set;
import java.util.TreeSet;


/**
 * WMS purpose.
 * 
 * <p>
 * Description of WMS  Used to store WMS data.
 * </p>
 * 
 * <p></p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @version $Id: WMSConfig.java,v 1.6 2004/01/31 00:27:27 jive Exp $
 */
public class WMSConfig extends ServiceConfig {
    private static final String WMS_VERSION = "1.1.1";
    public static final String CONFIG_KEY = "Config.WMS";

    /**
     * This is a set of <code>dataStoreID.typeName</code> that is  enabled for
     * use with WMS.
     * 
     * <p>
     * You can use this information to bother DataConfig for the details such
     * as:
     * </p>
     * 
     * <ul>
     * <li>
     * Title
     * </li>
     * <li>
     * Abstract
     * </li>
     * </ul>
     * 
     * <p>
     * Cool?
     * </p>
     */
    private Set enabledFeatures = new TreeSet(); // keep sorted

    /** A string representing the update time of the WMS */
    private String updateTime;

    /**
     * WMS constructor.
     * 
     * <p>
     * Creates a WMS to represent an instance with default data.
     * </p>
     *
     * @see defaultSettings()
     */
    public WMSConfig() {
        super();
    }

    /**
     * WMS constructor.
     * 
     * <p>
     * Creates a copy of the WMSDTO provided. All the data structures are
     * cloned.
     * </p>
     *
     * @param w The WMSDTO to copy.
     */
    public WMSConfig(WMSDTO w) {
        super(w.getService());
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
    public void update(WMSDTO dto) {
        if (dto == null) {
            throw new NullPointerException("WMS Data Transfer Object required");
        }

        super.update(dto.getService());
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
    public WMSDTO toDTO() {
        WMSDTO wmsDto = new WMSDTO();
        wmsDto.setService((ServiceDTO) super.toServDTO());

        return wmsDto;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public String getUpdateTime() {
        return updateTime;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     */
    public void setUpdateTime(String string) {
        updateTime = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return
     */
    public Set getEnabledFeatures() {
        return enabledFeatures;
    }

    /**
     * DOCUMENT ME!
     *
     * @param set
     */
    public void setEnabledFeatures(Set set) {
        enabledFeatures = set;
    }
}
