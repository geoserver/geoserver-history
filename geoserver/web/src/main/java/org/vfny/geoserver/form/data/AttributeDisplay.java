/* Copyright (c) 2001, 2003 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.vfny.geoserver.form.data;

import org.vfny.geoserver.config.AttributeTypeInfoConfig;


/**
 * Present Attribute information to user input.
 */
public class AttributeDisplay {
    private static final String NAMESPACE_SEPARATOR = ":"; //$NON-NLS-1$
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
     *
    DOCUMENT ME!
     *
     * @return Returns the fragment.
     */
    public String getFragment() {
        return fragment;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the maxOccurs.
     */
    public String getMaxOccurs() {
        return maxOccurs;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the minOccurs.
     */
    public String getMinOccurs() {
        return minOccurs;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the nillible.
     */
    public boolean isNillable() {
        return nillable;
    }

    /**
     *
    DOCUMENT ME!
     *
     * @return Returns the selectedType.
     */
    public String getType() {
        return type;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return name + NAMESPACE_SEPARATOR + type;
    }
}
