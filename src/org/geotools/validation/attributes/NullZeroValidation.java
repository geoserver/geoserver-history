/* Copyright (c) 2001, 2003 TOPP - www.openplans.org.  All rights reserved.
 * This code is licensed under the GPL 2.0 license, availible at the root
 * application directory.
 */
package org.geotools.validation.attributes;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.validation.FeatureValidation;
import org.geotools.validation.ValidationResults;


/**
 * NullZeroFeatureValidation purpose.
 * 
 * <p>
 * Description of NullZeroFeatureValidation ...
 * </p>
 * 
 * <p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * Tests for null/0 atribute values.
 * </li>
 * </ul>
 * 
 * Example Use:
 * <pre><code>
 * NullZeroFeatureValidation x = new NullZeroFeatureValidation(...);
 * </code></pre>
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: jive $ (last modification)
 * @version $Id: NullZeroValidation.java,v 1.1 2004/01/31 00:24:07 jive Exp $
 */
public class NullZeroValidation implements FeatureValidation {
    private String attributeName;
    private String name = "";
    private String description = "";
    
    private String typeRef;

    public NullZeroValidation(){super();}
    
    /**
     * Implement validate.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param feature Provides the attributes to test.
     * @param type not used.
     * @param results a reference for returning error codes.
     *
     * @return false when null or 0 values are found in the attribute.
     *
     * @throws Exception
     *
     * @see org.geotools.validation.FeatureValidation#validate(org.geotools.feature.Feature,
     *      org.geotools.feature.FeatureType,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(Feature feature, FeatureType type,
        ValidationResults results) throws Exception {
        Object ft = feature.getAttribute(attributeName);

        if (ft == null) {
            results.error(feature, attributeName + " is Empty");

            return false;
        }

        if (ft instanceof Number) {
            Number nb = (Number) ft;

            if (nb.intValue() == 0) {
                results.error(feature, attributeName + " is Zero");

                return false;
            }
        }

        return true;
    }

    /**
     * Implement setName.
     *
     * @see org.geotools.validation.Validation#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Implement getName.
     *
     * @see org.geotools.validation.Validation#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * Implement setDescription.
     *
     * @see org.geotools.validation.Validation#setDescription(java.lang.String)
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Implement getDescription.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     *
     * @see org.geotools.validation.Validation#getDescription()
     */
    public String getDescription() {
        return description;
    }

    /**
     * Implement getPriority.
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return 0;
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
     * Access typeName property.
     *
     * @return Returns the typeName.
     */
    public String getTypeRef() {
        return typeRef;
    }

    /**
     * Set typeName to typeName.
     *
     * @param typeName The typeName to set.
     */
    public void setTypeRef(String typeName) {
        this.typeRef = typeName;
    }
    
    /**
     * Access attributeName property.
     *
     * @return the path being stored for validation
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * set AttributeName to name. 
     * @param name
     */public void setAttributeName(String name) {
        attributeName = name;
    }
}
