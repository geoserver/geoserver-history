/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
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
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: NullZeroFeatureValidation.java,v 1.2.2.3 2004/01/05 22:14:44 dmzwiers Exp $
 */
public class NullZeroFeatureValidation implements FeatureValidation {
    private String path;
    private String name = "";
    private String description = "";
    private String[] typeNames;

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
     *      org.geotools.feature.FeatureTypeInfo,
     *      org.geotools.validation.ValidationResults)
     */
    public boolean validate(Feature feature, FeatureType type,
        ValidationResults results) throws Exception {
        Object ft = feature.getAttribute(path);

        if (ft == null) {
            results.error(feature, path + " is Empty");

            return false;
        }

        if (ft instanceof Number) {
            Number nb = (Number) ft;

            if (nb.intValue() == 0) {
                results.error(feature, path + " is Zero");

                return false;
            }
        }

        return true;
    }

    /**
     * Implement setName.
     * 
     *
     * @see org.geotools.validation.Validation#setName(java.lang.String)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Implement getName.
     * 
     *
     * @see org.geotools.validation.Validation#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * Implement setDescription.
     * 
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
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return 0;
    }

    /**
     * Implement setTypeNames.
     * 
     *
     * @see org.geotools.validation.Validation#setTypeNames(java.lang.String[])
     */
    public void setTypeNames(String[] names) {
        typeNames = names;
    }

    /**
     * Implement getTypeNames.
     * 
     *
     * @see org.geotools.validation.Validation#getTypeNames()
     */
    public String[] getTypeNames() {
        return typeNames;
    }

    /**
     * getPath purpose.
     * 
     *
     * @return the path being stored for validation
     */
    public String getPath() {
        return path;
    }

    /**
     * setPath purpose.
     * 
     *
     * @param string the path which would be used if the validation method was executed.
     * 
     * @see org.geotools.validation.NullZeroFeatureValidation#validate(org.geotools.feature.Feature,
     *      org.geotools.feature.FeatureTypeInfo,
     *      org.geotools.validation.ValidationResults)
     */
    public void setPath(String string) {
        path = string;
    }
}
