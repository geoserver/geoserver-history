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
 * RangeFeatureValidation validates that a number is within a given range.
 * 
 * <p>
 * RangeFeatureValidation is a quick and simple class the checks that the given
 * number resides within a given range.
 * </p>
 * 
 * <p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * Default max value is Integer.MAX_VALUE;
 * </li>
 * <li>
 * Default min value is Integer.MIN_VALUE;
 * </li>
 * <li>
 * If only one boundary of the range is set, only that boundary is checked.
 * </li>
 * <li>
 * The value of the integer is contained in the field specified by path.
 * </li>
 * </ul>
 * 
 * Example Use:
 * <pre><code>
 * RangeFeatureValidation x = new RangeFeatureValidation();
 * 
 * x.setMin(3);
 * x.setMax(5);
 * x.setPath("id");
 * 
 * boolean result = x.validate(feature, featureType, results);
 * </code></pre>
 * </p>
 *
 * @author rgould, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @version $Id: RangeFeatureValidation.java,v 1.3.2.3 2004/01/05 22:14:44 dmzwiers Exp $
 */
public class RangeFeatureValidation implements FeatureValidation {
    private String[] names;
    private String description;
    private String name;
    private int max = Integer.MAX_VALUE;
    private int min = Integer.MIN_VALUE;
    private String path;

    /**
     * RangeFeatureValidation constructor.
     * 
     * <p>
     * Description
     * </p>
     */
    public RangeFeatureValidation() {
        super();
    }

    /**
     * Override validate.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param feature
     * @param type
     * @param results
     *
     * @return
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

            if (nb.intValue() < min) {
                results.error(feature, path + " is less than " + min);

                return false;
            }

            if (nb.intValue() > max) {
                results.error(feature, path + " is greater than " + max);

                return false;
            }
        }

        return true;
    }

    /**
     * Override setName.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param name
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
     * Description ...
     * </p>
     *
     * @return
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
     * Description ...
     * </p>
     *
     * @param description
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
     * Override getPriority.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     *
     * @see org.geotools.validation.Validation#getPriority()
     */
    public int getPriority() {
        return 0;
    }

    /**
     * Override setTypeNames.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param names
     *
     * @see org.geotools.validation.Validation#setTypeNames(java.lang.String[])
     */
    public void setTypeNames(String[] names) {
        this.names = names;
    }

    /**
     * Override getTypeNames.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     *
     * @see org.geotools.validation.Validation#getTypeNames()
     */
    public String[] getTypeNames() {
        return names;
    }

    /**
     * getMax purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public int getMax() {
        return max;
    }

    /**
     * getMin purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public int getMin() {
        return min;
    }

    /**
     * getPath purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * setMax purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */
    public void setMax(int i) {
        max = i;
    }

    /**
     * setMin purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param i
     */
    public void setMin(int i) {
        min = i;
    }

    /**
     * setPath purpose.
     * 
     * <p>
     * Description ...
     * </p>
     *
     * @param string
     */
    public void setPath(String string) {
        path = string;
    }
}
