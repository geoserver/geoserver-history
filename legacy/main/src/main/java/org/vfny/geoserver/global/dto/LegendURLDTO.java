/* This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.global.dto;


/**
 * Data Transfer Object for legend information.
 *
 * <p>
 * Defines the legend icon to be associated with a FeatureType.
 * </p>
 *
 * <p>
 * <pre>
 * Example:<code>
 * LegendDTO legend = new LegendDTO();
 * legend.setOnlineResource("http://www.myserver.org/legends/legend1.png");
 * legend.setWidth(72);
 * legend.setHeight(72);
 * legend.setFormat("image/png");
 * </code>
 *  </pre>
 * </p>
 *
 * @author Charles Kolbowicz, Institut de Recherche pour le D�veloppement
 *         (IRD), 2004
 * @version $Id$
 */
public final class LegendURLDTO implements DataTransferObject {
    /** The legend icon width. */
    private int width;

    /** The legend icon height. */
    private int height;

    /** The legend icon format; */
    private String format;

    /** The legend icon location. */
    private String onlineResource;

    /**
     * LegendConfig constructor.
     *
     * <p>
     * does nothing
     * </p>
     */
    public LegendURLDTO() {
    }

    /**
     * LegendConfig constructor.
     *
     * <p>
     * Creates a copy of the LegendConfig provided. If the LegendConfig
     * provided is null then default values are used. All the data structures
     * are cloned.
     * </p>
     *
     * @param legend The legend to copy.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public LegendURLDTO(LegendURLDTO legend) {
        if (legend == null) {
            throw new NullPointerException();
        }

        format = legend.getFormat();
        width = legend.getWidth();
        height = legend.getHeight();
        onlineResource = legend.getOnlineResource();
    }
    
    /**
     * Implement clone.
     *
     * <p>
     * creates a clone of this object
     * </p>
     *
     * @return A copy of this LegendURLDTO
     *
     * @see java.lang.Object#clone()
     */
    public Object clone() {
        return new LegendURLDTO(this);
    }

    /**
     * Implement equals.
     *
     * <p>
     * recursively tests to determine if the object passed in is a copy of this
     * object.
     * </p>
     *
     * @param obj The LegendURLDTO object to test.
     *
     * @return true when the object passed is the same as this object.
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if ((obj == null) || !(obj instanceof LegendURLDTO)) {
            return false;
        }

        LegendURLDTO legend = (LegendURLDTO) obj;
        boolean r = true;
        r = r && (width == legend.getWidth());

        r = r && (height == legend.getHeight());

        r = r && (format.equals(legend.getFormat()));

        r = r && (onlineResource.equals(legend.getOnlineResource()));

        return r;
    }

    /**
     * Implement hashCode.
     *
     * @return Service hashcode or 0
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        int r = 1;

        r *= (width * height);

        if (format != null) {
            r *= format.hashCode();
        }

        if (onlineResource != null) {
            r *= onlineResource.hashCode();
        }

        return r;
    }

    public String toString() {
        return "LegendURL [" + width + ", " + height + "] - Type : " + format + " -  Location : "
        + onlineResource + "\n";
    }

    /**
     * Getter for legend icon width.
     *
     * @return Value of legend icon width;
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Setter for legend icon width.
     *
     * @param width New value of legend icon width.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Getter for legend icon height.
     *
     * @return Value of legend icon height.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Setter for legend icon height.
     *
     * @param height New value of legend icon height.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Getter for legend icon format.
     *
     * @return Value of legend icon format.
     */
    public String getFormat() {
        return this.format;
    }

    /**
     * Setter for legend icon format.
     *
     * @param format New value of legend icon format.
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Getter for property onlineResource.
     *
     * @return Value of property onlineResource.
     */
    public String getOnlineResource() {
        return this.onlineResource;
    }

    /**
     * Setter for legend icon onlineResource.
     *
     * @param onlineResource New value of legend icon onlineResource.
     */
    public void setOnlineResource(String onlineResource) {
        this.onlineResource = onlineResource;
    }
}
