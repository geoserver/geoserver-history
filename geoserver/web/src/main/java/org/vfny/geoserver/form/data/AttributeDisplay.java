/* Copyright (c) 2001 - 2007 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.vfny.geoserver.config.AttributeTypeInfoConfig;


/**
 * Present Attribute information to user input.
 */
public class AttributeDisplay {
    private String name;
    private boolean nillable;
    private String minOccurs;
    private String maxOccurs;
    private String type;
    private String fragment;

    public AttributeDisplay(AttributeTypeInfoConfig config) {
        name = config.getName();
        nillable = config.isNillable();
        minOccurs = String.valueOf(config.getMinOccurs());
        maxOccurs = String.valueOf(config.getMaxOccurs());
        type = config.getType();
        fragment = config.getFragment();
    }

    /*public AttributeDisplay( AttributeTypeInfoDTO dto ){
        this( new AttributeTypeInfoConfig( dto ));
    }   */

    /**
     * @return Returns the fragment.
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * @return Returns the maxOccurs.
     */
    public String getMaxOccurs() {
        return maxOccurs;
    }

    /**
     * @return Returns the minOccurs.
     */
    public String getMinOccurs() {
        return minOccurs;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Returns the nillible.
     */
    public boolean isNillable() {
        return nillable;
    }

    /**
     * @return Returns the selectedType.
     */
    public String getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name + ":" + type;
    }
}
