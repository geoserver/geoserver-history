/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

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
 * @version $Id: WFSConfig.java,v 1.10 2004/09/09 17:05:10 cholmesny Exp $
 */
public class WFSConfig extends ServiceConfig {
    public static final String CONFIG_KEY = "Config.WFS";
    private boolean gmlPrefixing;
    
    private int serviceLevel;
    
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
        gmlPrefixing = w.isGmlPrefixing();
        serviceLevel = w.getServiceLevel();
        srsXmlStyle = w.isSrsXmlStyle();
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
        gmlPrefixing = dto.isGmlPrefixing();
        srsXmlStyle = dto.isSrsXmlStyle();
        serviceLevel = dto.getServiceLevel();
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
        wfsDto.setServiceLevel(serviceLevel);
        wfsDto.setSrsXmlStyle(srsXmlStyle);
        return wfsDto;
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
	 * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326
	 * style.  
	 *
	 * @return <tt>true</tt> if the srs is reported with the xml style
	 */
	public boolean isSrsXmlStyle() {
		return srsXmlStyle;
	}

	/**
	 * Sets whether the srs xml attribute should be in the EPSG:4326 (non-xml)
	 * style, or in the http://www.opengis.net/gml/srs/epsg.xml#4326
	 * style.  
	 *
	 * @param doXmlStyle whether the srs style should be xml or not.
	 */
	public void setSrsXmlStyle(boolean doXmlStyle) {
		this.srsXmlStyle = doXmlStyle;
	}

}
