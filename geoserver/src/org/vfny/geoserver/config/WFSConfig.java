/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WFSDTO;

import java.util.Set;
import java.util.TreeSet;


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
 * @version $Id: WFSConfig.java,v 1.2.2.9 2004/01/09 18:27:29 dmzwiers Exp $
 */
public class WFSConfig extends ServiceConfig {
    public static final String CONFIG_KEY = "Config.WFS";

	private boolean gmlPrefixing;
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
		gmlPrefixing = w.isGmlPrefixing();
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
     * @return false when obj null, or not correct class instance.
     *
     * @see org.vfny.geoserver.config.DataStructure#loadDTO(java.lang.Object)
     */
    public void update(WFSDTO dto) {
        if (dto == null) {
			throw new NullPointerException("WFS Data Transfer Object required");
        }
        super.update(dto.getService());
        gmlPrefixing = dto.isGmlPrefixing();
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
		wfsDto.setGmlPrefixing(gmlPrefixing);
        return wfsDto;
    }

	/**
	 * @return
	 */
	public Set getEnabledFeatures() {
		return enabledFeatures;
	}

	/**
	 * @param set
	 */
	public void setEnabledFeatures(Set set) {
		enabledFeatures = set;
	}

	/**
	 * isGmlPrefixing purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @return
	 */
	public boolean isGmlPrefixing() {
		return gmlPrefixing;
	}

	/**
	 * setGmlPrefixing purpose.
	 * <p>
	 * Description ...
	 * </p>
	 * @param b
	 */
	public void setGmlPrefixing(boolean b) {
		gmlPrefixing = b;
	}

}
