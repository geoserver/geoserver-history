/*
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */
package org.geoserver.data.feature;

/**
 * This class represents legend icon parameters.
 *
 * @author Charles Kolbowicz
 * @version $Id$
 */
public class LegendURL {
	
    /** Holds value of legend icon width. */
    private int width;

    /** Holds value of legend icon height. */
    private int height;

    /** Holds value of legend icon format. */
    private String format;

    /** Holds value of legend icon onlineResource. */
    private String onlineResource;

    /**
     * Getter for legend icon width.
     *
     * @return Value of property width.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Getter for  legend icon height.
     *
     * @return Value of  legend icon height.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Getter for legend icon format.
     *
     * @return Value of  legend icon format.
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * Getter for  legend icon onlineResource.
     *
     * @return Value of  legend icon onlineResource.
     */
    public String getOnlineResource() {
        return this.onlineResource;
    }
}
