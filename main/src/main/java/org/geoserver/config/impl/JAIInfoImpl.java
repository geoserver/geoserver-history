/* Copyright (c) 2001 - 2008 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.config.impl;

import java.io.Serializable;

import javax.media.jai.JAI;

import org.geoserver.config.JAIInfo;

import com.sun.media.jai.util.SunTileCache;

public class JAIInfoImpl implements Serializable, JAIInfo {

    public static final String KEY = "jai.info";
    
    private static final long serialVersionUID = 7121137497699361776L;
    
    transient JAI jai;
    transient SunTileCache tileCache;
    
    boolean allowInterpolation;
    
    public static final boolean DEFAULT_Recycling = false;
    boolean recycling = DEFAULT_Recycling;
    
    public static final int DEFAULT_TilePriority = Thread.NORM_PRIORITY;
    int tilePriority = DEFAULT_TilePriority;
    
    public static final int DEFAULT_TileThreads = 7;
    int tileThreads = DEFAULT_TileThreads;
    
    public static final double DEFAULT_MemoryCapacity = 0.5;
    double memoryCapacity = DEFAULT_MemoryCapacity;
    
    public static final double DEFAULT_MemoryThreshold = 0.75;
    double memoryThreshold = DEFAULT_MemoryThreshold;
    
    public static final boolean DEFAULT_ImageIOCache = false;
    boolean imageIOCache = DEFAULT_ImageIOCache;
    
    public static final boolean DEFAULT_PNGNative = false;
    boolean pngAcceleration = DEFAULT_PNGNative;
    
    public static final boolean DEFAULT_JPEGNative = false;
    boolean jpegAcceleration = DEFAULT_JPEGNative;
    
    public static final boolean DEFAULT_MosaicNative = false;
    boolean allowNativeMosaic = DEFAULT_MosaicNative;

    /**
     * @uml.property name="allowInterpolation"
     */
    public boolean getAllowInterpolation() {
        return allowInterpolation;
    }

    /**
     * @uml.property name="allowInterpolation"
     */
    public void setAllowInterpolation(boolean allowInterpolation) {
        this.allowInterpolation = allowInterpolation;
    }

    /**
     * @uml.property name="recycling"
     */
    public boolean isRecycling() {
        return recycling;
    }

    /**
     * @uml.property name="recycling"
     */
    public void setRecycling(boolean recycling) {
        this.recycling = recycling;
    }

    /**
     * @uml.property name="tilePriority"
     */
    public int getTilePriority() {
        return tilePriority;
    }

    /**
     * @uml.property name="tilePriority"
     */
    public void setTilePriority(int tilePriority) {
        this.tilePriority = tilePriority;
    }

    /**
     * @uml.property name="tileThreads"
     */
    public int getTileThreads() {
        return tileThreads;
    }

    /**
     * @uml.property name="tileThreads"
     */
    public void setTileThreads(int tileThreads) {
        this.tileThreads = tileThreads;
    }
        
    /**
     * @uml.property name="memoryCapacity"
     */
    public double getMemoryCapacity() {
        return memoryCapacity;
    }

    /**
     * @uml.property name="memoryCapacity"
     */
    public void setMemoryCapacity(double memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    /**
     * @uml.property name="memoryThreshold"
     */
    public double getMemoryThreshold() {
        return memoryThreshold;
    }

    /**
     * @uml.property name="memoryThreshold"
     */
    public void setMemoryThreshold(double memoryThreshold) {
        this.memoryThreshold = memoryThreshold;
    }
    
    public boolean isPngAcceleration() {
        return pngAcceleration;
    }
    
    public void setPngAcceleration( boolean pngAcceleration ) {
        this.pngAcceleration = pngAcceleration;
    }
    
    public boolean isJpegAcceleration() {
        return jpegAcceleration;
    }
    
    public void setJpegAcceleration( boolean jpegAcceleration ) {
        this.jpegAcceleration = jpegAcceleration;
    }
    
    public void setImageIOCache(boolean imageIOCache) {
        this.imageIOCache = imageIOCache;
    }
    
    public boolean isImageIOCache() {
        return imageIOCache;
    }
    
    public JAI getJAI() {
        return jai;
    }
    
    public void setJAI( JAI jai ) {
        this.jai = jai;
    }
    
    public SunTileCache getTileCache() {
        return tileCache;
    }
    
    public void setTileCache( SunTileCache tileCache ) {
        this.tileCache = tileCache;
    }

	public boolean isAllowNativeMosaic() {
		return allowNativeMosaic;
	}

	public void setAllowNativeMosaic(boolean allowNativeMosaic) {
		this.allowNativeMosaic = allowNativeMosaic;
	}
    
}
