/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geotools.validation.attributes;

import java.util.Map;
import java.util.logging.Logger;

import org.geotools.validation.IntegrityValidation;
import org.geotools.validation.ValidationResults;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Tests to that an attribute's value is unique across the entire FeatureType.
 * <p>
 * For a starting point you may want to look at UniqueFIDIntegrityValidation
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: UniquityValidation.java,v 1.1 2004/01/31 00:24:07 jive Exp $
 */
public class UniquityValidation implements IntegrityValidation {
    
    /** The logger for the validation module. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.geotools.validation");

    /**
     * User's Name of this integrity test.
     */
    private String name; 

    /**
     * User's description of this integrity test.
     */
    private String description;

    /** TypeRef is dataStoreId:typeName */
    private String typeRef;
    
    /** Attribute name to check for uniquity */
    private String attributeName;
    /**
     * No argument constructor, required by the Java Bean Specification.
     */
    public UniquityValidation() {
    }

    /**
     * Override setName.
     * 
     * <p>
     * Sets the name of this validation.
     * </p>
     *
     * @param name The name of this validation.
     *
     * @see org.geotools.validation.Validation#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Override getName.
     * 
     * <p>
     * Returns the name of this particular validation.
     * </p>
     *
     * @return The name of this particular validation.
     *
     * @see org.geotools.validation.Validation#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * Override setDescription.
     * 
     * <p>
     * Sets the description of this validation.
     * </p>
     *
     * @param description The description of the validation.
     *
     * @see org.geotools.validation.Validation#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Override getDescription.
     * 
     * <p>
     * Returns the description of this validation as a string.
     * </p>
     *
     * @return The description of this validation.
     *
     * @see org.geotools.validation.Validation#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /**
     * The priority level used to schedule this Validation.
     *
     * @return PRORITY_SIMPLE
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return PRIORITY_SIMPLE;
    }

    /**
     * Implementation of getTypeNames.
     *
     * @return Array of typeNames, or empty array for all, null for disabled
     *
     * @see org.geotools.validation.Validation#getTypeRefs()
     */
    public String[] getTypeRefs() {
        return null; // disabled by default
    }

    /**
     * Check FeatureType for ...
     * <p>
     * Detailed description...
     * </p>
     * 
     * @param layers Map of FeatureSource by "dataStoreID:typeName"
     * @param envelope The bounding box that encloses the unvalidated data
     * @param results Used to coallate results information
     *
     * @return <code>true</code> if all the features pass this test.
     */
    public boolean validate(Map layers, Envelope envelope, ValidationResults results) throws Exception {
        return false;
    }
}
