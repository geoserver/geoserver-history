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
 * @author $Author: jive $ (last modification)
 * @version $Id: RangeValidation.java,v 1.1 2004/01/31 00:24:06 jive Exp $
 */
public class RangeValidation implements FeatureValidation {
    private String typeRef;
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
    public RangeValidation() {
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
     *      org.geotools.feature.FeatureType,
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
     * Set type ref as dataStoreId:typeName.
     * 
     * @param typeRef type referenced 
     *
     * @see org.geotools.validation.Validation#setTypeNames(java.lang.String[])
     */
    public void setTypeRef( String typeRef ) {
        this.typeRef = typeRef;
    }

    public String getTypeRef(){
        return typeRef;
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
