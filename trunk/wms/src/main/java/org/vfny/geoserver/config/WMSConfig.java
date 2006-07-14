/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import org.vfny.geoserver.global.Config;
import org.vfny.geoserver.global.ConfigurationException;
import org.vfny.geoserver.global.WMS;
import org.vfny.geoserver.global.dto.ServiceDTO;
import org.vfny.geoserver.global.dto.WMSDTO;


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
 * @version $Id: WMSConfig.java,v 1.8 2004/04/01 23:24:17 emperorkefka Exp $
 */
public class WMSConfig extends ServiceConfig {
    private static final String WMS_VERSION = "1.1.1";
    public static final String CONFIG_KEY = "Config.WMS";

    /**
     * SVG renderers.
     */
    public static final String SVG_SIMPLE = "Simple";
    public static final String SVG_BATIK = "Batik";
    
    /** current svg renderer **/
    private String svgRenderer;
    /** anti aliasing hint for svg renderer **/
    private boolean svgAntiAlias;
    
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
        svgRenderer = SVG_SIMPLE;
        svgAntiAlias = true;
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
        svgRenderer = w.getSvgRenderer();
        svgAntiAlias = w.getSvgAntiAlias();
    }

    /**
     * Creates the WMSConfig.
     * 
     * @param wms The wms module.
     */
    public WMSConfig( WMS wms )  {
    		this( (WMSDTO) wms.toDTO() );
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
        svgRenderer = dto.getSvgRenderer();
        svgAntiAlias = dto.getSvgAntiAlias();
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
        wmsDto.setSvgRenderer(svgRenderer);
        wmsDto.setSvgAntiAlias(svgAntiAlias);
        return wmsDto;
    }
    
    /**
     * @return The constant identifying the current svg renderer.
     * @see org.vfny.geoserver.config.WMSConfig#SVG_SIMPLE
     * @see org.vfny.geoserver.config.WMSConfig#SVG_BATIK
     */
    public String getSvgRenderer() {
    	return svgRenderer;
    }
    
    /**
     * @param The constant identifying the current svg renderer.
     * @see org.vfny.geoserver.config.WMSConfig#SVG_SIMPLE
     * @see org.vfny.geoserver.config.WMSConfig#SVG_BATIK
     */
    public void setSvgRenderer(String svgRenderer) {
    	this.svgRenderer = svgRenderer;
    }
    
    /**
     * @param svgAntiAlias anti alias hint.
     */
    public void setSvgAntiAlias(boolean svgAntiAlias) {
    	this.svgAntiAlias = svgAntiAlias;
    }
    
    /**
     * @return The value of the anti aliasing rendering hint.
     */
    public boolean getSvgAntiAlias() {
    	return svgAntiAlias;
    }
}
