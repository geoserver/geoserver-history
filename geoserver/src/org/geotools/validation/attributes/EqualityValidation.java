/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geotools.validation.attributes;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.filter.Filter;
import org.geotools.validation.FeatureValidation;
import org.geotools.validation.ValidationResults;
import java.util.logging.Logger;


/**
 * Tests to see if an attribute is equal to a provided value.
 * 
 * <p>
 * I can only see this test being useful if a Filter is also used.
 * Online research shows that this test is used in the wild, so we are
 * adding it into our system.
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: EqualityValidation.java,v 1.1 2004/01/31 00:24:07 jive Exp $
 */
public class EqualityValidation implements FeatureValidation {
    /** The logger for the validation module. */
    private static final Logger LOGGER = Logger.getLogger(
            "org.geotools.validation");

    /** User's Name of this validation test. */
    private String name; // name of the validation

    /** User's description of this validation test. */
    private String description;

    /**
     * Identification of required FeatureType as dataStoreId:typeName.
     * 
     * <p>
     * The provided ValidationProcessor assumes that FeatureTypes will be
     * references will be of the form dataStoreId:typeName.
     * </p>
     * 
     * <p>
     * If "" or null is used All FetureTypes will be checked.
     * </p>
     */
    private String typeRef;

    private String attributeName;
    
    /** Expected value that attribute are supposed to equal */
    private Object expected;
    
    /** Filter used to limit the number of Features we check */
    private Filter filter = Filter.NONE; 
    
    /**
     * No argument constructor, required by the Java Bean Specification.
     */
    public EqualityValidation() {
    }

    /**
     * Sets the name of this validation.
     *
     * @param name The name of this validation.
     *
     * @see org.geotools.validation.Validation#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Access the user's name for this test.
     *
     * @return The name of this validation.
     *
     * @see org.geotools.validation.Validation#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the description of this validation.
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
        if( typeRef == null ){
            return null;
        }
        if( typeRef.equals("*")){
            return ALL;
        }
        return new String[]{ typeRef, };
    }

    /**
     * Access typeRef property.
     *
     * @return Returns the typeName.
     */
    public String getTypeRef() {
        return typeRef;
    }

    /**
     * Set typeRef to typeName.
     *
     * @param typeName The typeName to set.
     */
    public void setTypeRef(String typeRef) {
        this.typeRef = typeRef;
    }

    /**
     * Validation test for feature.
     * 
     * <p>
     * Description of test ...
     * </p>
     *
     * @param feature The Feature to be validated
     * @param type The FeatureType of the feature
     * @param results The storage for error messages.
     *
     * @return <code>true</code> if the feature is a valid geometry.
     *
     * @see org.geotools.validation.FeatureValidation#validate
     */
    public boolean validate(Feature feature, FeatureType type,
        ValidationResults results) {
        
        if( !filter.contains( feature )){
            return true;
        }
        Object actual = feature.getAttribute( attributeName );
        if( expected.equals( actual )){
            return true;
        }
        results.error( feature, attributeName+" did not not equals "+expected );
        return false;
    }
}
