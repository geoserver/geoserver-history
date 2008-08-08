/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
 * @version $Id$
 */
public class WMSConfig extends ServiceConfig {
    private static final String WMS_VERSION = "1.1.1";
    public static final String CONFIG_KEY = "Config.WMS";

    /**
     * SVG renderers.
     */
    public static final String SVG_SIMPLE = "Simple";
    public static final String SVG_BATIK = "Batik";

    /**
     * Interpolation Types
     */
    public static final String INT_NEAREST = "Nearest";
    public static final String INT_BIlINEAR = "Bilinear";
    public static final String INT_BICUBIC = "Bicubic";

    /** current svg renderer **/
    private String svgRenderer;

    /** anti aliasing hint for svg renderer **/
    private boolean svgAntiAlias;

    /** global Watermarking **/
    private boolean globalWatermarking;

    /** global Watermarking URL **/
    private String globalWatermarkingURL;
    
    /** global Watermarking transparency **/
    private int watermarkTransparency;
    
    /** 
     * Watermark position
     * <pre>
     * O -- O -- O      0 -- 1 -- 2
     * |    |    |      |    |    |
     * O -- O -- O  ==  3 -- 4 -- 5
     * |    |    |      |    |    |
     * O -- O -- O      6 -- 7 -- 8
     * </pre>
     *
     */
    private int watermarkPosition;

    /** rendering interpolation **/
    private Map baseMapLayers;
    private Map baseMapStyles;
    private Map baseMapEnvelopes;
    private String allowInterpolation;

    /**
     * Set of EPSG codes used to limit the number of SRS elements displayed in
     * the GetCapabilities document
     */
    private Set/*<String>*/ capabilitiesCrs;

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
        globalWatermarking = false;
        globalWatermarkingURL = "";
        watermarkTransparency = 0;
        watermarkPosition = 8;
        allowInterpolation = INT_NEAREST;
        baseMapLayers = new HashMap();
        baseMapStyles = new HashMap();
        baseMapEnvelopes = new HashMap();
        capabilitiesCrs = new HashSet();
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
        globalWatermarking = w.getGlobalWatermarking();
        globalWatermarkingURL = w.getGlobalWatermarkingURL();
        watermarkTransparency = w.getWatermarkTransparency();
        watermarkPosition = w.getWatermarkPosition();
        allowInterpolation = w.getAllowInterpolation();
        baseMapLayers = w.getBaseMapLayers();
        baseMapStyles = w.getBaseMapStyles();
        baseMapEnvelopes = w.getBaseMapEnvelopes();
        capabilitiesCrs = w.getCapabilitiesCrs();
    }

    /**
     * Creates the WMSConfig.
     *
     * @param wms The wms module.
     */
    public WMSConfig(WMS wms) {
        this((WMSDTO) wms.toDTO());
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
        globalWatermarking = dto.getGlobalWatermarking();
        globalWatermarkingURL = dto.getGlobalWatermarkingURL();
        watermarkTransparency = dto.getWatermarkTransparency();
        watermarkPosition = dto.getWatermarkPosition();
        allowInterpolation = dto.getAllowInterpolation();
        baseMapLayers = dto.getBaseMapLayers();
        baseMapStyles = dto.getBaseMapStyles();
        baseMapEnvelopes = dto.getBaseMapEnvelopes();
        capabilitiesCrs = dto.getCapabilitiesCrs();
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
        wmsDto.setGlobalWatermarking(globalWatermarking);
        wmsDto.setGlobalWatermarkingURL(globalWatermarkingURL);
        wmsDto.setWatermarkTransparency(watermarkTransparency);
        wmsDto.setWatermarkPosition(watermarkPosition);
        wmsDto.setAllowInterpolation(allowInterpolation);
        wmsDto.setBaseMapLayers(baseMapLayers);
        wmsDto.setBaseMapStyles(baseMapStyles);
        wmsDto.setBaseMapEnvelopes(baseMapEnvelopes);
        wmsDto.setCapabilitiesCrs(capabilitiesCrs);
        
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

    /**
     * @param allowInterpolation rendering interpolation hint.
     */
    public void setAllowInterpolation(String allowInterpolation) {
        this.allowInterpolation = allowInterpolation;
    }

    /**
     * @return The value of the rendering interpolation rendering hint.
     */
    public String getAllowInterpolation() {
        return allowInterpolation;
    }

    /**
     * The comma separated list of feature types that make up the
     * base-map layer list.
     * @return
     */
    public Map getBaseMapLayers() {
        return baseMapLayers;
    }

    public void setBaseMapLayers(Map layers) {
        baseMapLayers = layers;
    }

    /**
     * The comma separated list of Styles that make up the
     * base-map style list.
     * @return
     */
    public Map getBaseMapStyles() {
        return baseMapStyles;
    }

    public void setBaseMapStyles(Map styles) {
        baseMapStyles = styles;
    }

    public Map getBaseMapEnvelopes() {
        return baseMapEnvelopes;
    }

    public void setBaseMapEnvelopes(Map envelopes) {
        baseMapEnvelopes = envelopes;
    }

    public boolean getGlobalWatermarking() {
        return globalWatermarking;
    }

    public void setGlobalWatermarking(boolean globalWatermarking) {
        this.globalWatermarking = globalWatermarking;
    }

    public String getGlobalWatermarkingURL() {
        return globalWatermarkingURL;
    }

    public void setGlobalWatermarkingURL(String globalWatermarkingURL) {
        this.globalWatermarkingURL = globalWatermarkingURL;
    }
    
    public int getWatermarkTransparency() {
        return watermarkTransparency;
    }

    public void setWatermarkTransparency(int watermarkTransparency) {
        this.watermarkTransparency = watermarkTransparency;
    }

    public int getWatermarkPosition() {
        return watermarkPosition;
    }

    public void setWatermarkPosition(int watermarkPosition) {
        this.watermarkPosition = watermarkPosition;
    }

    public Set getCapabilitiesCrs(){
        return new TreeSet(capabilitiesCrs);
    }
    
    public void setCapabilitiesCrs(Set crsList) {
        this.capabilitiesCrs = crsList == null ? new TreeSet() : new TreeSet(crsList);
    }
}
