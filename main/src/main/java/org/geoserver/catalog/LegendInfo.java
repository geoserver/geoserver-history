package org.geoserver.catalog;

import java.io.Serializable;

/**
 * A legend for a layer.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * 
 */
public interface LegendInfo extends Serializable {

    /**
     * Identifier.
     */
    String getId();
    
    /**
     * Width of the legend.
     * 
     * @uml.property name="width"
     */
    int getWidth();

    /**
     * Sets width of the legend.
     * 
     * @uml.property name="width"
     */
    void setWidth(int width);

    /**
     * Height of the legend.
     * 
     * @uml.property name="height"
     */
    int getHeight();

    /**
     * Sets height of the legend.
     * 
     * @uml.property name="height"
     */
    void setHeight(int height);

    /**
     * Format of the legend.
     * 
     * @uml.property name="format"
     */
    String getFormat();

    /**
     * Sets format of the legend.
     * 
     * @uml.property name="format"
     */
    void setFormat(String format);

    /**
     * Online resource of the legend.
     * 
     * @uml.property name="onlineResource"
     */
    String getOnlineResource();

    /**
     * Sets online resource of the legend.
     * 
     * @uml.property name="onlineResource"
     */
    void setOnlineResource(String onlineResource);

}
