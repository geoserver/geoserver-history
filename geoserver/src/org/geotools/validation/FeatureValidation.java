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
package org.geotools.validation;

import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;


/**
 * Defined a per Feature validation test.
 * 
 * <p>
 * FeatureValidation PlugIns are used to check geospatial information for
 * internal consistency.  Each ValidationPlugIn is very specific in nature: it
 * performs one test extermly well.  This simplifies design decisions,
 * documenation configuration and use.
 * </p>
 * 
 * <p>
 * Following the lead the excelent design work in the JUnit testing framework
 * validation results are collected by a ValidationResults object. This
 * interface for the ValidationResults object also allows it to collect
 * warning information.  The PlugIn is also required to supply some metadata
 * to aid in its deployment, scripting, logging and execution and error
 * recovery.
 * </p>
 * 
 * <p>
 * Capabilities:
 * 
 * <ul>
 * <li>
 * Uses FeatureResults to allow environment to gather error/warning information
 * as required (transaction XML document, JTable, logging system, etc...)
 * </li>
 * <li>
 * Primiarly used as part of processing an Insert Element in the Transaction
 * opperation of a Web Feature Server. (Allows us to fail a Feature without
 * bothering the Database)
 * </li>
 * </ul>
 * 
 * Example Use (feature: id=1, name="foo", geom=linestring):
 * <pre><code>
 * RangeFeatureValidation test = new RangeFeatureValidation();
 * 
 * results.setValidation( test );
 * test.setMin(0);
 * test.validate( feature, featureType, results ); // true
 * test.setMin(2);
 * test.validate( feature, featureType, results ); // false
 * </code></pre>
 * </p>
 *
 * @author Jody Garnett, Refractions Research, Inc.
 * @version $Id: FeatureValidation.java,v 1.3.2.2 2004/01/03 00:20:14 dmzwiers Exp $
 */
public interface FeatureValidation extends Validation {
    /**
     * Used to check features against this validation rule.
     *
     * @param feature Feature to be Validated
     * @param type GlobalFeatureType schema of feature
     * @param results coallate results information
     *
     * @return True if all the features pass this test.
     */
    public boolean validate(Feature feature, FeatureType type,
        ValidationResults results) throws Exception;
}
