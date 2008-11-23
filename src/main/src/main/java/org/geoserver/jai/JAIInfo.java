package org.geoserver.jai;

import java.io.Serializable;

import javax.media.jai.JAI;

import com.sun.media.jai.util.SunTileCache;

public class JAIInfo implements Serializable {

    public static String KEY = "jai.info";
    
    private static final long serialVersionUID = 7121137497699361776L;
    
    transient JAI jai;
    transient SunTileCache tileCache;
    
    boolean allowInterpolation;
    boolean recycling;
    int tilePriority;
    int tileThreads;
    double memoryCapacity;
    double memoryThreshold;
    boolean imageIOCache;
    boolean pngAcceleration;
    boolean jpegAcceleration;

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
    public boolean getRecycling() {
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
    
    public boolean getPNGAcceleration() {
        return pngAcceleration;
    }
    
    public void setPNGAcceleration( boolean pngAcceleration ) {
        this.pngAcceleration = pngAcceleration;
    }
    
    public boolean getJPEGAcceleration() {
        return jpegAcceleration;
    }
    
    public void setJPEGAcceleration( boolean jpegAcceleration ) {
        this.jpegAcceleration = jpegAcceleration;
    }
    
    public void setImageIOCache(boolean imageIOCache) {
        this.imageIOCache = imageIOCache;
    }
    
    public boolean getImageIOCache() {
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
    
}
